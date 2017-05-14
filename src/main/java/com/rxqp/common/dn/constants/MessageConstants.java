package com.rxqp.common.dn.constants;

public class MessageConstants {

	public static Integer PLAYER_WAITER_FOR_STATUS_TYPE = 0;
	public static String PLAYER_WAITER_FOR_STATUS_MSG = "等待其他玩家!";

	public static Integer PLAYER_READY_STATUS_TYPE = 1;
	public static String PLAYER_READY_STATUS_MSG = "开始玩牌!";

	public static Integer PLAYER_PLAYING_TYPE = 2;
	public static String PLAYER_PLAYING_MSG = "玩牌中!";

	public static Integer PLAYER_QUIT_STATUS_TYPE = 3;
	public static String PLAYER_QUIT_STATUS_MSG = "退出!";

	public static Integer UNKNOWN_CAUSE_TYPE = 1000;
	public static String UNKNOWN_CAUSE_MSG = "未知内部原因异常!";

	public static Integer PLAYER_NO_LOGIN_TYPE_1001 = 1001;
	public static String PLAYER_NO_LOGIN_MSG_1001 = "玩家没有登录!";

	public static Integer PLAYER_STATE_TYPE_1002 = 1002;
	public static String PLAYER_STATE_MSG_1002 = "玩家正在游戏中!";

	public static Integer LOGIN_ERROR_TYPE_1003 = 1003;
	public static String LOGIN_ERROR_MSG_1003 = "登录失败!";

	public static Integer PLAYER_STATE_TYPE_1004 = 1004;
	public static String PLAYER_STATE_MSG_1004 = "玩家已经登录!";

	public static Integer PLAYER_STATE_TYPE_1005 = 1005;
	public static String PLAYER_STATE_MSG_1005 = "至少要两个玩家才能开牌!";

	public static Integer CREATE_ROOM_ERROR_TYPE = 3001;
	public static String CREATE_ROOM_ERROR_MSG = "创建房间失败!";

	public static Integer ENTRY_ROOM_ERROR_TYPE_4000 = 4000;
	public static String ENTRY_ROOM_ERROR_MSG_4000 = "房间没有人创建!";

	public static Integer ENTRY_ROOM_ERROR_TYPE_4001 = 4001;
	public static String ENTRY_ROOM_ERROR_MSG_4001 = "房间人数已满!";

	public static Integer ENTRY_ROOM_ERROR_TYPE_4002 = 4002;
	public static String ENTRY_ROOM_ERROR_MSG_4002 = "该房间已经开始游戏，不能进入!";

	public static Integer THE_ROOM_NO_EXTIST_ERROR_TYPE = 4002;
	public static String THE_ROOM_NO_EXTIST_ERROR_MSG = "该房间不存在!";

	public static Integer THE_ROOM_NO_EXTIST_PLAYER_TYPE = 4003;
	public static String THE_ROOM_NO_EXTIST_PLAYER_MSG = "该房间不存在玩家";

	public static Integer BANKER_CARDS_ERROR_TYPE = 5001;
	public static String BANKER_CARDS_ERROR_MSG = "庄家牌有误!";

}
