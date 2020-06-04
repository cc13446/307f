package app;

import Enum.FanSpeed;
import Enum.State;

public class RoomStateForm {
    private State state;
    private double currentTemp;
    private double targetTemp;
    private FanSpeed fanSpeed;
    private double feeRate;
    private double fee;
    private double duration;

    public RoomStateForm(State state, double fee, double currentTemp) {
        this.state = state;
        this.fee = fee;
        this.currentTemp = currentTemp;
    }

    public RoomStateForm(State state, double fee, double currentTemp, double targetTemp, double feeRate, double duration, FanSpeed fanSpeed) {
        this.state = state;
        this.fee = fee;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.feeRate = feeRate;
        this.duration = duration;
        this.fanSpeed = fanSpeed;
    }
}
