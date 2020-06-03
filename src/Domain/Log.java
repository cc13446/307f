package Domain;

import Enum.*;

import java.util.Date;

public class Log {
    private int logId;
    private int roomId;
    private ScheduleType scheduleType;
    private Mode mode;
    private FanSpeed fanSpeed;
    private double currentTemp;
    private double targetTemp;
    private double fee;
    private double feeRate;
    private Date time;
    public Log() {

    }
    public Log(int roomId, ScheduleType scheduleType, Mode mode, FanSpeed fanSpeed, double currentTemp, double targetTemp, double fee, double feeRate) {
        this.roomId = roomId;
        this.scheduleType = scheduleType;
        this.mode = mode;
        this.fanSpeed = fanSpeed;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.fee = fee;
        this.feeRate = feeRate;
    }
    public Log(int logId, int roomId, ScheduleType scheduleType, Mode mode, FanSpeed fanSpeed, double currentTemp, double targetTemp, double fee, double feeRate) {
        this.logId = logId;
        this.roomId = roomId;
        this.scheduleType = scheduleType;
        this.mode = mode;
        this.fanSpeed = fanSpeed;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.fee = fee;
        this.feeRate = feeRate;
    }


    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(double feeRate) {
        this.feeRate = feeRate;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId=" + logId +
                ", roomId=" + roomId +
                ", scheduleType=" + scheduleType +
                ", mode=" + mode +
                ", fanSpeed=" + fanSpeed +
                ", currentTemp=" + currentTemp +
                ", targetTemp=" + targetTemp +
                ", fee=" + fee +
                ", feeRate=" + feeRate +
                ", time=" + time +
                '}';
    }
}
