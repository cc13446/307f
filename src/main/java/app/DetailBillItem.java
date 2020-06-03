package app;

import java.util.Date;
import Enum.FanSpeed;
import Enum.Mode;
import Enum.ScheduleType;

/**
 * 目标温度 target
 * 风速 fanSpeed
 * 模式 mode
 * 请求时间 requestTime
 *
 */
public class DetailBillItem {
    private Date startTime;
    private Date endTime;
    private double targetTemp;
    //private Date currentTime;
    //private Date requestTime;
    private double fee;
    private double feeRate;
    //private long Time;
    private long duration;
    private FanSpeed fanSpeed;
    private Mode mode;
    private ScheduleType scheduleType;
    //private double feeRate;

    //加上ScheduleType，每个item的fee=后一条的fee-这一条的fee

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

//    public long getTime() {
//        return Time;
//    }
//
//    public void setTime(long time) {
//        Time = time;
//    }
//    public Date getRequestTime() {
//        return RequestTime;
//    }
//
//    public void setRequestTime(Date requestTime) {
//        RequestTime = requestTime;
//    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

//    public Date getCurrentTime() {
//        return currentTime;
//    }
//
//    public void setCurrentTime(Date currentTime) {
//        this.currentTime = currentTime;
//    }

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

    public void setDuration(long duration) {
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
