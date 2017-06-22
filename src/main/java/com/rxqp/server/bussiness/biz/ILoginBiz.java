package com.rxqp.server.bussiness.biz;

import com.rxqp.protobuf.DdzProto.MessageInfo;
import com.rxqp.server.bo.Player;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;

public interface ILoginBiz {

	/**
	 * 用户认证
	 * 
	 *            微信公众号的普通用户的一个唯一的标识
	 * @return
	 */
	public Player authenticate();

	public MessageInfo.Builder login(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	public void deletPlayerByChannelId(Channel channelId);

	public MessageInfo.Builder deletPlayerByPlayerid(MessageInfo messageInfoReq,
									  ChannelHandlerContext ctx);

	public MessageInfo.Builder reLogin(MessageInfo messageInfoReq,
									 ChannelHandlerContext ctx);
}
