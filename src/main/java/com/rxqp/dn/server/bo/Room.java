package com.rxqp.dn.server.bo;

import java.util.List;

import com.rxqp.dn.common.enums.PokersTypeEnum;

public class Room {

	private Integer roomId;
	private List<Player> players;
	private Integer multiple = 1;// 倍率,1倍为底
	private Integer playedGames;// 一共玩几盘
	private Integer remainderGames;// 还剩几盘
	private Integer currentPlayerId;// 当前出牌玩家
	private Integer type;// 1表示房主出房费，2表示进入房间者均摊房费
	private PokersTypeEnum prePokersType;// 当前牌局中当前一轮出牌中，前一个玩家出牌的类型
	private List<Integer> prePokerIds;// 当前牌局中当前一轮出牌中，前一个玩家出牌的ID集合
	private Integer prePlayerId = -1;// 上一个出牌的玩家ID
	private Integer variablePoints;// 赖子点数

	public Room() {
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public Integer getPlayedGames() {
		return playedGames;
	}

	public void setPlayedGames(Integer playedGames) {
		this.playedGames = playedGames;
	}

	public Integer getRemainderGames() {
		return remainderGames;
	}

	public void setRemainderGames(Integer remainderGames) {
		this.remainderGames = remainderGames;
	}

	public Integer getCurrentPlayerId() {
		return currentPlayerId;
	}

	public void setCurrentPlayerId(Integer currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public PokersTypeEnum getPrePokersType() {
		return prePokersType;
	}

	public void setPrePokersType(PokersTypeEnum prePokersType) {
		this.prePokersType = prePokersType;
	}

	public List<Integer> getPrePokerIds() {
		return prePokerIds;
	}

	public void setPrePokerIds(List<Integer> prePokerIds) {
		this.prePokerIds = prePokerIds;
	}

	public Integer getPrePlayerId() {
		return prePlayerId;
	}

	public void setPrePlayerId(Integer prePlayerId) {
		this.prePlayerId = prePlayerId;
	}

	public Integer getVariablePoints() {
		return variablePoints;
	}

	public void setVariablePoints(Integer variablePoints) {
		this.variablePoints = variablePoints;
	}

}
