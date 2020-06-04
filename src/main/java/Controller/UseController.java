package Controller;

import app.Request;
import app.Scheduler;
import Enum.FanSpeed;

public class UseController {
    private Scheduler scheduler;

    public UseController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void requestOn(Request request) {

    }

    public void changeTargetTemp(int roomID, double targetTemp) {

    }

    public void changeFanSpeed(int roomID, FanSpeed fanSpeed) {

    }

    public void requestOff(int roomID) {

    }

    public void requestFee(int roomID) {

    }

    public void addRoom(int roomID, double currentTemp, double initTemp) {
        scheduler.addRoom(roomID, currentTemp, initTemp);
    }


}