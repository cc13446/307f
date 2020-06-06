package app;

import java.util.LinkedList;
import java.util.List;

import Dao.LogDao;
import Domain.Log;
import Enum.*;

public class WaitQueue {

    private final double FEE_RATE_HIGH;
    private final double FEE_RATE_MID;
    private final double FEE_RATE_LOW;
    private LogDao logDao;

    public List<Request> waitRequestList;
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
        waitRequestList.add(request);
        System.out.println(roomList.findRoom(request.getCustomId()));
        roomList.findRoom(request.getCustomId()).setState(State.WAIT);
        System.out.println("加入等待队列："+request.toString());
        System.out.println("等待队列长度："+waitRequestList.size());
    }

    public boolean removeRequest(int customId){
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
