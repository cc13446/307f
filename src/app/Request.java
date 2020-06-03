package app;

import java.util.Date;
import Enum.*;
public class Request {

    private int roomId;

    private double targetTemp;

    private FanSpeed fanSpeed;

    private long during;

    private Mode targetMode;

    public Request(int roomId,double targetTemp,FanSpeed fanSpeed,long during,Mode targetMode){
        this.roomId=roomId;
        this.targetTemp=targetTemp;
        this.fanSpeed=fanSpeed;
        this.during=during;
        this.targetMode=targetMode;
    }


    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public long getDuring() {
        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public Mode getTargetMode() {
        return targetMode;
    }

    public void setTargetMode(Mode targetMode) {
        this.targetMode = targetMode;
    }

    @Override
    public String toString() {
        return "Request{" +
                "roomId=" + roomId +
                ", targetTemp=" + targetTemp +
                ", fanSpeed=" + fanSpeed +
                ", during=" + during +
                ", targetMode=" + targetMode +
                '}';
    }
}
