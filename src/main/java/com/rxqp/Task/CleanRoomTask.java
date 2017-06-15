package com.rxqp.Task;

import com.rxqp.common.data.CommonData;
import com.rxqp.server.bo.Room;
import com.rxqp.server.bussiness.biz.impl.RoomBizImpl;
import com.rxqp.utils.DateUtils;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mengfanfei on 2017/6/15.
 */
public class CleanRoomTask extends TimerTask {

    RoomBizImpl roomBiz = new RoomBizImpl();
    public void cleanRoomTask(CleanRoomTask task){
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1 * 1000;
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
                    System.out.print("revmove room id:"+room.getRoomId());
                    roomBiz.removeRoom(room.getRoomId());
                }
            }
        }
    }
}
