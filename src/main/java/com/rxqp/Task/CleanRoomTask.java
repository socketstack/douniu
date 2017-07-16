package com.rxqp.Task;

import com.rxqp.common.data.CommonData;
import com.rxqp.protobuf.DdzProto;
import com.rxqp.server.bo.Player;
import com.rxqp.server.bo.Room;
import com.rxqp.server.bussiness.biz.impl.RoomBizImpl;
import com.rxqp.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by mengfanfei on 2017/6/15.
 */
public class CleanRoomTask extends TimerTask {

    RoomBizImpl roomBiz = new RoomBizImpl();
    public void cleanRoomTask(CleanRoomTask task){
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1 * 10000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    @Override
    public void run() {
        Map<Integer, Room> roomMap = CommonData.getAllRoom();
        for (Map.Entry<Integer,Room> entry : roomMap.entrySet()){
            Room room = entry.getValue();
            Date firtPlayerOffLineTime = room.getFirtPlayerOffLineTime();
            if (firtPlayerOffLineTime!=null){
                long diffTime = DateUtils.getDiffTime(new Date(),firtPlayerOffLineTime);
                if(diffTime >= 20*60*1000){//第一位离线玩家开始计时，超过20分钟，则自动解散该房间
                    System.out.println("^^^^^^^^^^^^^^^revmove room id:"+room.getRoomId());
                    //解散房间广播
                    if (CollectionUtils.isNotEmpty(room.getPlayers())){
                        DdzProto.MessageInfo.Builder postMsgInfo = DdzProto.MessageInfo.newBuilder();
                        postMsgInfo.setMessageId(DdzProto.MESSAGE_ID.msg_PostDissolutionResult);
                        DdzProto.PostDissolutionResult.Builder postDissolutionResult = DdzProto.PostDissolutionResult
                                .newBuilder();
                        postMsgInfo.setPostDissolutionResult(postDissolutionResult);
                        DdzProto.MessageInfo.Builder mi = dissolutionBuildSettementData(room);
                        //解散房间广播
                        for (Player pl : room.getPlayers()) {
                            pl.getChannel().writeAndFlush(postMsgInfo.build());// 广播解散房间成功
                            pl.getChannel().writeAndFlush(mi.build());// 广播计算结算信息
                        }
                    }
                    roomBiz.removeRoom(room.getRoomId());
                }
            }
            if (room.getDisband()){
                int diffTime = ((int)System.currentTimeMillis()/1000) - room.getStartDisbandTime();
                if (diffTime > 60){
                    room.setDisband(false);
                    room.setStartDisbandTime(0);
                    room.getAgreeDissolutionIds().clear();
                    room.getDisAgreeDissulutionIds().clear();
                }

            }
        }
    }

    /**
     * 解散房间的时候结算信息
     *
     * @param room
     * @return
     */
    private DdzProto.MessageInfo.Builder dissolutionBuildSettementData(Room room) {
        DdzProto.MessageInfo.Builder mi = DdzProto.MessageInfo.newBuilder();
        mi.setMessageId(DdzProto.MESSAGE_ID.msg_SettlementInfo);
        DdzProto.SettlementInfo.Builder settlementInfo = DdzProto.SettlementInfo.newBuilder();
        settlementInfo.setIsOver(true);
        List<Player> players = room.getPlayers();
        for (Player player : players) {
            DdzProto.SettlementData.Builder playerSettlement = DdzProto.SettlementData
                    .newBuilder();
            playerSettlement.setID(player.getId());
            playerSettlement.setGotscore(player.getScore());
            playerSettlement.setFinalscore(player.getFinalScore());
            playerSettlement.setLeaveCardNum(player.getCardNum());
            if (player.getFinalScore() > 0) {
                playerSettlement.setIsWin(true);
            } else {
                playerSettlement.setIsWin(false);
            }
            settlementInfo.addPlayers(playerSettlement);
        }
        mi.setSettlementInfo(settlementInfo);
        return mi;
    }
}
