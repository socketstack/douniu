package com.rxqp.Task;

import com.rxqp.common.data.CommonData;
import com.rxqp.common.exception.BusinnessException;
import com.rxqp.protobuf.DdzProto;
import com.rxqp.server.bo.Player;
import com.rxqp.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by mengfanfei on 2017/7/4.
 */
public class CheckPlayersTask extends TimerTask {

    public void checkPlayers(CheckPlayersTask task){
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 5 * 1000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    @Override
    public void run() {
        Map<Integer, Player> playerIdToPlayer = CommonData.getPlayerIdToPlayerMap();
        for (Player player:playerIdToPlayer.values()){
            if (!player.getOnline())
                continue;
            Date lastTime = player.getLastTime();
            if (lastTime!=null){
                long diffTime = DateUtils.getDiffTime(new Date(),lastTime);
                if (diffTime > 30*1000){//超过30S,当前玩家已经掉线
                    player.setOnline(false);
                    player.getChannel().close();
                    postPlayerOffLine(player.getId());
                    System.out.println("^^^^^^^^^playerId is offLine:"+player.getId());
                }
            }
        }
    }

    public void postPlayerOffLine(Integer playerId){
        try {
            List<Player> players = CommonData.getPlayersByIdInSameRoom(playerId);
            if (CollectionUtils.isNotEmpty(players)){
                for (Player player:players){
                    DdzProto.MessageInfo.Builder msg = DdzProto.MessageInfo.newBuilder();
                    msg.setMessageId(DdzProto.MESSAGE_ID.msg_PostPlayerOffline);
                    DdzProto.PostPlayerOffline.Builder postPlayerOffline = DdzProto.PostPlayerOffline
                            .newBuilder();
                    postPlayerOffline.setPlayerId(playerId);
                    msg.setPostPlayerOffline(postPlayerOffline);

                    player.getChannel().writeAndFlush(msg.build());
                }
            }
        } catch (BusinnessException e) {
            e.printStackTrace();
        }

    }
}
