package com.rxqp.dn.server.bussiness.biz.impl;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.stereotype.Service;

import com.rxqp.common.dn.constants.MessageConstants;
import com.rxqp.common.dn.data.CommonData;
import com.rxqp.dn.protobuf.DdzProto;
import com.rxqp.dn.protobuf.DdzProto.LoginReq;
import com.rxqp.dn.protobuf.DdzProto.MESSAGE_ID;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.protobuf.DdzProto.MsgInfo;
import com.rxqp.dn.protobuf.DdzProto.PlayerBaseInfo;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.ILoginBiz;

@Service
public class LoginBizImpl implements ILoginBiz {

	private static Integer id = 1;

	private ICommonBiz commonBiz = new CommonBiz();

	@Override
	public Player authenticate() {
		Player player = new Player();
		player.setId(id);
		player.setName("用户" + id);
		player.setIsland(true);
		id++;
		return player;
	}

	@Override
	public MessageInfo.Builder login(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		LoginReq loginReq = messageInfoReq.getLoginReq();
		PlayerBaseInfo playerBaseInfo = loginReq.getPlayerBaseInfo();
		Integer playerId = playerBaseInfo.getID();
		Player player = CommonData.getPlayerById(playerId);
		if (player != null) {
			MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.PLAYER_STATE_TYPE_1004,
					MessageConstants.PLAYER_STATE_MSG_1004);
			return msgInfo;
		} else {
			player = new Player();
		}
		String name = playerBaseInfo.getName();

		// TODO
		player.setId(playerId);
		player.setName(name);
		player.setChannel(ctx.channel());
		player.setIsland(true);
		CommonData.putPlayerIdToPlayer(playerId, player);
		// TODO
		boolean isSuccess = true;// 登录成功
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		if (isSuccess) {
			DdzProto.LoginResp.Builder loginResp = DdzProto.LoginResp
					.newBuilder();
			messageInfo.setMessageId(MESSAGE_ID.msg_LoginResp);
			messageInfo.setLoginResp(loginResp);
		} else {
			messageInfo.setMessageId(MESSAGE_ID.msg_MsgInfo);
			MsgInfo.Builder msgInfo = MsgInfo.newBuilder();
			String error = MessageConstants.LOGIN_ERROR_MSG_1003;// 错误信息
			msgInfo.setType(MessageConstants.LOGIN_ERROR_TYPE_1003);
			msgInfo.setMessage(error);
			messageInfo.setMsgInfo(msgInfo);
		}

		return messageInfo;
	}
}
