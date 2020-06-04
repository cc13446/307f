package Controller;

import Enum.*;
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