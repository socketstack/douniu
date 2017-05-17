package com.rxqp.dn.server.bussiness.biz.impl;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.rxqp.common.dn.constants.ExcMsgConstants;
import com.rxqp.common.dn.constants.MessageConstants;
import com.rxqp.common.dn.data.CommonData;
import com.rxqp.dn.common.utils.BeanCopy;
import com.rxqp.dn.exception.BusinnessException;
import com.rxqp.dn.protobuf.DdzProto;
import com.rxqp.dn.protobuf.DdzProto.CreateNNRoomReq;
import com.rxqp.dn.protobuf.DdzProto.EntryNNRoomReq;
import com.rxqp.dn.protobuf.DdzProto.EntryNNRoomResp;
import com.rxqp.dn.protobuf.DdzProto.MESSAGE_ID;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo.Builder;
import com.rxqp.dn.protobuf.DdzProto.NNAnswerDissolutionReq;
import com.rxqp.dn.protobuf.DdzProto.NNDissolutionReq;
import com.rxqp.dn.protobuf.DdzProto.NNType;
import com.rxqp.dn.protobuf.DdzProto.PostDissolutionResp;
import com.rxqp.dn.protobuf.DdzProto.PostNNEntryRoom;
import com.rxqp.dn.protobuf.DdzProto.RoomInfo;
import com.rxqp.dn.protobuf.DdzProto.SettlementData;
import com.rxqp.dn.protobuf.DdzProto.SettlementInfo;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.IRoomBiz;

@Service
public class RoomBizImpl implements IRoomBiz {

	private ICommonBiz commonBiz = new CommonBiz();

	private static Integer ROOM_ID_LENGTH = 1000;

	// 使用中的房号
	private static Set<Integer> usedRoomIds = Collections
			.synchronizedSet(new HashSet<Integer>());

