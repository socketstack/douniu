package com.rxqp.dn.server.bussiness.biz.impl;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.stereotype.Service;

import com.rxqp.dn.protobuf.DdzProto;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.server.bussiness.biz.ICoreBiz;
import com.rxqp.dn.server.bussiness.biz.IGameBiz;
import com.rxqp.dn.server.bussiness.biz.ILoginBiz;
import com.rxqp.dn.server.bussiness.biz.IRoomBiz;

@Service
public class CoreBizImpl implements ICoreBiz {

	private ILoginBiz loginBiz = new LoginBizImpl();
	private IGameBiz gameBiz = new GameBizImpl();
	private IRoomBiz roomBiz = new RoomBizImpl();

	@Override
	public MessageInfo process(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		DdzProto.MESSAGE_ID messageId = messageInfoReq.getMessageId();
		MessageInfo.Builder builder = MessageInfo.newBuilder();
		switch (messageId) {
		case msg_LoginReq:// 登录请求
			builder = loginBiz.login(messageInfoReq, ctx);
			break;
		case msg_CreateNNRoomReq:// 创建房间请求
			builder = roomBiz.createNewRoom(messageInfoReq);
			break;
		case msg_EntryNNRoomReq:// 进入房间请求
			builder = roomBiz.entryRoom(messageInfoReq, ctx);
			break;
		case msg_NNDealReq:// 发牌请求
			builder = gameBiz.dealProcess(messageInfoReq, ctx);
			break;
		case msg_DisbandReq:// 玩家请求解散房间
			// TODO
			break;
		case msg_DisbandCheckReq:// 其他玩家答复解散房间请求
			// TODO
			break;
		default:
			System.out.println("default");
			break;
		}
		if (builder != null)
			return builder.build();
		else
			return null;
	}
}
