package Controller;

import app.CheckRoomStateServant;
import Domain.RoomStateForm;
import app.Scheduler;

import java.util.List;

public class CheckRoomStateController {
    private Scheduler scheduler;

    public CheckRoomStateController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    public List<RoomStateForm> getRoomStateFormList(){
        System.out.println("CheckRoomStateController:getRoomStateFormList");
        return new CheckRoomStateServant().getRoomStateFormList(scheduler);
    }
}