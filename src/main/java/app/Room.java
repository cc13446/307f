package app;

import Enum.*;

import java.lang.annotation.Target;

public class Room {


    //标志服务的唯一ID
    private int customId;
    private State state;
    private int roomID;
    private double currentTemp;
    private double targetTemp;
    private double fee;
    private double feeRate;
    private FanSpeed fanSpeed;
    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(double feeRate) {
        this.feeRate = feeRate;
    }

    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public State getState() {
        return state;
    }

    public int getRoomID() {
        return roomID;
    }

    public double getFee() {
        return fee;
    }

    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }


    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public Room(int customId, int roomID, double currentTemp, double targetTemp) {
        this.customId = customId;
        this.state = State.OFF;
        this.roomID = roomID;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.fee = 0;
        this.fanSpeed=FanSpeed.MEDIUM;
        this.duration=0;
        System.out.println("Create room " + roomID + " with CurrentTemp:" + currentTemp );
    }

    @Override
    public String toString() {
        return "Room{" +
                "customId=" + customId +
                ", state=" + state +
                ", roomID=" + roomID +
                ", currentTemp=" + currentTemp +
                ", targetTemp=" + targetTemp +
                ", fee=" + fee +
                ", feeRate=" + feeRate +
                ", fanSpeed=" + fanSpeed +
                ", duration=" + duration +
                '}';
    }
}