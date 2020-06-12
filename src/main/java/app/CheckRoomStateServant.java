package app;

import java.util.List;

/*
 *  查看房间状态的服务对象
 *  最后更新时间：2020/6/8 20:20
 */

public class CheckRoomStateServant {

    public List<RoomStateForm> getRoomStateFormList(Scheduler scheduler){
        System.out.println("CheckRoomStateServant:getRoomStateFormList");
        return scheduler.roomList.getRoomStateFormList();
    }
}