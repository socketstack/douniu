package com.rxqp.dn.server.bussiness.biz;

import io.netty.channel.ChannelHandlerContext;

import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.server.bo.Player;

public interface ILoginBiz {

	/**
	 * 用户认证
	 * 
	 * @param openId
	 *            微信公众号的普通用户的一个唯一的标识
	 * @return
	 */
	public Player authenticate();

	public MessageInfo.Builder login(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);
}
