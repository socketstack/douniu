package com.rxqp.server.bussiness.biz.impl;

import com.rxqp.common.constants.MessageConstants;
import com.rxqp.common.constants.WeixinConstants;
import com.rxqp.common.data.CommonData;
import com.rxqp.model.AccessTokenOpenId;
import com.rxqp.model.WeixinUserInfo;
import com.rxqp.protobuf.DdzProto;
import com.rxqp.protobuf.DdzProto.*;
import com.rxqp.server.bo.Player;
import com.rxqp.server.bussiness.biz.ICommonBiz;
import com.rxqp.server.bussiness.biz.ILoginBiz;
import com.rxqp.utils.CommonUtils;
import com.rxqp.utils.WeixinUtil;
import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.xml.registry.infomodel.User;

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
		String token = loginReq.getToken();
		PlayerBaseInfo playerBaseInfo = getUserInfoByToken(token);
		Integer playerId = playerBaseInfo.getID();
		Player player = CommonData.getPlayerById(playerId);
		if (player != null) {
			MessageInfo.Builder msgInfo = MessageInfo.newBuilder();
			msgInfo = commonBiz.setMessageInfo(
					MessageConstants.PLAYER_STATE_TYPE_1004,
					MessageConstants.PLAYER_STATE_MSG_1004);
			return msgInfo;
		} else {
			player = new Player();
		}
		String name = playerBaseInfo.getName();

		// TODO
		player.setId(playerId);
		player.setName(name);
		player.setChannel(ctx.channel());
		player.setIsland(true);
		CommonData.putPlayerIdToPlayer(playerId, player);
		// TODO
		boolean isSuccess = true;// 登录成功
		MessageInfo.Builder messageInfo = MessageInfo.newBuilder();
		if (isSuccess) {
			DdzProto.LoginResp.Builder loginResp = DdzProto.LoginResp
					.newBuilder();
			messageInfo.setMessageId(MESSAGE_ID.msg_LoginResp);
			loginResp.setPlayerBaseInfo(playerBaseInfo);
			messageInfo.setLoginResp(loginResp);
		} else {
			messageInfo.setMessageId(MESSAGE_ID.msg_MsgInfo);
			MsgInfo.Builder msgInfo = MsgInfo.newBuilder();
			String error = MessageConstants.LOGIN_ERROR_MSG_1003;// 错误信息
			msgInfo.setType(MessageConstants.LOGIN_ERROR_TYPE_1003);
			msgInfo.setMessage(error);
			messageInfo.setMsgInfo(msgInfo);
		}

		return messageInfo;
	}

	/**
	 * 根据token获取用户微信基本信息
	 * @param token
	 * @return
	 */
	private PlayerBaseInfo getUserInfoByToken(String token){
		PlayerBaseInfo.Builder player = PlayerBaseInfo.newBuilder();
		AccessTokenOpenId accessTokenOpenId = getAccessTokenOpenId(token);
		if (accessTokenOpenId!=null){
			String openid = accessTokenOpenId.getOpenid();
			String access_token = accessTokenOpenId.getAccess_token();
			WeixinUserInfo weixinUserInfo = getWeixinUserInfo(access_token,openid);
//			Player pl = getPlayerByOpenid(openid);
			player.setID((int)(Math.random()*9+1)*100000);
			player.setName(weixinUserInfo.getName());
			player.setImgUrl(weixinUserInfo.getHeadImgUrl());
		}

		return player.build();
	}

	private Player getPlayerByOpenid(String openId){
		String url = WeixinConstants.getPlayerByOpenidUrl;
		url = url.replace("TOKEN", openId);
		JSONObject obj = WeixinUtil.httpRequest(url, "POST", null);
		Player player = new Player();
		if (obj == null){
			String url1 = WeixinConstants.addUserWithOpenid;
			url1 = url1.replace("OPENID", openId);
			JSONObject obj1 = WeixinUtil.httpRequest(url1, "POST", null);
			player.setId(obj1.getInt("id"));
			return player;
		}
		player.setId(obj.getInt("id"));
		return player;
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
