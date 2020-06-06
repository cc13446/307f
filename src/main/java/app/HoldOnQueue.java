package app;

import Dao.LogDao;
import Domain.Log;
import Enum.*;
import java.util.LinkedList;
import java.util.List;

public class HoldOnQueue {
    public  List<Request> holdOnRequestList;
    private final double FEE_RATE_HIGH;
    private final double FEE_RATE_MID;
    private final double FEE_RATE_LOW;
    private LogDao logDao;
    public RoomList roomList;

    public HoldOnQueue(double FEE_RATE_HIGH, double FEE_RATE_MID, double FEE_RATE_LOW, LogDao logDao, RoomList roomList) {
        this.FEE_RATE_HIGH = FEE_RATE_HIGH;
        this.FEE_RATE_MID = FEE_RATE_MID;
        this.FEE_RATE_LOW = FEE_RATE_LOW;
        this.logDao = logDao;
        this.roomList = roomList;
        holdOnRequestList = new LinkedList<Request>();
    }

    public Request findReqeust(int customId){
        for (Request req:holdOnRequestList){
            if(customId==req.getCustomId()){
                return req;
            }
        }
        return null;
    }
    public  boolean addRequest(Request request){
        holdOnRequestList.add(request);
        return true;
    }
    public Request removeRequest(int customId){
        Request req =  findReqeust(customId);
        holdOnRequestList.remove(req);
        return req;
    }
    public boolean changeRequestTemp(int customId, double temp){
        for (Request req:holdOnRequestList){
            if(customId==req.getCustomId()){
                req.setTargetTemp(temp);
                Room room = roomList.findRoom(customId);
                logDao.storeLog(new Log(req.getCustomId(),req.getRoomId(), ScheduleType.CHANGE_TEMP, req.getTargetMode(), req.getFanSpeed(), room.getCurrentTemp(), req.getTargetTemp(), room.getFee(),getFeeRate(req.getFanSpeed())));
                return true;
            }
        }
        return false;
    }

    public boolean changeRequestFanSpeed(int customId, FanSpeed fanSpeed){
        for (Request request:holdOnRequestList) {
            if (customId == request.getCustomId()) {
                request.setFanSpeed(fanSpeed);
                Room room = roomList.findRoom(customId);
                logDao.storeLog(new Log(request.getCustomId(), request.getRoomId(), ScheduleType.CHANGE_FAN_SPEED, request.getTargetMode(), request.getFanSpeed(), room.getCurrentTemp(), request.getTargetTemp(), room.getFee(), getFeeRate(request.getFanSpeed())));
                return true;
            }
        }
        return false;
    }
    public double getFeeRate(FanSpeed fanSpeed){
        switch (fanSpeed.ordinal()){
            case 0: return FEE_RATE_LOW;
            case 1: return FEE_RATE_MID;
            default:return FEE_RATE_HIGH;
        }
    }
}
