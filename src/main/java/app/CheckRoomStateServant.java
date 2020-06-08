package app;

import Enum.*;

import java.util.List;

public class CheckRoomStateServant {

    public List<RoomStateForm> getRoomStateFormList(Scheduler scheduler){
        System.out.println("CheckRoomStateServant:getRoomStateFormList");
        return scheduler.roomList.getRoomStateFormList();
    }
}