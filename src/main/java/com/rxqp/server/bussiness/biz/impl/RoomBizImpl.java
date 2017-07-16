package com.rxqp.server.bussiness.biz.impl;

import com.rxqp.common.constants.ExcMsgConstants;
import com.rxqp.common.constants.MessageConstants;
import com.rxqp.common.constants.WeixinConstants;
import com.rxqp.common.data.CommonData;
import com.rxqp.common.exception.BusinnessException;
import com.rxqp.protobuf.DdzProto;
import com.rxqp.protobuf.DdzProto.*;
import com.rxqp.protobuf.DdzProto.MessageInfo.Builder;
import com.rxqp.server.bo.Player;
import com.rxqp.server.bo.Room;
import com.rxqp.server.bussiness.biz.ICommonBiz;
import com.rxqp.server.bussiness.biz.IRoomBiz;
import com.rxqp.utils.BeanCopy;
import com.rxqp.utils.CommonUtils;
import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.rxqp.protobuf.DdzProto.BankerType.*;
import static com.rxqp.protobuf.DdzProto.NNStatus.*;
import static com.rxqp.protobuf.DdzProto.NNType.NNT_ERROR;
import static com.rxqp.protobuf.DdzProto.NNType.NNT_NONE;

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
			Integer type = creatRoomReq.getType();// type:1：房主支付	2：AA支付	3：赢家支付
			Integer playerId = creatRoomReq.getPlayerId();
			BankerType bankerType = creatRoomReq.getBankerType();
			if (bankerType == BT_NONE){
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_STATE_TYPE_1008,
						MessageConstants.PLAYER_STATE_MSG_1008);
				return messageInfo;
			}
			messageInfo.setMessageId(MESSAGE_ID.msg_CreateNNRoomResp);

			Integer roomId = -1;
			Player player = CommonData.getPlayerById(playerId);
			if (player ==null || !player.getIsland()) {// 该玩家未登陆
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
				roomId = createNewRoom(games, type, playerId, bankerType);
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
			player.setNnStatus(STATUS_CREATE_ROOM);//玩家当前所处状态
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

	private Integer createNewRoom(Integer games, Integer type, Integer playerId,BankerType bankerType)
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
			room.setBankerType(bankerType);//庄类型

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
		} else if (players.size() > 4) {// 斗牛每一房间最多5个玩家
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
			player.setNextPlayerId(players.get(0).getId());// 其下一位玩家是第一位玩家
			if (!player.getIsland()) {// 该玩家未登陆
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
						MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
				return messageInfo;
			}
			player.setRoomId(roomId);
			player.setOnPlay(true);
			player.setOrder(players.size() + 1);// 玩家按先后顺序第N位进入房间，方便前端安排座位顺序
			player.setNnStatus(STATUS_ENTER_ROOM);//玩家处于进入房间状态
			CommonData.putPlayerIdToPlayer(playerId, player);
			players.add(player);
			RoomInfo.Builder roomInfo = RoomInfo.newBuilder();
			roomInfo.setRoomId(roomId);
			roomInfo.addAllPlayers(BeanCopy.playersCopy(players));
			roomInfo.setTotalGames(room.getTotalGames());
			roomInfo.setIsDisband(room.getDisband());//该房间是否处于解散房间状态
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
				player.setPokerIds(null);
				player.setRoomId(-1);
				player.setScore(0);
				player.setFinalScore(0);
				player.setBetPoints(0);
				player.setNntype(null);
				player.setIsBanker(false);
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
		try {
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
//				messageInfo = commonBiz.setMessageInfo(
//						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
//						MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
//				return messageInfo;
				messageInfo.setMessageId(MESSAGE_ID.msg_PostDissolutionResult);
				PostDissolutionResult.Builder postDissolutionResult = PostDissolutionResult
						.newBuilder();
				messageInfo.setPostDissolutionResult(postDissolutionResult);
				return messageInfo;
			}
			room.getAgreeDissolutionIds().add(playerId);
			room.setDisband(true);//该房间当前处于请求解散房间状态
			room.setStartDisbandTime((int)(System.currentTimeMillis() / 1000));//单位是S
			List<Player> players = room.getPlayers();
			// 广播其他玩家有玩家请求解散房间
			if (CollectionUtils.isNotEmpty(players)) {
				if (players.size() == 1) {// 说明只有房主一个人，则直接解散房间成功
					messageInfo.setMessageId(MESSAGE_ID.msg_PostDissolutionResult);
					PostDissolutionResult.Builder postDissolutionResult = PostDissolutionResult
							.newBuilder();
					messageInfo.setPostDissolutionResult(postDissolutionResult);
					removeRoom(room.getRoomId());// 删除该房间信息
					return messageInfo;
				}
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
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
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
//			room.increaseAgreeDissolutionCnt();
			room.getAgreeDissolutionIds().add(playerId);
		} else {
//			room.increaseDisAgreeDissolutionCnt();
			room.getDisAgreeDissulutionIds().add(playerId);
		}

		List<Player> players = room.getPlayers();
		// 广播其所有玩家，目前多少玩家同意解散房间，多少玩家不同意
		if (CollectionUtils.isNotEmpty(players)) {
			MessageInfo.Builder postMsgInfo = MessageInfo.newBuilder();
			postMsgInfo
					.setMessageId(MESSAGE_ID.msg_PostAnswerDissolutionResult);
			PostAnswerDissolutionResult.Builder postAnswerDissolutionResult = PostAnswerDissolutionResult
					.newBuilder();
			postAnswerDissolutionResult.setAgreeCnt(room.getAgreeDissolutionIds().size());
			postAnswerDissolutionResult.setDisagreeCnt(room.getDisAgreeDissulutionIds().size());
			postMsgInfo
					.setPostAnswerDissolutionResult(postAnswerDissolutionResult);
			for (Player pl : players) {
				pl.getChannel().writeAndFlush(postMsgInfo.build());
			}
		}

		Integer playerCnt = room.getPlayers().size();
		Boolean isSuccess = false;
		if (playerCnt == 2 || playerCnt == 3) {
			if (room.getAgreeDissolutionIds().size() >= 2) {
				isSuccess = true;
			}
			if (room.getDisAgreeDissulutionIds().size()>=2){
				room.setDisband(false);//解散房间失败
				room.setStartDisbandTime(0);
				room.getDisAgreeDissulutionIds().clear();
				room.getAgreeDissolutionIds().clear();
			}
		} else if (playerCnt == 4 || playerCnt == 5) {
			if (room.getAgreeDissolutionIds().size() >= 3) {
				isSuccess = true;
			}
			if (room.getDisAgreeDissulutionIds().size() >= 3){
				room.setDisband(false);//解散房间失败
				room.setStartDisbandTime(0);
				room.getDisAgreeDissulutionIds().clear();
				room.getAgreeDissolutionIds().clear();
			}
		}
		if (isSuccess) {
			// 广播其他玩家有玩家请求解散房间
			if (CollectionUtils.isNotEmpty(players)) {
				try {
					deductionRoomCards(room);//扣减房卡
					MessageInfo.Builder postMsgInfo = MessageInfo.newBuilder();
					postMsgInfo.setMessageId(MESSAGE_ID.msg_PostDissolutionResult);
					PostDissolutionResult.Builder postDissolutionResult = PostDissolutionResult
							.newBuilder();
					postMsgInfo.setPostDissolutionResult(postDissolutionResult);
					MessageInfo.Builder mi = dissolutionBuildSettementData(room);
					for (Player pl : players) {
						pl.getChannel().writeAndFlush(postMsgInfo.build());// 广播解散房间成功
						pl.getChannel().writeAndFlush(mi.build());// 广播计算结算信息
					}
				}catch (Exception e){
					e.printStackTrace();
				}finally {
					removeRoom(room.getRoomId());// 删除该房间信息
				}
			}
		}
		return null;
	}

	public void deductionRoomCards(Room room){
		int games = room.getTotalGames();
		int type = room.getType();
		try{
			if (type == 3){//赢家支付
				int cards = games / 10;//每张房卡10局
				String deductionRoomCardsUrl = WeixinConstants.deductionRoomCardsUrl;
				deductionRoomCardsUrl = deductionRoomCardsUrl.replace("USERID",""+room.getWinPlayerId());
				Player player = CommonData.getPlayerById(room.getWinPlayerId());
				player.setCardNum(player.getCardNum()-cards*4);
				deductionRoomCardsUrl = deductionRoomCardsUrl.replace("CARDS",""+cards*4);
				JSONObject obj1 = CommonUtils.sendGet(deductionRoomCardsUrl);
				String isSuccess = obj1.getString("success");
				System.out.println("deductionRoomCards isSuccess:"+isSuccess);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 解散房间的时候结算信息
	 * 
	 * @param room
	 * @return
	 */
	private Builder dissolutionBuildSettementData(Room room) {
		MessageInfo.Builder mi = MessageInfo.newBuilder();
		mi.setMessageId(MESSAGE_ID.msg_SettlementInfo);
		SettlementInfo.Builder settlementInfo = SettlementInfo.newBuilder();
		settlementInfo.setIsOver(true);
		List<Player> players = room.getPlayers();
		for (Player player : players) {
			SettlementData.Builder playerSettlement = SettlementData
					.newBuilder();
			playerSettlement.setID(player.getId());
			playerSettlement.setGotscore(player.getScore());
			playerSettlement.setFinalscore(player.getFinalScore());
			playerSettlement.setLeaveCardNum(player.getCardNum());
			if (player.getFinalScore() > 0) {
				playerSettlement.setIsWin(true);
			} else {
				playerSettlement.setIsWin(false);
			}
			settlementInfo.addPlayers(playerSettlement);
		}
		mi.setSettlementInfo(settlementInfo);
		return mi;
	}

	/**
	 * 正常情况下，结算信息
	 * 
	 * @param room
	 * @return
	 */
	public Builder buildSettlementData(Room room) {
		MessageInfo.Builder mi = MessageInfo.newBuilder();
		mi.setMessageId(MESSAGE_ID.msg_SettlementInfo);
		SettlementInfo.Builder settlementInfo = SettlementInfo.newBuilder();
		if (room.getPlayedGames() > room.getTotalGames())
			settlementInfo.setIsOver(true);
		else
			settlementInfo.setIsOver(false);
		List<Player> players = room.getPlayers();
		Player banker = CommonData.getPlayerById(room.getBankerId());// 庄家
		if (banker.getNntype() != null
				&& banker.getNntype().equals(NNT_ERROR)) {// 庄家牌类型有误
			mi = commonBiz.setMessageInfo(
					MessageConstants.BANKER_CARDS_ERROR_TYPE,
					MessageConstants.BANKER_CARDS_ERROR_MSG);
			return mi;
		}
		SettlementData.Builder bankerSettlement = SettlementData.newBuilder();// 庄家的结算信息
		bankerSettlement.setID(banker.getId());
		bankerSettlement.setLeaveCardNum(banker.getCardNum());
		bankerSettlement.setGotscore(0);
		for (Player player : players) {
			SettlementData.Builder playerSettlement = SettlementData
					.newBuilder();// 非庄家玩家各自的结算信息
			playerSettlement.setID(player.getId());
			player.setNnStatus(STATUS_PREPARE_NEXT);
			if (!player.getIsBanker()) {
				Integer score = comparePoints(bankerSettlement, banker, player);
				playerSettlement.setGotscore(score);
				playerSettlement.setLeaveCardNum(player.getCardNum());
				Integer playerFinalScore = player.getFinalScore() + score;
				playerSettlement.setFinalscore(playerFinalScore);
				player.setFinalScore(playerFinalScore);
				player.setScore(score);
				if (score > 0) {
					playerSettlement.setIsWin(true);
					room.setWinPlayerId(player.getId());
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
			room.setWinPlayerId(banker.getId());
		} else {
			bankerSettlement.setIsWin(false);
		}
		settlementInfo.addPlayers(bankerSettlement);
		mi.setSettlementInfo(settlementInfo);
		return mi;
	}

	@Override
	public Builder reEntryRoom(MessageInfo messageInfoReq, ChannelHandlerContext ctx) {
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		messageInfo.setMessageId(MESSAGE_ID.msg_EntryNNRoomResp);

		EntryNNRoomResp.Builder entryRoomResp = EntryNNRoomResp.newBuilder();

		ReEntryNNRoomReq req = messageInfoReq.getReEntryNNRoomReq();
		Integer roomId = req.getRoomId();
		Room room = CommonData.getRoomByRoomId(roomId);
		if (room == null) {
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.THE_ROOM_NO_EXTIST_ERROR_TYPE,
					MessageConstants.THE_ROOM_NO_EXTIST_ERROR_MSG);
			return messageInfo;
		}
		Integer playerId = req.getPlayerId();
		List<Player> players;
		players = room.getPlayers();
		if (CollectionUtils.isEmpty(players)){
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.THE_ROOM_NO_EXTIST_PLAYER_TYPE,
					MessageConstants.THE_ROOM_NO_EXTIST_PLAYER_MSG);
			return messageInfo;
		}
		boolean isInTheRoom = false;
		int onLineCnt = 0;//该房间在线人数
		for (Player pl:players){
			if(pl.getId().equals(playerId)){
				isInTheRoom = true;//判断该玩家原来是否在该房间
			}
			if (pl.getOnline()){
				onLineCnt++;
			}
		}
		if (onLineCnt == players.size()){//如果所有玩家又重新就位，则第一位玩家离线时间重置，否则过二十分钟，该房间自动解散
			room.setFirtPlayerOffLineTime(null);
		}
		if(!isInTheRoom){
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.ENTRY_ROOM_ERROR_TYPE_4004,
					MessageConstants.ENTRY_ROOM_ERROR_MSG_4004);
			return messageInfo;
		}

		Player player = CommonData.getPlayerById(playerId);
		if (player == null || !player.getIsland()) {
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
					MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
			return messageInfo;
		}

		if (!player.getIsland()) {// 该玩家未登陆
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.PLAYER_NO_LOGIN_TYPE_1001,
					MessageConstants.PLAYER_NO_LOGIN_MSG_1001);
			return messageInfo;
		}
		player.setRoomId(roomId);
		player.setOnPlay(true);//游戏中
		player.setOnline(true);//在线
		CommonData.putPlayerIdToPlayer(playerId, player);
		RoomInfo.Builder roomInfo = RoomInfo.newBuilder();
		roomInfo.setRoomId(roomId);
		roomInfo.addAllPlayers(BeanCopy.playersCopy(players));
		roomInfo.setTotalGames(room.getTotalGames());
		roomInfo.setPlayedGames(room.getPlayedGames());
		roomInfo.setMultiple(room.getMultiple());
		roomInfo.setBankerId(room.getBankerId());
		roomInfo.setIsDisband(room.getIsStartGame());//该房间是否处于请求解散房间状态
		roomInfo.addAllAgreePlayerIds(room.getAgreeDissolutionIds());//同意解散房间玩家ids
		roomInfo.addAllRefusePlayerIds(room.getDisAgreeDissulutionIds());//不同解散房间意玩家dis
		roomInfo.setStartDisbandTime(room.getStartDisbandTime());
		entryRoomResp.setRoomInfo(roomInfo);
		entryRoomResp.setOrder(player.getOrder());
		// 广播房间里其他玩家,离线玩家已经上线
		for (Player py : players) {
			if (py.getId().equals(playerId))// 自己不用通知
				continue;
			py.getChannel().writeAndFlush(setPostReEntryRoom(player));
		}

		messageInfo.setEntryNNRoomResp(entryRoomResp);
		return messageInfo;
	}

	/**
	 * 计算下一小局的庄家
	 * @param room
	 */
	@Override
	public void computeBanker(Room room) {
		if (room.getBankerType() == BT_NONE || room.getBankerType() == BT_BAWANG){
			return;
		}else if (room.getBankerType() == BT_LUNZHUANG){//轮庄
			Integer bankId = room.getBankerId();
			Player banker = CommonData.getPlayerById(bankId);
			NNType nntype = banker.getNntype();
			if (nntype == NNT_ERROR || nntype == NNT_NONE){//无牛下庄
				room.setBankerId(banker.getNextPlayerId());
				banker.setIsBanker(false);
				Player newBanker = CommonData.getPlayerById(banker.getNextPlayerId());
				newBanker.setIsBanker(true);
			}
		}else if(room.getBankerType() == BT_ZHUANZHUANG){//转庄
			Integer bankId = room.getBankerId();
			Player banker = CommonData.getPlayerById(bankId);
			NNType nntype = banker.getNntype();
			room.setBankerId(banker.getNextPlayerId());
			banker.setIsBanker(false);
			Player newBanker = CommonData.getPlayerById(banker.getNextPlayerId());
			newBanker.setIsBanker(true);
		}
	}

	//广播通知其他玩家，离线玩家已经上线
	private MessageInfo setPostReEntryRoom(Player player) {
		MessageInfo.Builder builder = MessageInfo.newBuilder();
		builder.setMessageId(MESSAGE_ID.msg_PostPlayerOnline);
		PostPlayerOnline.Builder postPlayerOnline = PostPlayerOnline.newBuilder();
		postPlayerOnline.setPlayerId(player.getId());
		builder.setPostPlayerOnline(postPlayerOnline.build());

		return builder.build();
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
		if (playerNntype.equals(NNT_ERROR))
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
