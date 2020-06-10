package Controller;

import Domain.Request;
import Domain.Room;
import Enum.*;
import app.*;

public class UseController {
    private Scheduler scheduler;

    public UseController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void requestOn(Request request,double currentTemperature) {
        System.out.println(request);
        scheduler.roomList.findRoom(request.getCustomId()).setCurrentTemp(currentTemperature);
        scheduler.dealWithRequest(request);
    }

    public void changeTargetTemp(int customID, double targetTemp) {
        scheduler.changeTargetTemp(customID, targetTemp);
    }

    public void changeFanSpeed(int roomID, FanSpeed fanSpeed) {
        scheduler.changeFanSpeed(roomID, fanSpeed);
    }

    public void requestOff(int customID) {
        scheduler.dealWithRequestOff(customID);
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

    public double requestFee(int customId, double currentTemp) {
        Room room = scheduler.roomList.findRoom(customId);
        room.setCurrentTemp(currentTemp);
        Request request = scheduler.holdOnQueue.findReqeust(customId);
        if(request != null && Math.abs(room.getCurrentTemp() - request.getTargetTemp()) >= 1){
            System.out.println("待机重新调度");
            scheduler.holdOnQueue.removeRequest(customId);
            scheduler.waitQueue.addRequest(request);
            scheduler.schedule();
        }
        return room.getFee();
    }
    public State requestState(int customId) {
        Room room = scheduler.roomList.findRoom(customId);
        return room.getState();
    }

    public void addRoom(int customId, int roomID, double currentTemp, double targetTemp) {
        System.out.println("useController addRoom");
        scheduler.addRoom(customId,roomID, currentTemp, targetTemp);
    }

    public Mode getMode(){
        return scheduler.getDefaultMode();
    }
    public int findRoomId(int customId){
        return scheduler.roomList.findRoom(customId).getRoomID();
    }

    public int getMaxCustomId(){
        return scheduler.logDao.QueryMaxCustomId();
    }
}