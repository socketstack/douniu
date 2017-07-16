package com.rxqp.server.netty.handler;

import com.rxqp.Task.CheckPlayersTask;
import com.rxqp.Task.CleanRoomTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

public class OutHandler extends ChannelOutboundHandlerAdapter {

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
			SocketAddress localAddress, ChannelPromise promise)
			throws Exception {
		// TODO Auto-generated method stub
		super.connect(ctx, remoteAddress, localAddress, promise);
		System.out
				.println("<<<<<<<<<<<<<<< connect server success >>>>>>>>>>>>>>>>");
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.bind(ctx, localAddress, promise);
		CleanRoomTask task = new CleanRoomTask();
		task.cleanRoomTask(task);
		CheckPlayersTask task1 = new CheckPlayersTask();
		task1.checkPlayers(task1);
		System.out
				.println("<<<<<<<<<<<<<<< server bind success >>>>>>>>>>>>>>>>");
	}
}
