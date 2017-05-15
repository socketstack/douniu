package com.rxqp.dn.server.bussiness.biz.impl;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.rxqp.common.dn.constants.ExcMsgConstants;
import com.rxqp.common.dn.constants.MessageConstants;
import com.rxqp.common.dn.data.CommonData;
import com.rxqp.dn.exception.BusinnessException;
import com.rxqp.dn.protobuf.DdzProto;
import com.rxqp.dn.protobuf.DdzProto.MESSAGE_ID;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo.Builder;
import com.rxqp.dn.protobuf.DdzProto.NNPrepareReq;
import com.rxqp.dn.protobuf.DdzProto.NNShowCardsResp;
import com.rxqp.dn.protobuf.DdzProto.NNType;
import com.rxqp.dn.protobuf.DdzProto.PostNNDealResp;
import com.rxqp.dn.protobuf.DdzProto.PostNNPrepareResp;
import com.rxqp.dn.protobuf.DdzProto.PostNNShowCards;
import com.rxqp.dn.protobuf.DdzProto.PostStakeOver;
import com.rxqp.dn.protobuf.DdzProto.PostStakeResp;
import com.rxqp.dn.protobuf.DdzProto.PostStartNNGame;
import com.rxqp.dn.protobuf.DdzProto.SettlementData;
import com.rxqp.dn.protobuf.DdzProto.SettlementInfo;
import com.rxqp.dn.protobuf.DdzProto.StakeReq;
import com.rxqp.dn.protobuf.DdzProto.StakeResp;
import com.rxqp.dn.protobuf.DdzProto.StartNNGameReq;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.IGameBiz;
import com.rxqp.dn.server.bussiness.biz.IRoomBiz;

public class GameBizImpl implements IGameBiz {

	// 房间号对应待发的扑克牌
	private static Map<Integer, LinkedList<Integer>> roomIdToPokerIds = new ConcurrentHashMap<Integer, LinkedList<Integer>>();

	private ICommonBiz commonBiz = new CommonBiz();

	private IRoomBiz roomBiz = new RoomBizImpl();

