package app;

import java.util.LinkedList;
import java.util.List;
import org.junit.*;

public class WaitQueue {

    public List<Request> waitRequestList;

    public WaitQueue(){
        waitRequestList=new LinkedList<Request>();
        Request request=new Request(1,35,FanSpeed.LOW,1000,Mode.FAN);
        addRequest(request);
    }

    public int size(){
        return waitRequestList.size();
    }

    public void addRequest(Request request){
        waitRequestList.add(request);
    }

    public boolean removeRequest(int roomId){
        for (Request req:waitRequestList){
            if(roomId==req.getRoomId()){
                waitRequestList.remove(req);
                return true;
            }
        }
        return false;
    }

    public Request findRequest(int roomId){
        for (Request req:waitRequestList){
            if(roomId==req.getRoomId()){
                return req;
            }
        }
        return null;
    }

    public boolean changeRequestTemp(int roomId, double temp){
        for (Request req:waitRequestList){
            if(roomId==req.getRoomId()){
                req.setTargetTemp(temp);
                return true;
            }
        }
        return false;
    }

    public boolean changeRequestFanSpeed(int roomId, FanSpeed fanSpeed){
        for (Request req:waitRequestList){
            if(roomId==req.getRoomId()){
                req.setFanSpeed(fanSpeed);
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
        System.out.println(best.toString());
        return best;
    }
}
