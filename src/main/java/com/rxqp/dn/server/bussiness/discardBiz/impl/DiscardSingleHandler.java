package com.rxqp.dn.server.bussiness.discardBiz.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.rxqp.common.dn.constants.MessageConstants;
import com.rxqp.dn.common.enums.PokersTypeEnum;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.impl.CommonBiz;
import com.rxqp.dn.server.bussiness.discardBiz.IDiscardCommonService;
import com.rxqp.dn.server.bussiness.discardBiz.IDiscardHandler;

public class DiscardSingleHandler implements IDiscardHandler {

	private MessageInfo.Builder msgInfo;
	private Room room;// 当前房间
	private Player player;// 当前玩家
	private List<Integer> cardIds;// 当前出牌IDs
	private PokersTypeEnum cardType;// 当前出牌类型
	private List<Integer> preCardIds;// 前一家出牌的IDs
	private ICommonBiz commonBiz = new CommonBiz();
	private IDiscardCommonService discardCommonService = new DiscardCommonService();

	public DiscardSingleHandler(MessageInfo.Builder msgInfo, Room room,
			Player player, List<Integer> cardIds, PokersTypeEnum cardType,
			List<Integer> preCardIds) {
		this.msgInfo = msgInfo;
		this.room = room;
		this.player = player;
		this.cardIds = cardIds;
		this.cardType = cardType;
		this.preCardIds = preCardIds;
	}

	@Override
	public MessageInfo.Builder discard() {
		if (cardIds.size() != 1) {
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.DISCARD_CARDS_ISNOT_ONE_TYPE,
					MessageConstants.DISCARD_CARDS_ISNOT_ONE_MSG);
			return msgInfo;
		}
		if (CollectionUtils.isEmpty(preCardIds) || preCardIds.size() != 1) {
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.UNKNOWN_CAUSE_TYPE,
					MessageConstants.UNKNOWN_CAUSE_MSG);
			return msgInfo;
		}
		if (discardCommonService.singleCardCompare(cardIds.get(0),
				preCardIds.get(0)) == 1) {
			MessageInfo mi = discardCommonService
					.deleteDiscard(cardIds, player);
			if (mi != null) {
				return mi.toBuilder();
			}
			discardCommonService.postPlayersDiscards(room, cardType, cardIds,
					player, msgInfo);
		} else {
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.DISCARD_POINT_ISNOT_GREATER_TYPE,
					MessageConstants.DISCARD_POINT_ISNOT_GREATER_MSG);
			return msgInfo;
		}
		return msgInfo;
	}

}
