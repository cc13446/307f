package app;
import Domain.Log;
import Enum.*;
import Dao.LogDao;
public class Servant {
    private int roomId;
    private Mode mode;
    private FanSpeed fanSpeed;
    private double currentTemp;
    private double targetTemp;
    private double fee;
    private double feeRate;
    private LogDao logDao = new LogDao();

    public Servant(int roomId, Mode mode, FanSpeed fanSpeed, double targetTemp, double feeRate) {
        this.roomId = roomId;
        this.mode = mode;
        this.fanSpeed = fanSpeed;
        this.targetTemp = targetTemp;
        this.fee = 0;
        this.feeRate = feeRate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
    public boolean changeRoomTemp(){
        return true;
    }

    public boolean storeLog(ScheduleType scheduleType){
        Log log = new Log(roomId, scheduleType, mode, fanSpeed, currentTemp, targetTemp, fee, feeRate);
        return logDao.storeLog(log);
    }

    public boolean changeFee(){
        fee += feeRate;
        return true;
    }
}
