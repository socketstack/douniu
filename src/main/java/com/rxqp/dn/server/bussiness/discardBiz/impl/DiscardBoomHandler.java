package com.rxqp.dn.server.bussiness.discardBiz.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.rxqp.common.dn.constants.MessageConstants;
import com.rxqp.dn.common.enums.PokersTypeEnum;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo.Builder;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;
import com.rxqp.dn.server.bussiness.biz.ICommonBiz;
import com.rxqp.dn.server.bussiness.biz.impl.CommonBiz;
import com.rxqp.dn.server.bussiness.discardBiz.IDiscardCommonService;
import com.rxqp.dn.server.bussiness.discardBiz.IDiscardHandler;

public class DiscardBoomHandler implements IDiscardHandler {

	private MessageInfo.Builder msgInfo;
	private Room room;// 当前房间
	private Player player;// 当前玩家
	private List<Integer> cardIds;// 当前出牌IDs
	private PokersTypeEnum cardType;// 当前出牌类型
	private List<Integer> preCardIds;// 前一家出牌的IDs
	private ICommonBiz commonBiz = new CommonBiz();
	private IDiscardCommonService discardCommonService = new DiscardCommonService();

	public DiscardBoomHandler(MessageInfo.Builder msgInfo, Room room,
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
	public Builder discard() {
		msgInfo = checkCards();
		if (msgInfo != null) {
			return msgInfo;
		}
		PokersTypeEnum preCardsType = room.getPrePokersType();
		if (preCardsType != PokersTypeEnum.Boom
				&& preCardsType != PokersTypeEnum.JokerBoom) {// 如果上一家出的牌不是炸弹
			MessageInfo mi = discardCommonService
					.deleteDiscard(cardIds, player);
			if (mi != null) {
				return mi.toBuilder();
			}
			discardCommonService.postPlayersDiscards(room, cardType, cardIds,
					player, msgInfo);
		} else if (preCardsType == PokersTypeEnum.JokerBoom) {
			msgInfo = commonBiz
					.setMessageInfo(
							MessageConstants.DISCARD_CARDS_BOOM_ISNOT_GREATER_PREPLAYER_TYPE,
							MessageConstants.DISCARD_CARDS_BOOM_ISNOT_GREATER_PREPLAYER_MSG);
			return msgInfo;
		} else {// 都是四张牌的炸弹
			if (discardCommonService.singleCardCompare(cardIds.get(0),
					preCardIds.get(0)) == 1) {
				MessageInfo mi = discardCommonService.deleteDiscard(cardIds,
						player);
				if (mi != null) {
					return mi.toBuilder();
				}
				discardCommonService.postPlayersDiscards(room, cardType,
						cardIds, player, msgInfo);
			} else {
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.DISCARD_POINT_ISNOT_GREATER_TYPE,
						MessageConstants.DISCARD_POINT_ISNOT_GREATER_MSG);
				return msgInfo;
			}
		}
		Integer variableCard = room.getVariablePoints();// 当前房间当前局的赖子点数
		if (cardIds.contains(variableCard)) {// 说明是软炸弹
			room.setMultiple(room.getMultiple() + 1);// 倍率+1
		} else {
			room.setMultiple(room.getMultiple() + 2);// 倍率+2
		}
		return null;
	}

	private Builder checkCards() {
		if (CollectionUtils.isEmpty(cardIds) || cardIds.size() != 4) {
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.DISCARD_CARDS_ISNOT_FOUR_BOOM_TYPE,
					MessageConstants.DISCARD_CARDS_ISNOT_FOUR_BOOM_MSG);
			return msgInfo;
		} else if (cardIds.contains(54)
				|| cardIds.contains(53)
				|| !(cardIds.get(0) % 13 == cardIds.get(1) % 13
						&& cardIds.get(0) % 13 == cardIds.get(2) % 13 && cardIds
						.get(0) % 13 == cardIds.get(3) % 13)) {
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.DISCARD_CARDS_ISNOT_FOUR_BOOM_TYPE,
					MessageConstants.DISCARD_CARDS_ISNOT_FOUR_BOOM_MSG);
			return msgInfo;
		}
		return null;
	}
}
