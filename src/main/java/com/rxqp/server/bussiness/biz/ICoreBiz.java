package com.rxqp.server.bussiness.biz;

import io.netty.channel.ChannelHandlerContext;

import com.rxqp.protobuf.DdzProto;

public interface ICoreBiz {

	public DdzProto.MessageInfo process(DdzProto.MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);
}
