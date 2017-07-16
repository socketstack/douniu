package com.rxqp.server.netty.handler;

import com.rxqp.protobuf.DdzProto;
import com.rxqp.server.bussiness.biz.ILoginBiz;
import com.rxqp.server.bussiness.biz.impl.CoreBizImpl;
import com.rxqp.server.bussiness.biz.impl.LoginBizImpl;
import com.rxqp.utils.DateUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DdzServerHandler extends ChannelInboundHandlerAdapter {

	private ILoginBiz loginBiz = new LoginBizImpl();

	private com.rxqp.server.bussiness.biz.ICoreBiz ICoreBiz = new CoreBizImpl();

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		DdzProto.MessageInfo req = (DdzProto.MessageInfo) msg;
		System.out.println("===" + DateUtils.getCurrentTime() + "===req:" + req.toString());
		try {
			DdzProto.MessageInfo mi = ICoreBiz.process(req, ctx);
			if (mi != null)
				ctx.writeAndFlush(mi);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("----- INACTIVE -----");
		Channel channel = ctx.channel();
		loginBiz.deletPlayerByChannelId(channel);
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
	}

	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println(ctx);
	}
}