	@Override
	public Builder dealProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		try {
			MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
			NNPrepareReq req = messageInfoReq.getNnPrepareReq();
			Integer playerId = req.getPlayerId();
			Player player = CommonData.getPlayerById(playerId);
			if (player == null) {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
						MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
				return messageInfo;
			}
			Room room = CommonData.getRoomByRoomId(player.getRoomId());
			if (room == null) {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
				return messageInfo;
			}
			room.increasePreparedPlayerCnt();
			List<Player> players = CommonData
					.getPlayersByIdInSameRoom(playerId);
			for (Player pl : players) {
				MessageInfo.Builder msg = MessageInfo.newBuilder();
				msg.setMessageId(MESSAGE_ID.msg_PostNNPrepareResp);
				PostNNPrepareResp.Builder postprepareResp = PostNNPrepareResp
						.newBuilder();
				postprepareResp.setPlayerId(playerId);
				msg.setPostNNPrepareResp(postprepareResp);
				pl.getChannel().writeAndFlush(msg.build());
			}

			if (room.getPreparedPlayerCnt().equals(room.getPlayers().size())) {// 所有玩家准备就绪，开始发牌
				LinkedList<Integer> remainderPokerIds = roomIdToPokerIds
						.get(player.getRoomId());
				for (Player pl : players) {
					DdzProto.MessageInfo mi = shuffleDeal(pl, remainderPokerIds);
					pl.getChannel().writeAndFlush(mi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 洗牌发牌 player：表示当前请求发牌的玩家 pks:表示还未发出去的牌
	 * 
	 * @return
	 */
	private DdzProto.MessageInfo shuffleDeal(Player player,
			LinkedList<Integer> pks) {
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		messageInfo.setMessageId(MESSAGE_ID.msg_PostNNDealResp);

		Integer playerId = player.getId();
		Room room = CommonData.getRoomByRoomId(player.getRoomId());
		PostNNDealResp.Builder postDealResp = PostNNDealResp.newBuilder();
		postDealResp.setPlayerId(playerId);

		if (CollectionUtils.isEmpty(pks)) {
			pks = initPokers();
		}
		List<Integer> pokerIds = getPokers(pks);
		player.setPokerIds(pokerIds);
		roomIdToPokerIds.put(player.getRoomId(), pks);
		postDealResp.addAllPokers(pokerIds);
		postDealResp.setPlayedGames(room.getPlayedGames());
		postDealResp.setTotalGames(room.getTotalGames());
		if (player.getId().equals(room.getBankerId()))// 是否庄家
			postDealResp.setIsBanker(true);
		else
			postDealResp.setIsBanker(false);
		messageInfo.setNnDealResp(postDealResp);

		return messageInfo.build();
	}

	private List<Integer> getPokers(List<Integer> pks) {
		List<Integer> pokers = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 3; i > 0; i--) {
			int s = random.nextInt(pks.size());
			pokers.add(pks.get(s));
			pks.remove(s);
		}
		return pokers;
	}

	private LinkedList<Integer> initPokers() {
		LinkedList<Integer> pokers = new LinkedList<Integer>();
		for (Integer i = 1; i <= 52; i++) {
			pokers.add(i);
		}
		return pokers;
	}

	@Override
	public Builder startNNGame(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
		msgInfo.setMessageId(MESSAGE_ID.msg_StartNNGameResp);

		StartNNGameReq req = messageInfoReq.getStartNNGame();
		Integer roomOwnerId = req.getPlayerid();// 该房间房主ID，也就是创建房间玩家
		List<Player> players = null;
		try {
			players = CommonData.getPlayersByIdInSameRoom(roomOwnerId);// 获取同一房间其他玩家信息
			Player player = CommonData.getPlayerById(roomOwnerId);
			Room room = CommonData.getRoomByRoomId(player.getRoomId());
			if (room.getPlayers() == null && room.getPlayers().size() < 2) {// 至少两个玩家才能开牌
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_STATE_TYPE_1005,
						MessageConstants.PLAYER_STATE_MSG_1005);
				return msgInfo;
			}
			room.setIsStartGame(true);
		} catch (BusinnessException e) {
			if (ExcMsgConstants.NO_EXISTS_THE_ROOM_EXC_CODE.equals(e.getCode())) {// 该房间不存在
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
				return msgInfo;
			} else {
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.UNKNOWN_CAUSE_TYPE,
						MessageConstants.UNKNOWN_CAUSE_MSG);
				return msgInfo;
			}
		}
		// 广播其他玩家开始游戏
		if (CollectionUtils.isNotEmpty(players)) {
			MessageInfo.Builder postMsgInfo = MessageInfo.newBuilder();
			postMsgInfo.setMessageId(MESSAGE_ID.msg_PostStartNNGame);
			PostStartNNGame.Builder postStartNNGame = PostStartNNGame
					.newBuilder();
			postMsgInfo.setPostStartNNGame(postStartNNGame);
			for (Player pl : players) {
				pl.getChannel().writeAndFlush(postMsgInfo.build());
			}
		}
		return null;
	}

	/**
	 * 下注请求
	 */
	@Override
	public Builder stakeProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
		msgInfo.setMessageId(MESSAGE_ID.msg_StakeResp);
		StakeReq req = messageInfoReq.getStakeReq();
		Integer playerId = req.getPlayerid();
		Integer point = req.getPoint();

		Player player = CommonData.getPlayerById(playerId);
		player.setBetPoints(point);

		StakeResp.Builder resp = StakeResp.newBuilder();
		resp.setPoint(point);
		msgInfo.setStakeResp(resp);
		// players = CommonData.getPlayersByIdInSameRoom(playerId);//
		// 获取同一房间其他玩家信息
		Room room = CommonData.getRoomByRoomId(player.getRoomId());
		if (room == null) {
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
					MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
			return msgInfo;
		}
		room.increaseStakedPlayerCnt();
		List<Player> players = room.getPlayers();
		for (Player pl : players) {
			MessageInfo.Builder mi = MessageInfo.newBuilder();
			mi.setMessageId(MESSAGE_ID.msg_PostStakeResp);
			PostStakeResp.Builder postStake = PostStakeResp.newBuilder();
			postStake.setPlayerid(playerId);
			postStake.setPoint(point);
			mi.setPostStakeResp(postStake);
			if (!pl.getId().equals(playerId))
				pl.getChannel().writeAndFlush(mi.build());
		}
		if (room.getStakedPlayerCnt() >= room.getPlayers().size() - 1) {// 通知所有玩家，除了庄家外所有玩家都已经完成下注，可以看牌开牌了
			for (Player pl : players) {
				MessageInfo.Builder mi = MessageInfo.newBuilder();
				mi.setMessageId(MESSAGE_ID.msg_PostStakeOver);
				PostStakeOver.Builder postStakeOver = PostStakeOver
						.newBuilder();
				mi.setPostStakeOver(postStakeOver);
				if (!pl.getId().equals(playerId))
					pl.getChannel().writeAndFlush(mi.build());
			}
		}
		return msgInfo;
	}

