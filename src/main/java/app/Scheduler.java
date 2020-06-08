package app;

import Dao.LogDao;
import Domain.Log;
import Domain.Request;
import Domain.Room;
import Enum.*;

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
    public HoldOnQueue holdOnQueue;

    private final int MAX_SERVE_QUEUE_SIZE;

    public RoomList roomList;

    public LogDao logDao;

    public Scheduler(double feeRateHigh, double feeRateMid, double feeRateLow, LogDao logDao){
        FEE_RATE_HIGH=feeRateHigh;
        FEE_RATE_MID=feeRateMid;
        FEE_RATE_LOW=feeRateLow;
        MAX_SERVE_QUEUE_SIZE=3;

        state=State.OFF;
        roomList=new RoomList();
        waitQueue=new WaitQueue(FEE_RATE_HIGH, FEE_RATE_MID, FEE_RATE_LOW,roomList,logDao);
        this.logDao = logDao;
        serveQueue=new ServeQueue(FEE_RATE_HIGH, FEE_RATE_MID, FEE_RATE_LOW, this,logDao,roomList);
        holdOnQueue=new HoldOnQueue(FEE_RATE_HIGH, FEE_RATE_MID, FEE_RATE_LOW, logDao,roomList);
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

    public void addRoom(int customId, int roomID, double currentTemp, double targetTemp) {
        roomList.addRoom(customId,roomID, currentTemp, targetTemp, FanSpeed.MEDIUM, FEE_RATE_MID);
        System.out.println("Scheduler addRoom");
    }

    public void removeRoom(int ID){
        roomList.removeRoom(ID);
        System.out.println("removeRoom");
    }

    public void changeFanSpeed(int customId, FanSpeed fanSpeed){
        if(!serveQueue.changeRequestFanSpeed(customId, fanSpeed) && !waitQueue.changeRequestFanSpeed(customId, fanSpeed)){
            holdOnQueue.changeRequestFanSpeed(customId, fanSpeed);
        }
        Room room=roomList.findRoom(customId);
        if (null!=room){
            room.setFanSpeed(fanSpeed);
            if (FanSpeed.HIGH==fanSpeed) room.setFeeRate(FEE_RATE_HIGH);
            else if(FanSpeed.MEDIUM==fanSpeed) room.setFeeRate(FEE_RATE_MID);
            else room.setFeeRate(FEE_RATE_LOW);
        }
        schedule();
    }

    public void changeTargetTemp(int customId, double temp){
        if(!serveQueue.changeRequestTemp(customId, temp) && !waitQueue.changeRequestTemp(customId, temp)){
            holdOnQueue.changeRequestTemp(customId, temp);
        }
        Room room=roomList.findRoom(customId);
        if (null!=room){
            room.setTargetTemp(temp);
        }
    }

    public void dealWithRequest(Request request){
        Room room = roomList.findRoom(request.getCustomId());
        room.setState(State.ON);
        logDao.storeLog(new Log(request.getCustomId(),request.getRoomId(), ScheduleType.REQUEST_ON, request.getTargetMode(), request.getFanSpeed(), room.getCurrentTemp(), request.getTargetTemp(), room.getFee(), room.getFeeRate()));
        waitQueue.addRequest(request);
        System.out.println("dealWithRequest(Request request)");
        schedule();
    }

    public void dealWithRequestOff(int customId){
        Request req=serveQueue.findRequest(customId);
        Room room = roomList.findRoom(customId);
        room.setState(State.OFF);
        logDao.storeLog(new Log(req.getCustomId(),req.getRoomId(), ScheduleType.REQUEST_OFF, req.getTargetMode(), req.getFanSpeed(), room.getCurrentTemp(), req.getTargetTemp(), room.getFee(), room.getFeeRate()));
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
        req=holdOnQueue.findReqeust(customId);
        if (req!=null){
            //在等待队列中
            holdOnQueue.removeRequest(customId);
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
            System.out.println("schedule 服务对象数小于上限");
            waitQueue.removeRequest(req.getCustomId());
            serveQueue.addRequest(req);
            // 启动一个Servant

        }else{
            //服务对象数大于等于上限
            Request serveReq=serveQueue.getSlowestFanSpeedRequest();
            Request waitReq=waitQueue.getFastestFanSpeedRequest();
            if (waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())>0){
                //等待队列中有风速更快的，触发优先级调度
                System.out.println("schedule 触发优先级调度");
                serveQueue.removeRequest(serveReq.getCustomId());
                waitQueue.addRequest(serveReq);
                waitQueue.removeRequest(waitReq.getCustomId());
                serveQueue.addRequest(waitReq);
                schedule();
            }else if(waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())==0){
                for(Servant servant : serveQueue.servantList){
                    if(servant.getRequest().getFanSpeed() == serveReq.getFanSpeed()){
                        if(!servant.getRRFlag()){
                            servant.setRRFlag(true);
                        }

                    }
                    else {
                        servant.setRRFlag(false);
                    }
                }
            }else{
                for(Servant servant : serveQueue.servantList){
                    servant.setRRFlag(false);
                }
            }
        }
    }
}
