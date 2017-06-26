package com.rxqp.server.bussiness.biz.impl;

import com.rxqp.common.constants.CommonConstants;
import com.rxqp.common.constants.MessageConstants;
import com.rxqp.common.constants.WeixinConstants;
import com.rxqp.common.data.CommonData;
import com.rxqp.model.AccessTokenOpenId;
import com.rxqp.model.WeixinUserInfo;
import com.rxqp.protobuf.DdzProto;
import com.rxqp.protobuf.DdzProto.*;
import com.rxqp.server.bo.Player;
import com.rxqp.server.bo.Room;
import com.rxqp.server.bussiness.biz.ICommonBiz;
import com.rxqp.server.bussiness.biz.ILoginBiz;
import com.rxqp.utils.CommonUtils;
import com.rxqp.utils.WeixinUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class LoginBizImpl implements ILoginBiz {

	private static Integer id = 1;

	private ICommonBiz commonBiz = new CommonBiz();

	@Override
	public Player authenticate() {
		Player player = new Player();
		player.setId(id);
		player.setName("用户" + id);
		player.setIsland(true);
		id++;
		return player;
	}

	@Override
	public MessageInfo.Builder login(MessageInfo messageInfoReq,
			ChannelHandlerContext ctx) {
		LoginReq loginReq = messageInfoReq.getLoginReq();
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		try{
			String code = loginReq.getCode();
			PlayerBaseInfo playerBaseInfo;
			Integer playerId;
			if(StringUtils.isNotBlank(code)){
				playerBaseInfo = getUserInfoByToken(code);
			}else{
				playerId = loginReq.getPlayerid();
				playerBaseInfo = getUserInfoByPlayerId(playerId);
			}

			if (playerBaseInfo==null){
				MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_STATE_TYPE_1006,
						MessageConstants.PLAYER_STATE_MSG_1006);
				return msgInfo;
			}


			playerId = playerBaseInfo.getID();
			Player player = CommonData.getPlayerById(playerId);
			if (player != null && player.getOnline()) {
				MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
				msgInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_STATE_TYPE_1004,
						MessageConstants.PLAYER_STATE_MSG_1004);
				return msgInfo;
			} else if(player != null && !player.getOnline()){//之前掉线，或者异常退出，现在再次登录
				player.setOnline(true);//玩家重新登录
//				player.setOnPlay(true);//玩家游戏中
			}else{
				player = new Player();
			}
			String name = playerBaseInfo.getName();

			//
			player.setId(playerId);
			player.setName(name);
			player.setChannel(ctx.channel());
			player.setOnline(true);
			player.setImgUrl(playerBaseInfo.getImgUrl());
			player.setIsland(true);
			player.setToken(playerBaseInfo.getToken());
			CommonData.putPlayerIdToPlayer(playerId, player);
			CommonData.putChannelIdToPlayerId(player.getChannel().hashCode(),playerId);
			//
			boolean isSuccess = true;// 登录成功
			if (isSuccess) {
				DdzProto.LoginResp.Builder loginResp = DdzProto.LoginResp
						.newBuilder();
				messageInfo.setMessageId(MESSAGE_ID.msg_LoginResp);
				loginResp.setPlayerBaseInfo(playerBaseInfo);
				if(player.getOnPlay()) {
					loginResp.setPlayerState(1);//斗牛在线状态
				}else{
					loginResp.setPlayerState(0);//正常状态
				}
				if(player.getRoomId()!=null)
					loginResp.setRoomId(player.getRoomId());
				loginResp.setShareurl(CommonConstants.SHARE_URL);
				messageInfo.setLoginResp(loginResp);
			} else {
				messageInfo.setMessageId(MESSAGE_ID.msg_MsgInfo);
				MsgInfo.Builder msgInfo = MsgInfo.newBuilder();
				String error = MessageConstants.LOGIN_ERROR_MSG_1003;// 错误信息
				msgInfo.setType(MessageConstants.LOGIN_ERROR_TYPE_1003);
				msgInfo.setMessage(error);
				messageInfo.setMsgInfo(msgInfo);
			}
		}catch (Exception e){
			System.out.println("~~~~~~~~~"+e);
		}
		return messageInfo;
	}

	@Override
	public MessageInfo.Builder reLogin(MessageInfo messageInfoReq,
									 ChannelHandlerContext ctx) {
		ReLoginReq reLoginReq = messageInfoReq.getReLoginReq();
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		messageInfo.setMessageId(MESSAGE_ID.msg_LoginResp);
		String token = reLoginReq.getToken();
		Integer playerId = reLoginReq.getPlayerId();
		Player player = CommonData.getPlayerById(playerId);
		if (player != null) {
			if (playerId.equals(player.getId()) && token.equals(player.getToken())) {
				LoginResp.Builder loginResp = LoginResp.newBuilder();
				PlayerBaseInfo playerBaseInfo = getUserInfoByPlayerId(playerId);
				playerBaseInfo.toBuilder().setToken(player.getToken());
				loginResp.setPlayerBaseInfo(playerBaseInfo);
				loginResp.setShareurl(CommonConstants.SHARE_URL);
				if (player.getOnPlay()) {
					loginResp.setPlayerState(1);//斗牛在线状态
				} else {
					loginResp.setPlayerState(0);//正常状态
				}
				if (player.getRoomId() != null) {
					loginResp.setRoomId(player.getRoomId());
				}
				messageInfo.setLoginResp(loginResp);
			} else {
				messageInfo = commonBiz.setMessageInfo(
						MessageConstants.PLAYER_STATE_TYPE_1007,
						MessageConstants.PLAYER_STATE_MSG_1007);
			}
		} else {
			messageInfo = commonBiz.setMessageInfo(
					MessageConstants.PLAYER_STATE_TYPE_1006,
					MessageConstants.PLAYER_STATE_MSG_1006);
		}

		return messageInfo;
	}

	/**
	 * 玩家异常退出游戏
	 * @param channel
	 */
	public void deletPlayerByChannelId(Channel channel){
		if (channel != null){
			Integer playerId = CommonData.getPlayerIdByChannelId(channel.hashCode());
			if (playerId!=null){
//				CommonData.removePlayer(playerId);
				Player player = CommonData.getPlayerById(playerId);
				if(player!=null){
					player.setOnline(false);//用户掉线
					if(player.getRoomId()==null){
						return;
					}
					Room room = CommonData.getRoomByRoomId(player.getRoomId());
					if (room!=null){
						List<Player> pls = room.getPlayers();
						int isNotOnlineCnt = 0;//该房间不在线人数
						if(CollectionUtils.isNotEmpty(pls)){
							Iterator<Player> itr = pls.iterator();
							while (itr.hasNext()){
								Player pl = itr.next();
								if(!pl.getOnline()){
									isNotOnlineCnt++;
								}else{//给在线玩家发送离线通知
									pl.getChannel().writeAndFlush(getPostOffMessage(player));
								}
							}
							if(isNotOnlineCnt == 1){//该房间第一位玩家退出房间时间
								room.setFirtPlayerOffLineTime(new Date());
							}
						}
					}
				}
//				CommonData.removeChannelId(channel.hashCode());
			}
		}
	}

	private MessageInfo getPostOffMessage(Player player){
		MessageInfo.Builder postMsgInfo = MessageInfo.newBuilder();
		postMsgInfo.setMessageId(MESSAGE_ID.msg_PostPlayerOffline);
		PostPlayerOffline.Builder postPlayerOffline = PostPlayerOffline
				.newBuilder();
		postPlayerOffline.setPlayerId(player.getId());
		postMsgInfo.setPostPlayerOffline(postPlayerOffline);
		return postMsgInfo.build();
	}

	/**
	 * 玩家正常退出游戏
	 * @param messageInfoReq
	 * @param ctx
	 * @return
	 */
	@Override
	public MessageInfo.Builder deletPlayerByPlayerid(MessageInfo messageInfoReq, ChannelHandlerContext ctx) {
		SignOutReq signOutReq = messageInfoReq.getSignOutReq();
		Integer playerId = signOutReq.getPlayerid();
		if (playerId!=null){
			CommonData.removePlayer(playerId);
			Player player = CommonData.getPlayerById(playerId);
			if(player!=null){
				Channel channel = player.getChannel();
				Room room = CommonData.getRoomByRoomId(player.getRoomId());
				if (room!=null){
					List<Player> pls = room.getPlayers();
					Iterator<Player> itr = pls.iterator();
					while (itr.hasNext()){
						Player pl = itr.next();
						if(pl.getId().equals(playerId)){
							itr.remove();
						}
					}
				}
				if (channel!=null)
					CommonData.removeChannelId(channel.hashCode());
			}
		}
		return null;
	}

	/**
	 * 根据playerId获取用户微信基本信息
	 * @param playerId
	 * @return
	 */
	private PlayerBaseInfo getUserInfoByPlayerId(Integer playerId){
		PlayerBaseInfo.Builder player = PlayerBaseInfo.newBuilder();

		String url = WeixinConstants.getPlayerByPlayeridUrl;
		url = url.replace("PLAYERID", playerId.toString());
		JSONObject obj = CommonUtils.sendGet(url);
		if (obj != null){
			player.setID(obj.getInt("id"));
			player.setName(obj.getString("name"));
			player.setImgUrl(obj.getString("imgUrl"));
			player.setToken(CommonUtils.getAccessToken(playerId));//生成登录token
//			player.setCardNum(obj.getInt("cardNum"));
		}else{
			return null;
		}
		return player.build();
	}

	/**
	 * 根据token获取用户微信基本信息
	 * @param code
	 * @return
	 */
	private PlayerBaseInfo getUserInfoByToken(String code){
		PlayerBaseInfo.Builder player = PlayerBaseInfo.newBuilder();
		AccessTokenOpenId accessTokenOpenId = getAccessTokenOpenId(code);
		if (accessTokenOpenId!=null){
			String openid = accessTokenOpenId.getOpenid();
			String access_token = accessTokenOpenId.getAccess_token();
			WeixinUserInfo weixinUserInfo = getWeixinUserInfo(access_token,openid);
			Player pl = getPlayerByOpenid(weixinUserInfo);
			if (pl==null)
				return null;
			player.setID(pl.getId());
			player.setName(weixinUserInfo.getName());
			player.setImgUrl(weixinUserInfo.getHeadImgUrl());
			player.setToken(CommonUtils.getAccessToken(pl.getId()));//生成登录token
		}else{
			return null;
		}

		return player.build();
	}

	private Player getPlayerByOpenid(WeixinUserInfo weixinUserInfo){
		String openid = weixinUserInfo.getOpneid();
		String url = WeixinConstants.getPlayerByOpenidUrl;
		url = url.replace("OPENID", openid);
		JSONObject obj = CommonUtils.sendGet(url);
		Player player = new Player();
		if (obj == null){
			String url1 = WeixinConstants.addUserWithOpenid;
			url1 = url1.replace("OPENID", openid);
			url1 = url1.replace("NAME", weixinUserInfo.getName());
			url1 = url1.replace("IMG_URL", weixinUserInfo.getHeadImgUrl());
			JSONObject obj1 = CommonUtils.sendGet(url1);
			if(obj1==null)
				return null;
			player.setId(obj1.getInt("id"));
			return player;
		}else{
			player.setId(obj.getInt("id"));
			return player;
		}
	}

	private WeixinUserInfo getWeixinUserInfo(String access_token,String openid){
		String url = WeixinConstants.getWXUserInfoUrl;
		url = url.replace("ACCESS_TOKEN", access_token);
		url = url.replace("OPENID", openid);
		JSONObject obj = WeixinUtil.httpRequest(url, "POST", null);
		if (obj != null && obj.containsKey("errcode"))
			return null;
		WeixinUserInfo weixinUserInfo = new WeixinUserInfo();
		weixinUserInfo.setCity(obj.getString("city"));
		weixinUserInfo.setCountry(obj.getString("country"));
		weixinUserInfo.setHeadImgUrl(obj.getString("headimgurl"));
		weixinUserInfo.setName(obj.getString("nickname"));
		weixinUserInfo.setOpneid(obj.getString("openid"));
		weixinUserInfo.setProvince(obj.getString("province"));
		weixinUserInfo.setSex(obj.getInt("sex"));
//		weixinUserInfo.setPrivilege(obj.getJSONArray("privilege"));
		return weixinUserInfo;
	}

	private AccessTokenOpenId getAccessTokenOpenId(String code) {
		String url = WeixinConstants.getOpenidAndAccessCode;
		url = url.replace("APPID", CommonUtils.urlEnodeUTF8(WeixinConstants.appId));
		url = url.replace("SECRET",
				CommonUtils.urlEnodeUTF8(WeixinConstants.appSecret));
		url = url.replace("CODE", CommonUtils.urlEnodeUTF8(code));
		JSONObject obj = WeixinUtil.httpRequest(url, "POST", null);
		if (obj != null && obj.containsKey("errcode"))
			return null;
		else{
			AccessTokenOpenId accessTokenOpenId = new AccessTokenOpenId();
			String openid = obj.getString("openid");
			String access_token = obj.getString("access_token");
			String refresh_token = obj.getString("refresh_token");//用户刷新access_token
			Long expires_in = obj.getLong("expires_in");//access_token接口调用凭证超时时间，单位（秒）
			String scope = obj.getString("scope");//用户授权的作用域，使用逗号（,）分隔
			String unionid = obj.getString("unionid");// 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
			accessTokenOpenId.setOpenid(openid);
			accessTokenOpenId.setAccess_token(access_token);
			accessTokenOpenId.setRefresh_token(refresh_token);
			accessTokenOpenId.setExpires_in(expires_in);
			accessTokenOpenId.setScope(scope);
			accessTokenOpenId.setUnionid(unionid);
			return accessTokenOpenId;
		}
	}

}
