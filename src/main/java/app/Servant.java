package app;
import Domain.Log;
import Enum.*;
import Dao.LogDao;
import org.junit.Test;

public class Servant{
    private State state;
    private int roomId;
    private Mode mode;
    private FanSpeed fanSpeed;
    private double currentTemp;
    private double targetTemp;
    private double fee;
    private double feeRate;
    private LogDao logDao = new LogDao();

    public Servant(){

    }
//    public Servant(int roomId, Mode mode, FanSpeed fanSpeed, double targetTemp, double feeRate) {
//        this.roomId = roomId;
//        this.mode = mode;
//        this.fanSpeed = fanSpeed;
//        this.targetTemp = targetTemp;
//        this.fee = 0;
//        this.feeRate = feeRate;
//    }

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

    @Test
    public void test() throws InterruptedException {
        this.fee = 0;
        this.currentTemp = 0;
        this.targetTemp = 10;
        this.feeRate = 1.5;
        beginServe();
    }

    public boolean beginServe() throws InterruptedException {
        this.state = State.ON;
        if(!storeLog(ScheduleType.NEW_REQUEST)){
            return false;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(state == State.ON){
                        Thread.sleep(1000);
                        changeRoomTemp();
                        changeFee();
                        System.out.println(fee);
                        System.out.println(currentTemp);
                        if (targetTemp == currentTemp){
                            if(endServe()) return;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();
        return true;
    }

    public Boolean endServe()
    {
        this.state = State.OFF;
        storeLog(ScheduleType.CLOSE);
        return true;
    }

    public boolean changeTargetTemp(double targetTemp){
        this.targetTemp = targetTemp;
        return storeLog(ScheduleType.CHANGE_TEMP);
    }

    public boolean changeFanSpeed(FanSpeed fanSpeed){
        this.fanSpeed = fanSpeed;
        return storeLog(ScheduleType.CHANGE_FAN_SPEED);
    }

    private boolean storeLog(ScheduleType scheduleType){
        Log log = new Log(roomId, scheduleType, mode, fanSpeed, currentTemp, targetTemp, fee, feeRate);
        return logDao.storeLog(log);
    }

    private void changeFee(){
        fee += feeRate;
    }
    private void changeRoomTemp(){
        currentTemp += 1;
    }
}
