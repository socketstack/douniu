package com.rxqp.dn.server.bussiness.biz.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
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
import com.rxqp.dn.protobuf.DdzProto.NNDealReq;
import com.rxqp.dn.protobuf.DdzProto.NNDealResp;
import com.rxqp.dn.protobuf.DdzProto.NNShowCardsResp;
import com.rxqp.dn.protobuf.DdzProto.NNType;
import com.rxqp.dn.protobuf.DdzProto.PostNNShowCards;
import com.rxqp.dn.protobuf.DdzProto.PostStakeResp;
import com.rxqp.dn.protobuf.DdzProto.PostStartNNGame;
import com.rxqp.dn.protobuf.DdzProto.StakeReq;
import com.rxqp.dn.protobuf.DdzProto.StakeResp;
import com.rxqp.dn.protobuf.DdzProto.StartNNGameReq;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.IGameBiz;

public class GameBizImpl implements IGameBiz {

	// 房间号对应待发的扑克牌
	private static Map<Integer, LinkedList<Integer>> roomIdToPokerIds = new ConcurrentHashMap<Integer, LinkedList<Integer>>();

	private ICommonBiz commonBiz = new CommonBiz();

	@Override
	public Builder dealProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		try {
			MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
			NNDealReq req = messageInfoReq.getNnDealReq();
			Integer playerId = req.getPlayerId();
			Player player = CommonData.getPlayerById(playerId);
			if (player == null) {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
						MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
				return messageInfo;
			}
			LinkedList<Integer> remainderPokerIds = roomIdToPokerIds.get(player
					.getRoomId());
			Channel channel = player.getChannel();
			DdzProto.MessageInfo mi = shuffleDeal(player, remainderPokerIds);
			channel.writeAndFlush(mi);
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
		messageInfo.setMessageId(MESSAGE_ID.msg_NNDealResp);

		Integer playerId = player.getId();
		Room room = CommonData.getRoomByRoomId(player.getRoomId());
		NNDealResp.Builder dealResp = NNDealResp.newBuilder();
		dealResp.setPlayerId(playerId);

		if (CollectionUtils.isEmpty(pks)) {
			pks = initPokers();
		}
		List<Integer> pokerIds = getPokers(pks);
		player.setPokerIds(pokerIds);
		roomIdToPokerIds.put(player.getRoomId(), pks);
		dealResp.addAllPokers(pokerIds);
		dealResp.setPlayedGames(room.getPlayedGames());
		dealResp.setRemainderGames(room.getRemainderGames());

		messageInfo.setNnDealResp(dealResp);

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
		for (Integer i = 1; i <= 54; i++) {
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
				if (!pl.getId().equals(roomOwnerId))
					pl.getChannel().writeAndFlush(postMsgInfo.build());
			}
		}
		return msgInfo;
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
		msgInfo.setStakeResp(resp);
		List<Player> players = null;
		try {
			players = CommonData.getPlayersByIdInSameRoom(playerId);// 获取同一房间其他玩家信息
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
		return msgInfo;
	}

	/**
	 * 开牌请求
	 */
	@Override
	public Builder showCardsProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
		msgInfo.setMessageId(MESSAGE_ID.msg_NNShowCardsReq);

		Integer playerId = messageInfoReq.getNnShowCardsReq().getPlayerid();
		Player player = CommonData.getPlayerById(playerId);

		NNShowCardsResp.Builder resp = NNShowCardsResp.newBuilder();
		resp.setPlayerId(playerId);
		List<Integer> remainderPokerIds = getRemainderPokerIds(player
				.getRoomId());
		resp.addAllPokers(remainderPokerIds);
		NNType nntype = getNNType(player);// TODO

		List<Player> players = null;
		try {
			players = CommonData.getPlayersByIdInSameRoom(playerId);// 获取同一房间其他玩家信息
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
		for (Player pl : players) {
			MessageInfo.Builder mi = MessageInfo.newBuilder();
			mi.setMessageId(MESSAGE_ID.msg_PostNNShowCards);
			PostNNShowCards.Builder postNNShowCards = PostNNShowCards
					.newBuilder();
			postNNShowCards.setPlayerId(playerId);
			postNNShowCards.addAllPokers(pl.getPokerIds());
			postNNShowCards.setNntype(nntype);
			mi.setPostNNShowCards(postNNShowCards);
			if (!pl.getId().equals(playerId))
				pl.getChannel().writeAndFlush(mi.build());
		}
		return null;
	}

	/**
	 * 获取当前玩家扑克的牛牛类型
	 * 
	 * @param player
	 * @return
	 */
	private NNType getNNType(Player player) {
		List<Integer> pokerIds = player.getPokerIds();
		MessageInfo.Builder msgInfo = MessageInfo.newBuilder();

		return null;
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
