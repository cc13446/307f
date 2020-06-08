package Controller;

import Dao.LogDao;
import Enum.*;
import MyHttpHandler.*;
import MyHttpServe.HttpToServe;
import app.Scheduler;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartUpController {
    private Scheduler scheduler;
    private UseController useController;
    public CheckRoomStateController checkRoomStateController;
    public PrintReportController printReportController;
    public PrintBillController printBillController;
    public PrintDetailBillController printDetailBillController;
    private double feeRateHigh;
    private double feeRateMid;
    private double feeRateLow;
    private Mode mode;
    private double tempHighLimit;
    private double tempLowLimit;
    private double defaultTargetTemp;
    LogDao logDao;

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
        logDao = new LogDao();
        scheduler = new Scheduler(feeRateHigh, feeRateMid, feeRateLow, logDao);
        setPara(mode, tempHighLimit, tempLowLimit, defaultTargetTemp);

        useController = new UseController(scheduler);
        checkRoomStateController = new CheckRoomStateController(scheduler);
        // 三个控制器有问题
        printDetailBillController = new PrintDetailBillController(logDao);
        printBillController = new PrintBillController(logDao);
        printReportController = new PrintReportController(logDao);

        HttpToServe clientServe = new HttpToServe(80);

        List<HttpHandler> handlerList=new ArrayList<HttpHandler>(Arrays.asList(
                new FanHttpHandler(useController),
                new RoomInitHttpHandler(useController),
                new RequestOnAndOffHandler(useController),
                new TempHttpHandler(useController),
                new RoomExitHttpHandler(useController),
                new FeeHttpHandler(useController)
                ));
        List<String> urlList=new ArrayList<String>(Arrays.asList(
                "/room/fan",
                "/room/initial",
                "/room/service",
                "/room/temp",
                "/room/exit",
                "/room/fee"
        ));

        clientServe.beginServe(urlList,handlerList);

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