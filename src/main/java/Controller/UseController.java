package Controller;

import Enum.*;
import app.Request;
import app.Room;
import app.Scheduler;

public class UseController {
    private Scheduler scheduler;

    public UseController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void requestOn(Request request) {
        scheduler.dealWithRequest(request);
    }

    public void changeTargetTemp(int roomID, double targetTemp) {
        scheduler.changeTargetTemp(roomID, targetTemp);
    }

    public void changeFanSpeed(int roomID, FanSpeed fanSpeed) {
        scheduler.changeFanSpeed(roomID, fanSpeed);
    }

    public void requestOff(int roomID) {
        scheduler.dealWithRequestOff(roomID);
    }

    public void requestEnd(int ID){
        if (null!=scheduler.waitQueue.findRequest(ID)){
            scheduler.waitQueue.removeRequest(ID);
            scheduler.schedule();
        }
        if (null!=scheduler.serveQueue.findRequest(ID)){
            scheduler.serveQueue.removeRequest(ID);
            scheduler.schedule();
        }
        scheduler.removeRoom(ID);
    }

    public double requestFee(int roomID) {
        Room room = scheduler.roomList.findRoom(roomID);
        return room.getFee();
    }

    public void addRoom(int roomID, double currentTemp, double initTemp) {
        System.out.println("useController addRoom");
        scheduler.addRoom(roomID, currentTemp, initTemp);
    }

    public Mode getMode(){
        return scheduler.getDefaultMode();
    }
}