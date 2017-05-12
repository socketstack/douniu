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
	 * 开始游戏
	 * 
	 * @param messageInfoReq
	 * @param ctx
	 * @return
	 */
	public Builder startNNGame(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 下注请求
	 * 
	 * @param messageInfoReq
	 * @param ctx
	 * @return
	 */
	public Builder stakeProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 开牌请求
	 * 
	 * @param messageInfoReq
	 * @param ctx
	 * @return
	 */
	public Builder showCardsProcess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);
}
