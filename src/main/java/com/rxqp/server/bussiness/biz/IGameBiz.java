package com.rxqp.server.bussiness.biz;

import com.rxqp.protobuf.DdzProto.MessageInfo;
import com.rxqp.protobuf.DdzProto.MessageInfo.Builder;
import com.rxqp.server.bo.Room;
import io.netty.channel.ChannelHandlerContext;

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

	/**
	 * 发送语音
	 */
	public Builder sendSoundProccess(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 扣减房卡
	 * @param room
	 */
	public void deductionRoomCards(Room room);
}