	/**
	 * 开牌请求
	 */
	@Override
	public Builder showCardsProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
		msgInfo.setMessageId(MESSAGE_ID.msg_NNShowCardsResp);

		Integer playerId = messageInfoReq.getNnShowCardsReq().getPlayerid();
		boolean showAll = messageInfoReq.getNnShowCardsReq().getShowAll();// 是否公开牌
		Player player = CommonData.getPlayerById(playerId);
		List<Integer> remainderPokerIds = null;
		if (player.getPokerIds().size() < 5) {
			remainderPokerIds = getRemainderPokerIds(player.getRoomId());// 剩下的两张牌
			player.getPokerIds().addAll(remainderPokerIds);
		}
		NNType nntype = getNNType(player);
		player.setNntype(nntype);

		if (showAll) {// 开牌
			Room room = CommonData.getRoomByRoomId(player.getRoomId());
			if (room == null) {
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
				return msgInfo;
			}
			List<Player> players = room.getPlayers();

			for (Player pl : players) {
				MessageInfo.Builder mi = MessageInfo.newBuilder();
				mi.setMessageId(MESSAGE_ID.msg_PostNNShowCards);
				PostNNShowCards.Builder postNNShowCards = PostNNShowCards
						.newBuilder();
				postNNShowCards.setPlayerId(playerId);
				postNNShowCards.addAllPokers(player.getPokerIds());
				postNNShowCards.setNntype(nntype);
				mi.setPostNNShowCards(postNNShowCards);
				pl.getChannel().writeAndFlush(mi.build());
			}
			room.increaseShowCardsPlayerCnt();// 开牌人数加1
			if (room.getShowCardsPlayerCnt().equals(room.getPlayers().size())) {// 所有玩家已开牌，则进行结算
				room.increasePlayedGamesCnt();// 已玩局数加1
				MessageInfo.Builder mi = buildSettlementData(room);// 计算结算信息
				for (Player pl : players) {
					pl.getChannel().writeAndFlush(mi.build());
				}
				if (room.getPlayedGames() >= room.getTotalGames()) {// 该房间的房卡局数已经结束
					roomBiz.removeRoom(room.getRoomId());// 删除该房间信息
				} else {// 每一小局完了，需要初始化房间对象的相关数据
					room.init();
				}
			}
			return null;
		}
		NNShowCardsResp.Builder resp = NNShowCardsResp.newBuilder();
		resp.setPlayerId(playerId);
		resp.addAllPokers(remainderPokerIds);

