package com.rxqp.server.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Created by mengfanfei on 2017/6/7.
 */
public class MyOutboundHandler extends ChannelHandlerAdapter {
    public void close(ChannelHandlerContext ctx, ChannelPromise promise)
            throws Exception {
        System.out.println("close.....");
    }
}
