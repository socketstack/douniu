package com.rxqp.common.constants;

/**
 * Created by mengfanfei on 2017/6/4.
 */
public class WeixinConstants {
    // 公众号原始ID
    public static String originalId = "gh_7c6a4eaa152a";
    // 微信支付商号
    public static String mch_id = "1259469201";
    // 微信api秘钥
    public static String api_secret_key = "7ondrp6xukl5uh6k1x61k7bce7hhxty6";
    // 微信支付统一下单接口
    public static String UnifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static String JSAPI = "JSAPI";
    // 获取js接口临时票据url
    public static String JsApiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static String DateFormate = "yyyy-MM-dd HH:mm:ss";
    // 设备号,终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    public static String device_info = "WEB";
    public static String success = "SUCCESS";
    public static String fail = "FAIL";
    // ///////////// OAuth2.0 用户授权认证//////////////////////
    // 获取用户认证前的code
    public static String getCodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    public static String getOpenidAndAccessCode = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 不弹出授权页面，直接跳转，只能获取用户openid
    public static String OAuthScopeBasic = "snsapi_base";
    // 弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
    public static String OAuthScopeBasec = "snsapi_userinfo";

    public static String getOAuthAccessToken = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    //****************************************测试环境***************************************************
//    public static String getPlayerByOpenidUrl = "http://localhost:8081/getPlayerByOpenid.do?openid=OPENID";
//    public static String getPlayerByPlayeridUrl = "http://localhost:8081/getPlayerByPlayerid.do?playerid=PLAYERID";
//    public static String addUserWithOpenid = "http://localhost:8081/addUser.do?openid=OPENID&name=NAME&imgUrl=IMG_URL";
    //****************************************测试环境***************************************************

    //****************************************线上环境***************************************************
    public static String getPlayerByOpenidUrl = "http://139.129.98.110:8090/game_service/getPlayerByOpenid.do?openid=OPENID";
    public static String getPlayerByPlayeridUrl = "http://139.129.98.110:8090/game_service/getPlayerByPlayerid.do?playerid=PLAYERID";
    public static String addUserWithOpenid = "http://139.129.98.110:8090/game_service/addUser.do?openid=OPENID&name=NAME&imgUrl=IMG_URL";
    public static String deductionRoomCardsUrl = "http://139.129.98.110:8090/game_service/deductionRoomCards.do?userId=USERID&&cards=CARDS";
//    public static String deductionRoomCardsUrl = "http://localhost:8081/deductionRoomCards.do?userId=USERID&&cards=CARDS";
    //****************************************线上环境***************************************************

    public static String getWXUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    // ///////////// OAuth2.0 用户授权认证////////////////////
    // *************************************测试环境*************************************
    // 第三方用户唯一凭证
    // public static String appId = "wx2ed20c1fa7ad5f75";
    // 第三方用户唯一凭证密钥
    // public static String appSecret = "fa579e6c065064a4aba679a63a4cb3de";

    // 接收微信支付异步通知回调地址(测试)
//	public static String notify_url = "http://6rxvfz.natappfree.cc/wxPayNotify.do";
//	public static String WebUrl = "http://6rxvfz.natappfree.cc/wxIndex.do";
//	public static String SceneryUrl = "http://6rxvfz.natappfree.cc/wxScenery.do";
//	public static String TripMap = "http://6rxvfz.natappfree.cc/wxNavigation.do";
//	public static String VideoUrl = "http://6rxvfz.natappfree.cc/wxVideo.do";
    // 本地测试环境
//	public static String OauRedirectUrl = "http://6rxvfz.natappfree.cc/wxPrePay.do";
//	public static String OauRedirectUrlGetCode = "http://6rxvfz.natappfree.cc/wxPrePayGetCode.do";
//	public static String OauRedirectUrlLuckyDraw = "http://6rxvfz.natappfree.cc/wxLucky.do";
//	public static String itrPicUrl = "http://6rxvfz.natappfree.cc/images/wap/index/wxtextimg.jpg";
//	public static String trafficMapUrl = "http://6rxvfz.natappfree.cc/images/trafficMap.jpg";
//	public static String customerServiceUrl = "http://6rxvfz.natappfree.cc/coreServlet.do";
    // 本地测试环境,获取网页授权之前获取code，微信服务器回调
    public static String customerServcieReturnUrl = "http://6rxvfz.natappfree.cc/wxCustomerService.do";
    // *************************************测试环境*************************************

    // //
    // *************************************线上环境*************************************
    // // 第三方用户唯一凭证
    public static String appId = "wx1be92444a5248a8d";
    // // 第三方用户唯一凭证密钥
    public static String appSecret = "23e113e87d114beb89b870c69a14d901";

    // 接收微信支付异步通知回调地址
    public static String notify_url = "http://www.plxpl.cn/plxpl/wxPayNotify.do";
    public static String WebUrl = "http://www.plxpl.cn/plxpl/wxIndex.do";
    public static String SceneryUrl = "http://www.plxpl.cn/plxpl/wxScenery.do";
    public static String TripMap = "http://www.plxpl.cn/plxpl/wxNavigation.do";
    public static String VideoUrl = "http://www.plxpl.cn/plxpl/wxVideo.do";
    // 授权后重定向的回调链接地址,使用urlencode对链接进行处理
    public static String OauRedirectUrl = "http://www.plxpl.cn/plxpl/wxPrePay.do";
    public static String OauRedirectUrlLuckyDraw = "http://www.plxpl.cn/plxpl/wxLucky.do";
    public static String OauRedirectUrlGetCode = "http://www.plxpl.cn/plxpl/wxPrePayGetCode.do";
    public static String itrPicUrl = "http://www.plxpl.cn/plxpl/images/wap/index/wxtextimg.jpg";
    public static String trafficMapUrl = "http://www.plxpl.cn/plxpl/images/trafficMap.jpg";
    public static String customerServiceUrl = "http://www.plxpl.cn/plxpl/wxCustomerService.do";
    // *************************************线上环境*************************************
}
