package app;

import java.util.LinkedList;
import java.util.List;
import Enum.*;

public class WaitQueue {

    public List<Request> waitRequestList;
    public RoomList roomList;
    public WaitQueue(RoomList roomList){
        waitRequestList=new LinkedList<Request>();
        this.roomList = roomList;
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
                return true;
            }
        }
        return false;
    }

    public boolean changeRequestFanSpeed(int customId, FanSpeed fanSpeed){
        for (Request req:waitRequestList){
            if(customId==req.getCustomId()){
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
