package Controller;

import Enum.*;
import app.Request;
import app.Scheduler;

public class StartUpController {
    private Scheduler scheduler;
    private UseController useController;
    private CheckRoomStateController checkRoomStateController;
    private PrintReportController printReportController;
    private PrintBillController printBillController;
    private PrintDetailBillController printDetailBillController;
    private double feeRateHigh;
    private double feeRateMid;
    private double feeRateLow;

    public boolean powerOn() {
        scheduler = new Scheduler(feeRateHigh, feeRateMid, feeRateLow);
        useController = new UseController(scheduler);
        checkRoomStateController = new CheckRoomStateController(scheduler);

//        new Thread(scheduler).start();

        Request request1=new Request(1,35,FanSpeed.LOW,1000, Mode.FAN);
        scheduler.dealWithRequest(request1);
        Request request2=new Request(2,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request2);
        Request request3=new Request(3,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request3);
        Request request4=new Request(4,40,FanSpeed.MEDIUM,100,Mode.FAN);
        scheduler.dealWithRequest(request4);
        Request request5=new Request(5,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Request request6=new Request(6,40,FanSpeed.HIGH,100,Mode.FAN);
                    scheduler.dealWithRequest(request6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return true;
    }

    public boolean setPara(Mode mode, double tempHighLimit, double tempLowLimit, double defaultTargetTemp) {
        scheduler.setState(State.SET_MODE);
        scheduler.setDefaultMode(mode);
        scheduler.setTempHighLimit(tempHighLimit);
        scheduler.setTempLowLimit(tempLowLimit);
        scheduler.setDefaultTargetTemp(defaultTargetTemp);

        return true;
    }

    public boolean startUp() {
        scheduler.setState(State.ON);

        return true;
    }

    public void setFeeRateHigh(double feeRateHigh) {
        this.feeRateHigh = feeRateHigh;
    }

    public void setFeeRateMid(double feeRateMid) {
        this.feeRateMid = feeRateMid;
    }

    public void setFeeRateLow(double feeRateLow) {
        this.feeRateLow = feeRateLow;
    }
}