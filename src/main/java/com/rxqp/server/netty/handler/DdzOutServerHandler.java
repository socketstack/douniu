package com.rxqp.server.netty.handler;

import com.rxqp.utils.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import com.rxqp.protobuf.DdzProto;

public class DdzOutServerHandler extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		DdzProto.MessageInfo req = (DdzProto.MessageInfo) msg;
		System.out.println("-----"+ DateUtils.getCurrentTime() + "---resp:" + req.toString());
		// 执行下一个OutboundHandler
		super.write(ctx, msg, promise);
	}
}
