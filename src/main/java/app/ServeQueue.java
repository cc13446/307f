package app;

import java.io.IOException;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import Dao.LogDao;
import Domain.Log;
import Enum.*;
import Listener.MyEventListener;

public class ServeQueue {

    public List<Request> serveRequestList;
    public List<Servant> servantList;
    private Scheduler scheduler;
    private final double FEE_RATE_HIGH;
    private final double FEE_RATE_MID;
    private final double FEE_RATE_LOW;

    public ServeQueue(double FEE_RATE_HIGH, double FEE_RATE_MID, double FEE_RATE_LOW, Scheduler scheduler) {
        serveRequestList=new LinkedList<Request>();
        servantList = new LinkedList<Servant>();
        this.FEE_RATE_HIGH = FEE_RATE_HIGH;
        this.FEE_RATE_MID = FEE_RATE_MID;
        this.FEE_RATE_LOW = FEE_RATE_LOW;
        this.scheduler = scheduler;
    }

    public int size(){
        return serveRequestList.size();
    }

    public void addRequest(Request request, LogDao logDao, RoomList roomList){
        serveRequestList.add(request);
        roomList.findRoom(request.getCustomId()).setState(State.SERVE);
        Servant servant = new Servant(FEE_RATE_HIGH, FEE_RATE_MID, FEE_RATE_LOW, request, logDao, roomList);
        servant.setListener(new MyEventListener() {
            @Override
            public void handleEvent(EventObject event) {
                servantList.remove(servant);
                serveRequestList.remove(request);
                HoldOnQueue.addRequst(request);
                scheduler.schedule();
            }
        });
        servant.setState(State.ON);
        servantList.add(servant);

        try {
            servant.beginServe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("加入服务队列："+request.toString());
        System.out.println("服务队列长度："+serveRequestList.size());
    }

    public boolean removeRequest(int customId){
        for (Request req:serveRequestList){
            if(customId==req.getCustomId()){
                serveRequestList.remove(req);
                Servant servant = findServant(req.getCustomId());
                if(servant == null){
                    return false;
                }
                servant.setState(State.OFF);
                servantList.remove(servant);
                System.out.println("移出服务队列："+req.toString());
                System.out.println("服务队列长度："+serveRequestList.size());
                return true;
            }
        }
        return false;
    }

    public Request getSlowestFanSpeedRequest(){
        if (0==serveRequestList.size())
            return null;
        Request worst=serveRequestList.get(0);
        for (Request req:serveRequestList){
            if(req.getFanSpeed().compareTo(worst.getFanSpeed())<0){
                worst=req;
            }
        }
        return worst;
    }


    public Request findRequest(int customId){
        for (Request req:serveRequestList){
            if(customId==req.getCustomId()){
                return req;
            }
        }
        return null;
    }
    public Servant findServant(int customId){
        for (Servant s:servantList){
            if(customId==s.getCustomId()){
                return s;
            }
        }
        return null;
    }

    public boolean changeRequestTemp(int customId, double temp){
        for (Request req:serveRequestList){
            if(customId==req.getCustomId()){
                req.setTargetTemp(temp);
                return true;
            }
        }
        return false;
    }

    public boolean changeRequestFanSpeed(int customId, FanSpeed fanSpeed){
        Servant servant = findServant(customId);
        if(servant == null){
            return false;
        }
        Request request = servant.getRequest();
        request.setFanSpeed(fanSpeed);
        switch (request.getFanSpeed().ordinal()){
            case 0: servant.setFeeRate(FEE_RATE_LOW);break;
            case 1: servant.setFeeRate(FEE_RATE_MID);break;
            case 2: servant.setFeeRate(FEE_RATE_HIGH);break;
        }
        return true;
    }
}
