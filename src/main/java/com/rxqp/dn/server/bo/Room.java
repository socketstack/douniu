package com.rxqp.dn.server.bo;

import java.util.List;

public class Room {

	private Integer roomId;
	private List<Player> players;
	private Integer multiple = 1;// 倍率,1倍为底
	private Integer playedGames = 0;// 已经玩了几盘
	private Integer totalGames = 0;// 一共几盘
	private Integer currentPlayerId;// 当前出牌玩家
	private Integer type;// 1表示房主出房费，2表示进入房间者均摊房费
	private List<Integer> prePokerIds;// 当前牌局中当前一轮出牌中，前一个玩家出牌的ID集合
	private Integer prePlayerId = -1;// 上一个出牌的玩家ID
	private Boolean isStartGame = false;// 是否已经开始玩游戏
	private Integer preparedPlayerCnt = 0;// 准备就绪玩家人数
	private Integer stakedPlayerCnt = 0;// 已经下注的玩家人数
	private Integer bankerId;// 庄家ID
	private Integer showCardsPlayerCnt = 0;// 已经开牌人数

	public Room() {
	}

	public void init() {
		this.multiple = 1;
		this.playedGames = 0;
		this.totalGames = 0;
		this.prePlayerId = -1;
		this.isStartGame = false;
		this.preparedPlayerCnt = 0;
		this.stakedPlayerCnt = 0;
		this.showCardsPlayerCnt = 0;
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

	public Integer getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(Integer totalGames) {
		this.totalGames = totalGames;
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

	public Boolean getIsStartGame() {
		return isStartGame;
	}

	public void setIsStartGame(Boolean isStartGame) {
		this.isStartGame = isStartGame;
	}

	public Integer getPreparedPlayerCnt() {
		return preparedPlayerCnt;
	}

	public void setPreparedPlayerCnt(Integer preparedPlayerCnt) {
		this.preparedPlayerCnt = preparedPlayerCnt;
	}

	public synchronized void increasePreparedPlayerCnt() {
		preparedPlayerCnt++;
	}

	public Integer getBankerId() {
		return bankerId;
	}

	public void setBankerId(Integer bankerId) {
		this.bankerId = bankerId;
	}

	public Integer getShowCardsPlayerCnt() {
		return showCardsPlayerCnt;
	}

	public void setShowCardsPlayerCnt(Integer showCardsPlayerCnt) {
		this.showCardsPlayerCnt = showCardsPlayerCnt;
	}

	public synchronized void increaseShowCardsPlayerCnt() {
		showCardsPlayerCnt++;
	}

	public synchronized void increasePlayedGamesCnt() {
		playedGames++;
	}

	public Integer getStakedPlayerCnt() {
		return stakedPlayerCnt;
	}

	public void setStakedPlayerCnt(Integer stakedPlayerCnt) {
		this.stakedPlayerCnt = stakedPlayerCnt;
	}

	public synchronized void increaseStakedPlayerCnt() {
		stakedPlayerCnt++;
	}
}
