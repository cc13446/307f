package app;

import Enum.FanSpeed;
import Enum.State;

import java.io.Serializable;

public class RoomStateForm implements Serializable {
    public int roomID;
    public int customerID;
    public int state;
    public double currentTemp;
    public double targetTemp;
    public int fanSpeed;
    public double feeRate;
    public double fee;
    public long duration;

    public RoomStateForm(int state, double fee, double currentTemp) {
        this.state = state;
        this.fee = fee;
        this.currentTemp = currentTemp;
    }

    public RoomStateForm(int customerID,int roomId,int state, double fee, double currentTemp, double targetTemp, double feeRate, long duration, int fanSpeed) {
        this.state = state;
        this.fee = fee;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.feeRate = feeRate;
        this.duration = duration;
        this.fanSpeed = fanSpeed;
        this.customerID=customerID;
        this.roomID=roomId;
    }

    @Override
    public String toString() {
        return "RoomStateForm{" +
                "state=" + state +
                ", currentTemp=" + currentTemp +
                ", targetTemp=" + targetTemp +
                ", fanSpeed=" + fanSpeed +
                ", feeRate=" + feeRate +
                ", fee=" + fee +
                ", duration=" + duration +
                '}';
    }
}
