package com.rxqp.server.bo;

import com.rxqp.model.AccessTokenOpenId;
import com.rxqp.protobuf.DdzProto;
import com.rxqp.protobuf.DdzProto.NNType;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.net.Socket;
import java.util.List;

public class Player implements Serializable {

	private static final long serialVersionUID = 5732930012273200036L;

	private Integer id;
	private String name;
	private List<Integer> pokerIds;
	private Socket socket;
	private Boolean island = false;// 是否登录
	private Boolean onPlay = false;// 是否正在玩牌中
	private Boolean isOnline = false;//是否在线
	private Integer groupId;
	private Channel channel;
	private Integer roomId;
	private String imgUrl = "";
	private Integer score = 0;// 本局得分
	private Integer finalScore = 0;// 最终得分
	private Integer order;// 座位顺序
	private Integer nextPlayerId;// 按照座位顺序下家玩家ID
	private Integer betPoints;// 下注分数
	private Boolean isBanker = false;// 是否是庄家
	private NNType nntype;// 当前玩家扑克的类型
	private DdzProto.NNStatus nnStatus;//断线重连进入游戏后，状态 0;创建房间
													//1;进入房间 2;进入准备状态 3;准备完毕自动发牌 4;准备下注 5;准备开牌
	private String token;

	private AccessTokenOpenId accessTokenOpenId;//用户微信接口相关信息

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsland() {
		return island;
	}

	public void setIsland(Boolean island) {
		this.island = island;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Player(int id, String name, boolean island) {
		super();
		this.id = id;
		this.name = name;
		this.island = island;
	}

	public Player(int id, String name, Socket socket, Channel channel,
			boolean island) {
		super();
		this.id = id;
		this.name = name;
		this.socket = socket;
		this.island = island;
	}

	public Player() {
		super();
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", pokerIds=" + pokerIds
				+ ", island=" + island + "]";
	}

	public Boolean getOnPlay() {
		return onPlay;
	}

	public void setOnPlay(Boolean onPlay) {
		this.onPlay = onPlay;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Integer finalScore) {
		this.finalScore = finalScore;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getNextPlayerId() {
		return nextPlayerId;
	}

	public void setNextPlayerId(Integer nextPlayerId) {
		this.nextPlayerId = nextPlayerId;
	}

	public Integer getBetPoints() {
		return betPoints;
	}

	public List<Integer> getPokerIds() {
		return pokerIds;
	}

	public void setPokerIds(List<Integer> pokerIds) {
		this.pokerIds = pokerIds;
	}

	public void setBetPoints(Integer betPoints) {
		this.betPoints = betPoints;
	}

	public Boolean getIsBanker() {
		return isBanker;
	}

	public void setIsBanker(Boolean isBanker) {
		this.isBanker = isBanker;
	}

	public NNType getNntype() {
		return nntype;
	}

	public void setNntype(NNType nntype) {
		this.nntype = nntype;
	}

	public Boolean getOnline() {
		return isOnline;
	}

	public void setOnline(Boolean online) {
		isOnline = online;
	}

	public boolean equals(Player player) {
		if (this.id.equals(player.getId())
				&& this.name.equals(player.getName())
				&& this.groupId.equals(player.getGroupId())) {
			return true;
		} else {
			return false;
		}
	}

	public AccessTokenOpenId getAccessTokenOpenId() {
		return accessTokenOpenId;
	}

	public void setAccessTokenOpenId(AccessTokenOpenId accessTokenOpenId) {
		this.accessTokenOpenId = accessTokenOpenId;
	}

	public Boolean getBanker() {
		return isBanker;
	}

	public void setBanker(Boolean banker) {
		isBanker = banker;
	}

	public DdzProto.NNStatus getNnStatus() {
		return nnStatus;
	}

	public void setNnStatus(DdzProto.NNStatus nnStatus) {
		this.nnStatus = nnStatus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
