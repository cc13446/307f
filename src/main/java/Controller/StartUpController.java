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

/**
 * 空调启动控制器：负责启动空调并设置空调参数
 * 最后修改时间：2020/6/10 16:47
 */

public class StartUpController {
    private Scheduler scheduler;
    public UseController useController;
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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public double getTempHighLimit() {
        return tempHighLimit;
    }

    public void setTempHighLimit(double tempHighLimit) {
        this.tempHighLimit = tempHighLimit;
    }

    public double getTempLowLimit() {
        return tempLowLimit;
    }

    public void setTempLowLimit(double tempLowLimit) {
        this.tempLowLimit = tempLowLimit;
    }

    public double getDefaultTargetTemp() {
        return defaultTargetTemp;
    }

    public void setDefaultTargetTemp(double defaultTargetTemp) {
        this.defaultTargetTemp = defaultTargetTemp;
    }

    // 构造函数
    public StartUpController(double feeRateHigh, double feeRateMid, double feeRateLow, Mode mode, double tempHighLimit, double tempLowLimit, double defaultTargetTemp) {
        this.feeRateHigh = feeRateHigh;
        this.feeRateMid = feeRateMid;
        this.feeRateLow = feeRateLow;
        this.mode = mode;
        this.tempHighLimit = tempHighLimit;
        this.tempLowLimit = tempLowLimit;
        this.defaultTargetTemp = defaultTargetTemp;
    }

    // 空调开机
    public boolean powerOn() throws IOException {
        System.out.println("powerOn 执行了");
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
                new RoomInitHttpHandler(this),
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
        System.out.println("创建handler");

        return true;
    }

    // 设置空调基本参数
    public boolean setPara(Mode mode, double tempHighLimit, double tempLowLimit, double defaultTargetTemp) {
        scheduler.setState(State.SET_MODE);
        scheduler.setDefaultMode(mode);
        scheduler.setTempHighLimit(tempHighLimit);
        scheduler.setTempLowLimit(tempLowLimit);
        scheduler.setDefaultTargetTemp(defaultTargetTemp);

        return true;
    }

    // 空调启动
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