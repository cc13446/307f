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

    public boolean powerOn() {
        scheduler = new Scheduler();
        useController = new UseController(scheduler);
        checkRoomStateController = new CheckRoomStateController(scheduler);

        return true;
    }

    public boolean setPara(Mode mode, double tempHighLimit, double tempLowLimit, double defaultTargetTemp, double feeRateHigh, double feeRateMid, double feeRateLow) {

    }

    public boolean startUp() {

    }

}