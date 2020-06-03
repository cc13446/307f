package app;

import java.util.LinkedList;
import java.util.List;
import Enum.*;

public class ServeQueue {

    public List<Request> serveRequestList;

    public ServeQueue(){
        serveRequestList=new LinkedList<Request>();
    }

    public int size(){
        return serveRequestList.size();
    }

    public void addRequest(Request request){
        serveRequestList.add(request);
        System.out.println("加入服务队列："+request.toString());
        System.out.println("服务队列长度："+serveRequestList.size());
    }

    public boolean removeRequest(int roomId){
        for (Request req:serveRequestList){
            if(roomId==req.getRoomId()){
                serveRequestList.remove(req);
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

    public void replaceRequest(Request request){
        int outReqId=getSlowestFanSpeedRequest().getRoomId();
        removeRequest(outReqId);
        addRequest(request);
    }

    public Request findRequest(int roomId){
        for (Request req:serveRequestList){
            if(roomId==req.getRoomId()){
                return req;
            }
        }
        return null;
    }

    public boolean changeRequestTemp(int roomId, double temp){
        for (Request req:serveRequestList){
            if(roomId==req.getRoomId()){
                req.setTargetTemp(temp);
                return true;
            }
        }
        return false;
    }

    public boolean changeRequestFanSpeed(int roomId, FanSpeed fanSpeed){
        for (Request req:serveRequestList){
            if(roomId==req.getRoomId()){
                req.setFanSpeed(fanSpeed);
                return true;
            }
        }
        return false;
    }
}