	@Override
	public Builder createNewRoom(MessageInfo messageInfoReq) {
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		try {
			CreateNNRoomReq.Builder creatRoomReq = messageInfoReq
					.getCreateNNRoomReq().toBuilder();
			Integer games = creatRoomReq.getGames();// 房间场数
			Integer type = creatRoomReq.getType();// type:1--房主支付房费 3--各自支付房费
			Integer playerId = creatRoomReq.getPlayerId();
			messageInfo.setMessageId(MESSAGE_ID.msg_CreateNNRoomResp);

			Integer roomId = -1;
			Player player = CommonData.getPlayerById(playerId);
			if (!player.getIsland()) {// 该玩家未登陆
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
						MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
				return messageInfo;
			}
			if (player.getOnPlay()) {// 该玩家玩牌中
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_PLAYING_TYPE,
						MessageConstants.PLAYER_PLAYING_MSG);
				return messageInfo;
			}
			try {
				roomId = createNewRoom(games, type, playerId);
			} catch (BusinnessException e) {
				if (ExcMsgConstants.NO_EXISTS_THE_ROOM_EXC_CODE.equals(e
						.getCode())) {// 该房间不存在
					messageInfo = commonBiz.setMessageInfo(
							MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
							MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
					return messageInfo;
				} else {
					messageInfo = commonBiz.setMessageInfo(
							MessageConstants.UNKNOWN_CAUSE_TYPE,
							MessageConstants.UNKNOWN_CAUSE_MSG);
					return messageInfo;
				}
			}
			if (roomId.equals(-1)) {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.CREATE_ROOM_ERROR_TYPE,
						MessageConstants.CREATE_ROOM_ERROR_MSG);
				return messageInfo;
			}
			player.setRoomId(roomId);
			player.setOnPlay(true);
			player.setOrder(1);// 创建房间的人座位顺序为1
			player.setIsBanker(true);// 庄家
			CommonData.putPlayerIdToPlayer(playerId, player);

			DdzProto.CreateNNRoomResp.Builder createRoomResp = DdzProto.CreateNNRoomResp
					.newBuilder();
			createRoomResp.setRoomId(roomId);

			messageInfo.setCreateNNRoomResp(createRoomResp);
		} catch (Exception e) {
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.CREATE_ROOM_ERROR_TYPE,
					MessageConstants.CREATE_ROOM_ERROR_MSG);
		}

		return messageInfo;
	}

	private Integer createNewRoom(Integer games, Integer type, Integer playerId)
			throws BusinnessException {
		Integer roomId = (int) ((Math.random() * 9 + 1) * ROOM_ID_LENGTH);
		int cnt = 0;
		while (usedRoomIds.contains(roomId)) {
			roomId = (int) ((Math.random() * 9 + 1) * ROOM_ID_LENGTH);
			cnt++;
			if (cnt >= 1000) {
				return -1;
			}
		}
		usedRoomIds.add(roomId);
		Player player = CommonData.getPlayerById(playerId);
		if (player == null) {// 说明没有登录
			throw new BusinnessException(ExcMsgConstants.NO_LOGIN_ERROR_CODE,
					ExcMsgConstants.NO_LOGIN_ERROR_MSG);
		} else {
			Room room = new Room();
			room.setRoomId(roomId);
			List<Player> players = new ArrayList<Player>();
			players.add(player);
			room.setPlayers(players);
			room.setTotalGames(games);
			room.setType(type);
			room.setBankerId(playerId);// 第一盘房主为庄家
			CommonData.putRoomIdToRoom(roomId, room);
		}
		return roomId;
	}

	@Override
	public Builder entryRoom(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		messageInfo.setMessageId(MESSAGE_ID.msg_EntryNNRoomResp);

		EntryNNRoomResp.Builder entryRoomResp = EntryNNRoomResp.newBuilder();

		EntryNNRoomReq req = messageInfoReq.getEntryNNRoomReq();
		Integer roomId = req.getRoomId();
		Room room = CommonData.getRoomByRoomId(roomId);
		if (room == null) {
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
					MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
			return messageInfo;
		}
		if (room.getIsStartGame()) {// 已经开始游戏，则不容许再进入
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.ENTRY_ROOM_ERROR_TYPE_4002,
					MessageConstants.ENTRY_ROOM_ERROR_MSG_4002);
			return messageInfo;
		}
		Integer playerId = req.getPlayerId();
		List<Player> players;
		players = room.getPlayers();
		if (players == null) {
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.ENTRY_ROOM_ERROR_TYPE_4000,
					MessageConstants.ENTRY_ROOM_ERROR_MSG_4000);
			return messageInfo;
		} else if (players.size() > 5) {// 斗牛每一房间最多5个玩家
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.ENTRY_ROOM_ERROR_TYPE_4001,
					MessageConstants.ENTRY_ROOM_ERROR_MSG_4001);
			return messageInfo;
		} else {
			Player player = CommonData.getPlayerById(playerId);
			if (player == null || !player.getIsland()) {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
						MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
				return messageInfo;
			} else if (player.getOnPlay()) {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_STATE_TYPE_1002,
						MessageConstants.PLAYER_STATE_MSG_1002);
				return messageInfo;
			}
			// 设置下一位玩家的ID
			int existPlayersCnt = players.size();// 当前已经进入房间的玩家数量
			players.get(existPlayersCnt - 1).setNextPlayerId(playerId);// 将其设置为前一位玩家的下家
			if (existPlayersCnt == 4) {// 当前玩家是第五位进入房间的玩家
				player.setNextPlayerId(players.get(0).getId());// 其下一位玩家是第一位玩家
			}
			if (!player.getIsland()) {// 该玩家未登陆
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
						MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
				return messageInfo;
			}
			if (player.getOnPlay()) {// 改玩家玩牌中
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_PLAYING_TYPE,
						MessageConstants.PLAYER_PLAYING_MSG);
				return messageInfo;
			}
			player.setRoomId(roomId);
			player.setOnPlay(true);
			player.setOrder(players.size() + 1);// 玩家按先后顺序第N位进入房间，方便前端安排座位顺序
			CommonData.putPlayerIdToPlayer(playerId, player);
			players.add(player);
			RoomInfo.Builder roomInfo = RoomInfo.newBuilder();
			roomInfo.setRoomId(roomId);
			roomInfo.addAllPlayers(BeanCopy.playersCopy(players));
			roomInfo.setTotalGames(room.getTotalGames());
			entryRoomResp.setRoomInfo(roomInfo);
			entryRoomResp.setOrder(players.size());// 玩家按先后顺序第N位进入房间，方便前端安排座位顺序
			// 广播房间里其他玩家
			for (Player py : players) {
				if (py.getId().equals(playerId))// 自己不用通知
					continue;
				py.getChannel().writeAndFlush(setPostEntryRoom(player));
			}
		}

		messageInfo.setEntryNNRoomResp(entryRoomResp);
		return messageInfo;
	}

	private MessageInfo setPostEntryRoom(Player player) {
		MessageInfo.Builder builder = MessageInfo.newBuilder();
		builder.setMessageId(MESSAGE_ID.msg_PostNNEntryRoom);
		PostNNEntryRoom.Builder entryRoom = PostNNEntryRoom.newBuilder();
		entryRoom.setPlayer(BeanCopy.playerCopy(player));

		builder.setPostNNEntryRoom(entryRoom.build());

		return builder.build();
	}

	@Override
	public Boolean removeRoom(Integer roomId) {
		try {
			usedRoomIds.remove(roomId);
			List<Player> players = CommonData.getPlayersByRoomId(roomId);
			for (Player player : players) {
				player.setOnPlay(false);
			}
			CommonData.removeRoom(roomId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Builder disolutionRoom(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		NNDissolutionReq req = messageInfoReq.getNnDissolutionReq();
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
		List<Player> players = room.getPlayers();
		// 广播其他玩家有玩家请求解散房间
		if (CollectionUtils.isNotEmpty(players)) {
			MessageInfo.Builder postMsgInfo = MessageInfo.newBuilder();
			postMsgInfo.setMessageId(MESSAGE_ID.msg_PostDissolutionResp);
			PostDissolutionResp.Builder postDissolutionResp = PostDissolutionResp
					.newBuilder();
			postDissolutionResp.setPlayerid(playerId);
			postMsgInfo.setPostDissolutionResp(postDissolutionResp);
			for (Player pl : players) {
				pl.getChannel().writeAndFlush(postMsgInfo.build());
			}
		}
		return null;
	}

	@Override
	public Builder answerDissolution(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		NNAnswerDissolutionReq req = messageInfoReq.getNnAnswerDissolutionReq();
		Integer playerId = req.getPlayerId();
		Boolean isAgree = req.getIsAgree();// 是否同意解散，1:表示同意 2:表示不同意
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
		if (isAgree) {
			room.increaseAgreeDissolutionCnt();
		}

		Integer playerCnt = room.getPlayers().size();
		Boolean isSuccess = false;
		if (playerCnt == 2 || playerCnt == 3) {
			if (room.getAgreeDissolutionCnt() >= 2) {
				isSuccess = true;
			}
		} else if (playerCnt == 4 || playerCnt == 5) {
			if (room.getAgreeDissolutionCnt() >= 3) {
				isSuccess = true;
			}
		}
		if (isSuccess) {
			List<Player> players = room.getPlayers();
			// 广播其他玩家有玩家请求解散房间
			if (CollectionUtils.isNotEmpty(players)) {
				MessageInfo.Builder postMsgInfo = MessageInfo.newBuilder();
				postMsgInfo.setMessageId(MESSAGE_ID.msg_PostDissolutionResp);
				PostDissolutionResp.Builder postDissolutionResp = PostDissolutionResp
						.newBuilder();
				postDissolutionResp.setPlayerid(playerId);
				postMsgInfo.setPostDissolutionResp(postDissolutionResp);
				MessageInfo.Builder mi = buildSettlementData(room);
				for (Player pl : players) {
					pl.getChannel().writeAndFlush(postMsgInfo.build());// 广播解散房间成功
					pl.getChannel().writeAndFlush(mi.build());// 广播计算结算信息
				}
				removeRoom(room.getRoomId());// 删除该房间信息
			}
		}
		return null;
	}

	/**
	 * 结算信息
	 * 
	 * @param pl
	 * @param room
	 * @return
	 */
	public Builder buildSettlementData(Room room) {
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
}
