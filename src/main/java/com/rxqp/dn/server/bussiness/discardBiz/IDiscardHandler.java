package com.rxqp.dn.server.bussiness.discardBiz;

import com.rxqp.dn.protobuf.DdzProto.MessageInfo;

public interface IDiscardHandler {

	public MessageInfo.Builder discard();
}
