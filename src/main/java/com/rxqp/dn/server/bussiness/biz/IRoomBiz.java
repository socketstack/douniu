package com.rxqp.dn.server.bussiness.biz;

import io.netty.channel.ChannelHandlerContext;

import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo.Builder;
import com.rxqp.dn.server.bo.Room;

public interface IRoomBiz {

	/**
	 * 创建房间
	 * 
	 * @param messageInfoReq
	 * @return
	 */
	public MessageInfo.Builder createNewRoom(MessageInfo messageInfoReq);

	/**
	 * 
	 * @param messageInfoReq
	 * @return
	 */
	public MessageInfo.Builder entryRoom(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 删除房间信息
	 * 
	 * @param roomId
	 * @return
	 */
	public Boolean removeRoom(Integer roomId);

	/**
	 * 解散房间请求
	 * 
	 * @param messageInfoReq
	 * @param ctx
	 * @return
	 */
	public MessageInfo.Builder disolutionRoom(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 玩家回复解散房间
	 * 
	 * @param messageInfoReq
	 * @param ctx
	 * @return
	 */
	public MessageInfo.Builder answerDissolution(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx);

	/**
	 * 房间大结算
	 * 
	 * @param room
	 * @return
	 */
	public Builder buildSettlementData(Room room);

}