		resp.setNntype(nntype);
		msgInfo.setNnShowCardsResp(resp);
		return msgInfo;
	}

	/**
	 * 结算信息
	 * 
	 * @param pl
	 * @param room
	 * @return
	 */
	private Builder buildSettlementData(Room room) {
		MessageInfo.Builder mi = MessageInfo.newBuilder();
		mi.setMessageId(MESSAGE_ID.msg_SettlementInfo);
		SettlementInfo.Builder settlementInfo = SettlementInfo.newBuilder();
		if (room.getPlayedGames() >= room.getTotalGames())
			settlementInfo.setIsOver(true);
		else
			settlementInfo.setIsOver(false);
		List<Player> players = room.getPlayers();
		Player banker = CommonData.getPlayerById(room.getBankerId());// 庄家
		if (banker.getNntype().equals(NNType.NNT_ERROR)) {// 庄家牌类型有误
			mi = commonBiz.setMessageInfo(
					MessageConstants.BANKER_CARDS_ERROR_TYPE,
					MessageConstants.BANKER_CARDS_ERROR_MSG);
			return mi;
		}
		SettlementData.Builder bankerSettlement = SettlementData.newBuilder();// 庄家的结算信息
		bankerSettlement.setID(banker.getId());
		for (Player player : players) {
			SettlementData.Builder playerSettlement = SettlementData
					.newBuilder();// 非庄家玩家各自的结算信息
			playerSettlement.setID(player.getId());
			if (!player.getIsBanker()) {
				Integer score = comparePoints(bankerSettlement, banker, player);
				playerSettlement.setGotscore(score);
				Integer playerFinalScore = player.getFinalScore() + score;
				playerSettlement.setFinalscore(playerFinalScore);
				player.setFinalScore(playerFinalScore);
				player.setScore(score);
				if (score > 0) {
					playerSettlement.setIsWin(true);
				} else {
					playerSettlement.setIsWin(false);
				}
				settlementInfo.addPlayers(playerSettlement);
			}
		}
		Integer bankerFinalcore = banker.getFinalScore()
				+ bankerSettlement.getGotscore();
		bankerSettlement.setFinalscore(bankerFinalcore);
		banker.setFinalScore(bankerFinalcore);
		banker.setScore(bankerSettlement.getGotscore());
		if (bankerSettlement.getGotscore() > 0) {
			bankerSettlement.setIsWin(true);
		} else {
			bankerSettlement.setIsWin(false);
		}
		settlementInfo.addPlayers(bankerSettlement);
		mi.setSettlementInfo(settlementInfo);
		return mi;
	}

	/**
	 * 返回普通玩家得分
	 * 
	 * @param bankerSettlement
	 * @param bankerPlayer
	 * @param player
	 * @return
	 */
	private Integer comparePoints(SettlementData.Builder bankerSettlement,
			Player bankerPlayer, Player player) {
		NNType bankerNntype = bankerPlayer.getNntype();
		NNType playerNntype = player.getNntype();
		if (playerNntype.equals(NNType.NNT_ERROR))
			return 0;
		if (bankerNntype.getNumber() > playerNntype.getNumber()) {// 如果庄家牌型大
			int rate = getRateByNNType(bankerNntype);
			int score = player.getBetPoints() * rate;
			bankerSettlement
					.setGotscore(bankerSettlement.getGotscore() + score);
			return (-1) * score;
		} else if (bankerNntype.getNumber() < playerNntype.getNumber()) {// 如果普通玩家牌型大
			int rate = getRateByNNType(playerNntype);
			int score = player.getBetPoints() * rate;
			bankerSettlement
					.setGotscore(bankerSettlement.getGotscore() - score);
			return score;
		} else {// 牌型一样
			List<Integer> bankerPids = bankerPlayer.getPokerIds();
			List<Integer> playerPids = player.getPokerIds();
			Integer bankPid = Collections.max(bankerPids);
			Integer playerPid = Collections.max(playerPids);
			if (bankPid > playerPid) {// 庄家牌大
				int rate = getRateByNNType(bankerNntype);
				int score = player.getBetPoints() * rate;
				bankerSettlement.setGotscore(bankerSettlement.getGotscore()
						+ score);
				return (-1) * score;
			} else if (bankPid < playerPid) {// 普通玩家牌大
				int rate = getRateByNNType(playerNntype);
				int score = player.getBetPoints() * rate;
				bankerSettlement.setGotscore(bankerSettlement.getGotscore()
						- score);
				return score;
			} else {
				return 0;
			}
		}
	}

	private int getRateByNNType(NNType nntype) {
		switch (nntype) {
		case NNT_SPECIAL_BOMEBOME:
			return 5;
		case NNT_SPECIAL_NIUHUA:
			return 5;
		case NNT_SPECIAL_NIUNIU:
			return 4;
		case NNT_SPECIAL_NIU9:
			return 3;
		case NNT_SPECIAL_NIU8:
			return 2;
		default:
			return 1;
		}
	}

	/**
	 * 获取当前玩家扑克的牛牛类型
	 * 
	 * @param player
	 * @return
	 */
	private NNType getNNType(Player player) {
		List<Integer> pokerIds = player.getPokerIds();
		List<Integer> points = new ArrayList<Integer>();
		if (isBomb(pokerIds)) {// 是否炸弹
			return NNType.NNT_SPECIAL_BOMEBOME;
		}
		if (isFiveFlow(pokerIds)) {// 是否五花牛
			return NNType.NNT_SPECIAL_NIUHUA;
		}
		for (Integer pid : pokerIds) {
			Integer point = pid % 13;
			if (point.equals(12)) {// A扑克牌
				points.add(1);
			} else if (point > 8) {// J、Q、K扑克牌,算10点
				points.add(10);
			} else {
				points.add(point + 2);
			}
		}
		List<Integer> otherCnts = new ArrayList<Integer>();
		Boolean hasNiu = false;
		for (Integer i = 0; i < points.size(); i++) {
			Integer a = points.get(i);
			for (Integer j = i + 1; j < points.size(); j++) {
				Integer b = points.get(j);
				for (Integer k = j + 1; k < points.size(); k++) {
					Integer c = points.get(k);
					if ((a + b + c) % 10 == 0) {// 有牛
						Integer cnt = 0;
						hasNiu = true;
						for (Integer n = 0; n < points.size(); n++) {// 算另两张牌点数
							if (n != i && n != j && n != k) {
								cnt += points.get(n);
							}
						}
						int niuNum = cnt % 10;
						if (niuNum == 0) {// 牛牛
							return NNType.NNT_SPECIAL_NIUNIU;
						} else {
							otherCnts.add(niuNum);// 可能存在多种牛的情况，要取最大的值
						}
					}
				}
			}
		}
		if (hasNiu) {
			Collections.sort(otherCnts);
			if (otherCnts.size() > 0) {
				return getNNTypeByNum(otherCnts.get(otherCnts.size() - 1));
			} else {
				return getNNTypeByNum(-1);
			}
		} else {
			return NNType.NNT_NONE;
		}
	}

	private NNType getNNTypeByNum(Integer num) {
		switch (num) {
		case 1:
			return NNType.NNT_SPECIAL_NIU1;
		case 2:
			return NNType.NNT_SPECIAL_NIU2;
		case 3:
			return NNType.NNT_SPECIAL_NIU3;
		case 4:
			return NNType.NNT_SPECIAL_NIU4;
		case 5:
			return NNType.NNT_SPECIAL_NIU5;
		case 6:
			return NNType.NNT_SPECIAL_NIU6;
		case 7:
			return NNType.NNT_SPECIAL_NIU7;
		case 8:
			return NNType.NNT_SPECIAL_NIU8;
		case 9:
			return NNType.NNT_SPECIAL_NIU9;
		default:
			return NNType.NNT_ERROR;
		}
	}

	/**
	 * 是否五花牛
	 * 
	 * @param pokerIds
	 * @return
	 */
	private Boolean isFiveFlow(List<Integer> pokerIds) {
		for (Integer pokerId : pokerIds) {
			if ((pokerId % 13) <= 10) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否炸弹
	 * 
	 * @param pokers
	 * @return
	 */
	private static Boolean isBomb(List<Integer> pokerIds) {
		boolean isBomb = false;
		List<Integer> points = new ArrayList<Integer>();
		for (Integer pid : pokerIds) {
			points.add(pid % 13);
		}
		Collections.sort(points);
		Integer sameCnt = 1;
		for (int i = 0; i < points.size() - 1; i++) {
			Integer pokerId = points.get(i);
			Integer pid = points.get(i + 1);
			if (pokerId.equals(pid)) {
				sameCnt++;
				if (sameCnt == 4)
					return true;
			} else {
				sameCnt = 1;
			}
		}
		if (sameCnt == 4)
			isBomb = true;
		return isBomb;
	}

	/**
	 * 判断是否是10点
	 * 
	 * @param point
	 * @return
	 */
	private boolean isTenPoint(Integer point) {
		if (point == 8 || point == 9 || point == 10 || point == 11) {
			return true;
		} else {
			return false;
		}
	}

	private List<Integer> getRemainderPokerIds(Integer roomId) {
		LinkedList<Integer> remainderPokerIds = roomIdToPokerIds.get(roomId);
		List<Integer> pokerIds = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 2; i > 0; i--) {
			int s = random.nextInt(remainderPokerIds.size());
			pokerIds.add(remainderPokerIds.get(s));
			remainderPokerIds.remove(s);
		}
		return pokerIds;
	}
}
