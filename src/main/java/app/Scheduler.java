package app;

import Dao.LogDao;
import Enum.FanSpeed;
import Enum.Mode;
import Enum.State;

public class Scheduler {

    private State state;

    private Mode defaultMode;

    private double tempHighLimit;

    private double tempLowLimit;

    private double defaultTargetTemp;

    private final double FEE_RATE_HIGH;

    private final double FEE_RATE_MID;

    private final double FEE_RATE_LOW;

    public WaitQueue waitQueue;

    public ServeQueue serveQueue;

    private final int MAX_SERVE_QUEUE_SIZE;

    public RoomList roomList;

    private LogDao logDao;

    public Scheduler(double feeRateHigh, double feeRateMid, double feeRateLow, LogDao logDao){
        FEE_RATE_HIGH=feeRateHigh;
        FEE_RATE_MID=feeRateMid;
        FEE_RATE_LOW=feeRateLow;
        MAX_SERVE_QUEUE_SIZE=3;

        state=State.OFF;
        roomList=new RoomList();
        waitQueue=new WaitQueue(roomList);
        this.logDao = logDao;
        serveQueue=new ServeQueue(FEE_RATE_HIGH, FEE_RATE_MID, FEE_RATE_LOW, this);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setDefaultMode(Mode defaultMode) {
        this.defaultMode = defaultMode;
    }

    public void setTempHighLimit(double tempHighLimit) {
        this.tempHighLimit = tempHighLimit;
    }

    public void setTempLowLimit(double tempLowLimit) {
        this.tempLowLimit = tempLowLimit;
    }

    public void setDefaultTargetTemp(double defaultTargetTemp) {
        this.defaultTargetTemp = defaultTargetTemp;
    }

    public double getFEE_RATE_HIGH() {
        return FEE_RATE_HIGH;
    }

    public double getFEE_RATE_MID() {
        return FEE_RATE_MID;
    }

    public double getFEE_RATE_LOW() {
        return FEE_RATE_LOW;
    }

    public Mode getDefaultMode(){
        return defaultMode;
    }

    public void addRoom(int customId, int roomID, double currentTemp, double initTemp) {
        roomList.addRoom(customId,roomID, currentTemp, initTemp);
        System.out.println("Scheduler addRoom");
    }

    public void removeRoom(int ID){
        roomList.removeRoom(ID);
        System.out.println("removeRoom");
    }

    public void changeFanSpeed(int customId, FanSpeed fanSpeed){
        if(!serveQueue.changeRequestFanSpeed(customId, fanSpeed)){
            waitQueue.changeRequestFanSpeed(customId, fanSpeed);
        }
    }

    public void changeTargetTemp(int customId, double temp){
        if(!serveQueue.changeRequestTemp(customId, temp)){
            waitQueue.changeRequestTemp(customId, temp);
        }
    }

    public void dealWithRequest(Request request){
        waitQueue.addRequest(request);
        System.out.println("dealWithRequest(Request request)");
        schedule();
    }

    public void dealWithRequestOff(int customId){
        Request req=serveQueue.findRequest(customId);
        if (req!=null){
            //在服务队列中，需要出队
            serveQueue.removeRequest(customId);
            schedule();
        }
        req=waitQueue.findRequest(customId);
        if (req!=null){
            //在等待队列中
            waitQueue.removeRequest(customId);
        }
    }

    public void schedule(){
        //服务对象数小于上限
        if (serveQueue.size()<MAX_SERVE_QUEUE_SIZE){
            Request req = waitQueue.getFastestFanSpeedRequest();
            // 等待队列中没有请求
            if(req == null){
                return;
            }
            waitQueue.removeRequest(req.getCustomId());
            serveQueue.addRequest(req,logDao,roomList);
            // 启动一个Servant

        }else{
            //服务对象数大于等于上限
            Request serveReq=serveQueue.getSlowestFanSpeedRequest();
            Request waitReq=waitQueue.getFastestFanSpeedRequest();
            if (waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())>0){
                //等待队列中有风速更快的，触发优先级调度
                serveQueue.removeRequest(serveReq.getCustomId());
                waitQueue.addRequest(serveReq);
                waitQueue.removeRequest(waitReq.getCustomId());
                serveQueue.addRequest(waitReq,logDao,roomList);
                schedule();
            }else if(waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())==0){
                //触发时间片轮转
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000); //等改成两分钟
                            if (null!=serveQueue.findRequest(serveReq.getCustomId())){
                                System.out.println("5s后");
                                serveQueue.removeRequest(serveReq.getCustomId());
                                waitQueue.addRequest(serveReq);
                                Request nowWaitReq=waitQueue.getFastestFanSpeedRequest();
                                waitQueue.removeRequest(nowWaitReq.getCustomId());
                                serveQueue.addRequest(nowWaitReq,logDao,roomList);
                                schedule();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
