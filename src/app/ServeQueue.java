package app;

import java.util.LinkedList;
import java.util.List;

public class ServeQueue {

    public List<Request> serveRequestList;

    public ServeQueue(){
        serveRequestList=new LinkedList<Request>();
        Request request=new Request(1,35,FanSpeed.LOW,1000,Mode.FAN);
    }

    public int size(){
        return serveRequestList.size();
    }

    public void addRequest(Request request){
        serveRequestList.add(request);
    }

    public boolean removeRequest(int roomId){
        for (Request req:serveRequestList){
            if(roomId==req.getRoomId()){
                serveRequestList.remove(req);
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
        System.out.println(worst.toString());
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
