package Controller;

import app.CheckRoomStateServant;
import app.Scheduler;
import java.util.List;

public class CheckRoomStateController {
    private Scheduler scheduler;

    public CheckRoomStateController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void checkRoomState(List<Integer> roomList) {
//        CheckRoomStateServant checkRoomStateServant = new CheckRoomStateServant();
//        checkRoomStateServant.checkRoomState(roomList);
        CheckRoomStateServant.checkRoomState(roomList);
    }
}