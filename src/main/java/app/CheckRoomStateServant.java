package app;

import Enum.*;

import java.util.List;

public class CheckRoomStateServant {

    public List<RoomStateForm> getRoomStateFormList(Scheduler scheduler){
        return scheduler.roomList.getRoomStateFormList();
    }
}