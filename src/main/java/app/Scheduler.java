package app;

import Dao.LogDao;
import Domain.Log;
import Domain.Request;
import Domain.Room;
import Enum.*;

/*
 *  调度对象，负责空调服务的调度
 *  最后更新时间：2020/06/11 09:00
 */

public class Scheduler {
    //调度对象状态
    private State state;
    //默认模式
    private Mode defaultMode;
    //最高温度限制
    private double tempHighLimit;
    //最低温度限制
    private double tempLowLimit;
    //默认目标温度
    private double defaultTargetTemp;
    //高风速费率
    private final double FEE_RATE_HIGH;
    //中风速费率
    private final double FEE_RATE_MID;
    //低风速费率
    private final double FEE_RATE_LOW;
    //等待队列
    public WaitQueue waitQueue;
    //服务队列
    public ServeQueue serveQueue;
    //挂起队列
    public HoldOnQueue holdOnQueue;
    //最大服务队列容量
    private final int MAX_SERVE_QUEUE_SIZE;
    //所有开房正被使用的房间列表
    public RoomList roomList;
    //持久化对象
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
        //按照服务队列、等待队列、挂起队列的顺序查找改变风速，若前面找到则不会进后面队列寻找
        if(!serveQueue.changeRequestFanSpeed(customId, fanSpeed) && !waitQueue.changeRequestFanSpeed(customId, fanSpeed)){
            holdOnQueue.changeRequestFanSpeed(customId, fanSpeed);
        }
        //改变房间列表中该房间的状态
        Room room=roomList.findRoom(customId);
        if (null!=room){
            room.setFanSpeed(fanSpeed);
            if (FanSpeed.HIGH==fanSpeed) room.setFeeRate(FEE_RATE_HIGH);
            else if(FanSpeed.MEDIUM==fanSpeed) room.setFeeRate(FEE_RATE_MID);
            else room.setFeeRate(FEE_RATE_LOW);
        }
        //修改风速会触发一次调度
        schedule();
    }

    public void changeTargetTemp(int customId, double temp){
        //改变目标温度与改变风速相同，唯一不同是不会触发调度
        if(!serveQueue.changeRequestTemp(customId, temp) && !waitQueue.changeRequestTemp(customId, temp)){
            holdOnQueue.changeRequestTemp(customId, temp);
        }
        Room room=roomList.findRoom(customId);
        if (null!=room){
            room.setTargetTemp(temp);
        }
    }

    public void dealWithRequest(Request request){
        //处理用户使用空调的请求，将其加入等待队列并触发调度
        Room room = roomList.findRoom(request.getCustomId());
        room.setState(State.ON);
        logDao.storeLog(new Log(request.getCustomId(),request.getRoomId(), ScheduleType.REQUEST_ON, request.getTargetMode(), request.getFanSpeed(), room.getCurrentTemp(), request.getTargetTemp(), room.getFee(), room.getFeeRate()));
        waitQueue.addRequest(request);
        System.out.println("dealWithRequest(Request request)");
        schedule();
    }

    public void dealWithRequestOff(int customId){
        //处理用户将空调关机的请求
        //按顺序将请求从服务队列、等待队列、挂起队列中找到并移除，前面找到就不会进后面队列寻找
        Room room = roomList.findRoom(customId);
        room.setState(State.OFF);
        Request req=serveQueue.findRequest(customId);
        if (req!=null){
            //在服务队列中，需要出队
            serveQueue.removeRequest(customId);
            schedule();
        }
        else {
            req=waitQueue.findRequest(customId);
            if (req!=null){
                //在等待队列中
                waitQueue.removeRequest(customId);
            }
            else
            {
                req=holdOnQueue.findReqeust(customId);
                if (req!=null){
                    //在挂起队列中
                    holdOnQueue.removeRequest(customId);
                }
            }
        }
        //增加一条关机记录
        logDao.storeLog(new Log(req.getCustomId(),req.getRoomId(), ScheduleType.REQUEST_OFF, req.getTargetMode(), req.getFanSpeed(), room.getCurrentTemp(), req.getTargetTemp(), room.getFee(), room.getFeeRate()));
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
            schedule();
        }else{
            //服务对象数大于等于上限
            Request serveReq=serveQueue.getSlowestFanSpeedRequest();
            Request waitReq=waitQueue.getFastestFanSpeedRequest();
            if(waitReq == null || serveReq == null){
                return;
            }
            if (waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())>0){
                //等待队列中有风速更快的，触发优先级调度
                System.out.println("schedule 触发优先级调度");
                serveQueue.removeRequest(serveReq.getCustomId());
                waitQueue.addRequest(serveReq);
                waitQueue.removeRequest(waitReq.getCustomId());
                serveQueue.addRequest(waitReq);
                schedule();
            }else if(waitReq.getFanSpeed().compareTo(serveReq.getFanSpeed())==0){
                //等待队列中最快的与服务队列中最慢的请求风速相同，触发时间片轮转
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
                //等待队列中最快的比服务队列中最慢的还慢，不会触发时间片轮转
                for(Servant servant : serveQueue.servantList){
                    servant.setRRFlag(false);
                }
            }
        }
    }
}
