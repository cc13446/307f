package Domain;

import Enum.FanSpeed;
import Enum.State;

/**
 * 房间：用于在服务器中记录对应房间的各种状态
 * 最后修改时间：2020/6/7 1:40
 */

public class Room {
    // 标志服务的唯一ID
    private int customId;
    // 房间中的空调状态
    private State state;
    // 房间ID
    private int roomID;
    // 房间的当前温度
    private double currentTemp;
    // 空调的目标温度
    private double targetTemp;
    // 当前的总费用
    private double fee;
    // 当前费率
    private double feeRate;
    // 当前风速
    private FanSpeed fanSpeed;
    // 总服务时间
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

    public Room(int customId, int roomID, double currentTemp, double targetTemp, FanSpeed defaultFanSpeed, double defaultFeeRate) {
        this.customId = customId;
        this.state = State.OFF;
        this.roomID = roomID;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.fee = 0;
        this.fanSpeed= defaultFanSpeed;
        this.duration=0;
        this.feeRate = defaultFeeRate;
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