package com.rxqp.dn.server.bussiness.biz;

import io.netty.channel.ChannelHandlerContext;

import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo.Builder;

public interface IGameBiz {

	/**
	 * 处理发牌
	 * 
	 * @param messageInfoReq
	 * @return
	 */
	public Builder dealProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 抢地主
	 * 
	 * @param messageInfoReq
	 * @return
	 */
	public MessageInfo.Builder grabHost(MessageInfo messageInfoReq);

	/**
	 * 处理出牌请求
	 * 
	 * @param messageInfoReq
	 * @return
	 */
	public Builder discardProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);
}
