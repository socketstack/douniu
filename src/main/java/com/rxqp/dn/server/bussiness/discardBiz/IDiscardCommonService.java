package com.rxqp.dn.server.bussiness.discardBiz;

import java.util.List;

import com.rxqp.dn.common.enums.PokersTypeEnum;
import com.rxqp.dn.protobuf.DdzProto.MessageInfo;
import com.rxqp.dn.server.bo.Player;
import com.rxqp.dn.server.bo.Room;

public interface IDiscardCommonService {

	/**
	 * 单牌比较
	 * 
	 * @param cardId
	 * @param preCardId
	 * @return 1表示cardId>preCardId 0表示两者相等 -1表示cardId<preCardId
	 */
	public int singleCardCompare(int cardId, int preCardId);

	/**
	 * 删除出掉的牌
	 * 
	 * @param cardIds
	 * @param pokers
	 * @return
	 */
	public MessageInfo deleteDiscard(List<Integer> cardIds, Player player);

	/**
	 * 广播通知所有玩家出牌的情况
	 * 
	 * @param room
	 * @param cardType
	 * @param cardIds
	 * @param player
	 * @param msgInfo
	 */
	public void postPlayersDiscards(Room room, PokersTypeEnum cardType,
			List<Integer> cardIds, Player player, MessageInfo.Builder msgInfo);

	/**
	 * 广播通知结算
	 * 
	 * @return
	 */
	public void postPlayersSettlement(Player player);

}
