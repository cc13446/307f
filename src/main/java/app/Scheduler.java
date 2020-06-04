package app;

import Enum.FanSpeed;
import Enum.Mode;
import Enum.State;

public class Scheduler implements Runnable {

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

    public Scheduler(double feeRateHigh, double feeRateMid, double feeRateLow){
        FEE_RATE_HIGH=feeRateHigh;
        FEE_RATE_MID=feeRateMid;
        FEE_RATE_LOW=feeRateLow;
        MAX_SERVE_QUEUE_SIZE=3;

        state=State.OFF;
        waitQueue=new WaitQueue();
        serveQueue=new ServeQueue();
        roomList=new RoomList();
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

    public void addRoom(int roomID, double currentTemp, double initTemp) {
        roomList.addRoom(roomID, currentTemp, initTemp);
    }

    public void changeFanSpeed(int roomId, FanSpeed fanSpeed){
        Request req=serveQueue.findRequest(roomId);
        if (req!=null){
            //在服务队列中
            req.setFanSpeed(fanSpeed);
            schedule();
        }
        req=waitQueue.findRequest(roomId);
        if (req!=null){
            //在等待队列中
            req.setFanSpeed(fanSpeed);
            schedule();
        }
    }

    public void changeTargetTemp(int roomId, double temp){
        Request req=serveQueue.findRequest(roomId);
        if (null!=req){
            req.setTargetTemp(temp);
        }
        req=waitQueue.findRequest(roomId);
        if (null!=req){
            req.setTargetTemp(temp);
        }
    }

    public void dealWithRequest(Request request){
        waitQueue.addRequest(request);
        schedule();
    }

    public void dealWithRequestOff(int roomId){
        Request req=serveQueue.findRequest(roomId);
        if (req!=null){
            //在服务队列中，需要出队
            serveQueue.removeRequest(roomId);
            schedule();
        }
        req=waitQueue.findRequest(roomId);
        if (req!=null){
            //在等待队列中
            waitQueue.removeRequest(roomId);
        }
    }

    public void schedule(){
        if (serveQueue.size()<MAX_SERVE_QUEUE_SIZE){
            //服务对象数小于上限
            Request req = waitQueue.getFastestFanSpeedRequest();
            waitQueue.removeRequest(req.getRoomId());
            serveQueue.addRequest(req);
        }else{
            //服务对象数大于等于上限
            Request serveReq=serveQueue.getSlowestFanSpeedRequest();
            Request waitReq=waitQueue.getFastestFanSpeedRequest();
            if (waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())>0){
                //等待队列中有风速更快的，触发优先级调度
                serveQueue.removeRequest(serveReq.getRoomId());
                waitQueue.addRequest(serveReq);
                waitQueue.removeRequest(waitReq.getRoomId());
                serveQueue.addRequest(waitReq);
            }else if(waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())==0){
                //触发时间片轮转
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            System.out.println("2s后");
                            serveQueue.removeRequest(serveReq.getRoomId());
                            waitQueue.addRequest(serveReq);
                            schedule();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }else{

            }
        }
    }

    @Override
    public void run() {
        while(true){

        }
    }
}
