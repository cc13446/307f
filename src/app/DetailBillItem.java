package app;

import java.util.Date;
import Enum.FanSpeed;
import Enum.Mode;

public class DetailBillItem {
    private Date startTime;
    private Date endTime;
    private double targetTemp;
    private Date currentTime;
    private double fee;
    private double feeRate;
    private Date RequestTime;
    private double duration;
    private FanSpeed fanSpeed;
    private Mode mode;

    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(double feeRate) {
        this.feeRate = feeRate;
    }

    public Date getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(Date requestTime) {
        RequestTime = requestTime;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }



    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getStartTime(){
        return this.endTime;
    }
    public void setEndTime(Date endTime){
        this.endTime=endTime;
    }
    public Date getEndTime(){
        return this.endTime;
    }
    public void setFee(double fee){
        this.fee=fee;

    }
    public double getFee(){
        return this.fee;
    }

}
