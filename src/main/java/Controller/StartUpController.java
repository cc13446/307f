package Controller;

import Enum.*;
import MyHttpHandler.FanHttpHandler;
import MyHttpServe.HttpToServe;
import app.Request;
import app.Scheduler;

import java.io.IOException;

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
    private Mode mode;
    private double tempHighLimit;
    private double tempLowLimit;
    private double defaultTargetTemp;

    public StartUpController(double feeRateHigh, double feeRateMid, double feeRateLow, Mode mode, double tempHighLimit, double tempLowLimit, double defaultTargetTemp) {
        this.feeRateHigh = feeRateHigh;
        this.feeRateMid = feeRateMid;
        this.feeRateLow = feeRateLow;
        this.mode = mode;
        this.tempHighLimit = tempHighLimit;
        this.tempLowLimit = tempLowLimit;
        this.defaultTargetTemp = defaultTargetTemp;
    }

    public boolean powerOn() throws IOException {
        scheduler = new Scheduler(feeRateHigh, feeRateMid, feeRateLow);
        setPara(mode, tempHighLimit, tempLowLimit, defaultTargetTemp);
        useController = new UseController(scheduler);
        checkRoomStateController = new CheckRoomStateController(scheduler);
        // test
        Request request1=new Request(1,35,FanSpeed.LOW,1000, Mode.FAN);
        scheduler.dealWithRequest(request1);
        Request request2=new Request(2,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request2);
        Request request3=new Request(3,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request3);
        Request request4=new Request(4,40,FanSpeed.MEDIUM,100,Mode.FAN);
        scheduler.dealWithRequest(request4);
        Request request5=new Request(5,40,FanSpeed.MEDIUM,100,Mode.FAN);
        scheduler.dealWithRequest(request5);

        FanHttpHandler fanHttpHandler = new FanHttpHandler(useController);
        HttpToServe fanServe = new HttpToServe("/room/fan", 80);
        fanServe.beginServe(fanHttpHandler);

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