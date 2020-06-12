package app;

import java.util.LinkedList;
import java.util.List;

import Dao.LogDao;
import Domain.Log;
import Domain.Request;
import Domain.Room;
import Enum.*;

/*
 *  等待队列，保存正在等待的那些请求
 *  最后更新时间：2020/6/7 01:40
 */

public class WaitQueue {
    //三个费率
    private final double FEE_RATE_HIGH;

    private final double FEE_RATE_MID;

    private final double FEE_RATE_LOW;
    //持久化层对象
    private LogDao logDao;
    //保存正在等待的请求队列
    public List<Request> waitRequestList;
    //房间队列表的引用
    public RoomList roomList;


    public WaitQueue(double FEE_RATE_HIGH, double FEE_RATE_MID, double FEE_RATE_LOW, RoomList roomList, LogDao logDao){
        waitRequestList=new LinkedList<Request>();
        this.roomList = roomList;
        this.FEE_RATE_HIGH = FEE_RATE_HIGH;
        this.FEE_RATE_MID = FEE_RATE_MID;
        this.FEE_RATE_LOW = FEE_RATE_LOW;
        this.logDao = logDao;
    }

    public int size(){
        return waitRequestList.size();
    }

    public void addRequest(Request request){
        //将请求加入等待队列
        waitRequestList.add(request);
        System.out.println(roomList.findRoom(request.getCustomId()));
        roomList.findRoom(request.getCustomId()).setState(State.WAIT);
        System.out.println("加入等待队列："+request.toString());
        System.out.println("等待队列长度："+waitRequestList.size());
    }

    public boolean removeRequest(int customId){
        //将请求移出等待队列
        for (Request req:waitRequestList){
            if(customId==req.getCustomId()){
                waitRequestList.remove(req);
                System.out.println("移出等待队列："+req.toString());
                System.out.println("等待队列长度："+waitRequestList.size());
                return true;
            }
        }
        return false;
    }

    public Request findRequest(int customId){
        for (Request req:waitRequestList){
            if(customId==req.getCustomId()){
                return req;
            }
        }
        return null;
    }

    public boolean changeRequestTemp(int customId, double temp){
        //修改等待队列中某请求的目标温度
        for (Request req:waitRequestList){
            if(customId==req.getCustomId()){
                req.setTargetTemp(temp);
                Room room = roomList.findRoom(customId);
                logDao.storeLog(new Log(req.getCustomId(),req.getRoomId(), ScheduleType.CHANGE_FAN_SPEED, req.getTargetMode(), req.getFanSpeed(), room.getCurrentTemp(), req.getTargetTemp(), room.getFee(),getFeeRate(req.getFanSpeed())));
                return true;
            }
        }
        return false;
    }

    public boolean changeRequestFanSpeed(int customId, FanSpeed fanSpeed){
        //修改等待队列中某请求的风速
        for (Request req:waitRequestList){
            if(customId==req.getCustomId()){
                req.setFanSpeed(fanSpeed);
                Room room = roomList.findRoom(customId);
                logDao.storeLog(new Log(req.getCustomId(),req.getRoomId(), ScheduleType.CHANGE_FAN_SPEED, req.getTargetMode(), req.getFanSpeed(), room.getCurrentTemp(), req.getTargetTemp(), room.getFee(),getFeeRate(req.getFanSpeed())));
                return true;
            }
        }
        return false;
    }

    public Request getFastestFanSpeedRequest(){
        //返回等待队列中风速最快的请求
        if (0==waitRequestList.size())
            return null;
        Request best=waitRequestList.get(0);
        for (Request req:waitRequestList){
            if(req.getFanSpeed().compareTo(best.getFanSpeed())>0){
                best=req;
            }
        }
        return best;
    }

    public double getFeeRate(FanSpeed fanSpeed){
        switch (fanSpeed.ordinal()){
            case 0: return FEE_RATE_LOW;
            case 1: return FEE_RATE_MID;
            default:return FEE_RATE_HIGH;
        }
    }
}
