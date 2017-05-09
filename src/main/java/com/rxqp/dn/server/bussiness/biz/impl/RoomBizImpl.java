package com.rxqp.dn.server.bussiness.biz.impl;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.rxqp.dn.protobuf.DdzProto.PostNNEntryRoom;
import com.rxqp.dn.protobuf.DdzProto.RoomInfo;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.IRoomBiz;

@Service
public class RoomBizImpl implements IRoomBiz {

	private ICommonBiz commonBiz = new CommonBiz();

	private static Integer ROOM_ID_LENGTH = 1000;

	// 使用中的房号
	private Set<Integer> usedRoomIds = Collections
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
			if (null != roomId) {
				Player player = CommonData.getPlayerById(playerId);
				if (player.getIsland() && !player.getOnPlay()) {// 该玩家登陆且不在玩牌中才可以创建房间
					try {
						roomId = createNewRoom(games, type, playerId);
					} catch (BusinnessException e) {
						if (ExcMsgConstants.NO_EXISTS_THE_ROOM_EXC_CODE
								.equals(e.getCode())) {// 该房间不存在
							messageInfo = commonBiz
									.setMessageInfo(
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
					CommonData.putPlayerIdToPlayer(playerId, player);
				}
			}
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
			List<Player> players = new ArrayList<Player>();
			players.add(player);
			room.setPlayers(players);
			room.setRemainderGames(games);
			room.setType(type);
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
		Integer playerId = req.getPlayerId();
		List<Player> players;
		try {
			players = CommonData.getPlayersByRoomId(roomId);
		} catch (BusinnessException e) {
			if (ExcMsgConstants.NO_EXISTS_THE_ROOM_EXC_CODE.equals(e.getCode())) {// 该房间不存在
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
			if (player.getIsland() && !player.getOnPlay()) {// 该玩家登陆，并且不在玩牌状态，才可以进入房间
				player.setRoomId(roomId);
				player.setOnPlay(true);
				player.setOrder(players.size() + 1);// 玩家按先后顺序第N位进入房间，方便前端安排座位顺序
				CommonData.putPlayerIdToPlayer(playerId, player);
				players.add(player);
				RoomInfo.Builder roomInfo = RoomInfo.newBuilder();
				roomInfo.setRoomId(roomId);
				roomInfo.addAllPlayers(BeanCopy.playersCopy(players));
				entryRoomResp.setRoomInfo(roomInfo);
				entryRoomResp.setOrder(players.size());// 玩家按先后顺序第N位进入房间，方便前端安排座位顺序
				// 广播房间里其他玩家
				for (Player py : players) {
					if (py.getId().equals(playerId))// 自己不用通知
						continue;
					py.getChannel().writeAndFlush(setPostEntryRoom(player));
				}
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
}
