package app;

import java.util.LinkedList;
import java.util.List;
import org.junit.*;
import Enum.*;

public class WaitQueue {

    public List<Request> waitRequestList;

    public WaitQueue(){
        waitRequestList=new LinkedList<Request>();
    }

    public int size(){
        return waitRequestList.size();
    }

    public void addRequest(Request request){
        waitRequestList.add(request);
        System.out.println("加入等待队列："+request.toString());
        System.out.println("等待队列长度："+waitRequestList.size());
    }

    public boolean removeRequest(int roomId){
        for (Request req:waitRequestList){
            if(roomId==req.getRoomId()){
                waitRequestList.remove(req);
                System.out.println("移出等待队列："+req.toString());
                System.out.println("等待队列长度："+waitRequestList.size());
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
        return best;
    }
}
