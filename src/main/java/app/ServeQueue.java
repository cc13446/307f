package app;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import Dao.LogDao;
import Domain.Log;
import Domain.Request;
import Domain.Room;
import Enum.*;
import Listener.MyEventListener;

/*
 *  服务队列，保存被服务那些请求
 *  最后更新时间：2020/06/11 09:00
 */

public class ServeQueue {
    //服务请求队列，保存被服务那些请求
    public List<Request> serveRequestList;
    //服务对象队列，保存服务对象
    public List<Servant> servantList;
    //保存一个调度对象的引用
    private Scheduler scheduler;
    //高中低费率
    private final double FEE_RATE_HIGH;
    private final double FEE_RATE_MID;
    private final double FEE_RATE_LOW;
    //持久化层对象
    private LogDao logDao;
    //保存调度对象中房间列表的引用
    private  RoomList roomList;


    public ServeQueue(double FEE_RATE_HIGH, double FEE_RATE_MID, double FEE_RATE_LOW, Scheduler scheduler,LogDao logDao, RoomList roomList) {
        serveRequestList=new LinkedList<Request>();
        servantList = new LinkedList<Servant>();
        this.FEE_RATE_HIGH = FEE_RATE_HIGH;
        this.FEE_RATE_MID = FEE_RATE_MID;
        this.FEE_RATE_LOW = FEE_RATE_LOW;
        this.scheduler = scheduler;
        this.logDao = logDao;
        this.roomList = roomList;
    }

    public int size(){
        return serveRequestList.size();
    }

    //请求加入服务队列
    public void addRequest(Request request){
        serveRequestList.add(request);
        roomList.findRoom(request.getCustomId()).setState(State.SERVE);
        Servant servant = new Servant(FEE_RATE_HIGH, FEE_RATE_MID, FEE_RATE_LOW, request, logDao, roomList,scheduler.getDefaultMode());
        servant.setListener(new MyEventListener() {
            @Override
            public void handleEvent(EventObject event) {
                //若服务对象为挂起状态，从服务队列移除放入挂起队列
                if(servant.getState() == State.HOLDON){
                    servantList.remove(servant);
                    serveRequestList.remove(request);
                    scheduler.holdOnQueue.addRequest(request);
                }
                //若服务对象为等待状态，从服务队列移除放入等待队列
                else if(servant.getState() == State.WAIT){
                    servantList.remove(servant);
                    serveRequestList.remove(request);
                    scheduler.waitQueue.addRequest(request);
                }
                //触发一次调度
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
        //将请求从服务队列中移出
        for (Request req:serveRequestList){
            if(customId==req.getCustomId()){
                serveRequestList.remove(req);
                Servant servant = findServant(req.getCustomId());
                if(servant == null){
                    return false;
                }
                servant.endServe();
                servantList.remove(servant);
                System.out.println("移出服务队列："+req.toString());
                System.out.println("服务队列长度："+serveRequestList.size());
                return true;
            }
        }
        return false;
    }

    public Request getSlowestFanSpeedRequest(){
        //返服务队列中风速最慢的请求
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
        //改变目标温度
        for (Request req:serveRequestList){
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
        //改变风速,会触发调度，改变费率
        Servant servant = findServant(customId);
        if(servant == null){
            return false;
        }
        Request request = servant.getRequest();
        request.setFanSpeed(fanSpeed);
        servant.setFeeRate(getFeeRate(fanSpeed));
        Room room = roomList.findRoom(customId);
        logDao.storeLog(new Log(request.getCustomId(),request.getRoomId(), ScheduleType.CHANGE_FAN_SPEED, request.getTargetMode(), request.getFanSpeed(), room.getCurrentTemp(), request.getTargetTemp(), room.getFee(),getFeeRate(request.getFanSpeed())));

        return true;
    }

    public double getFeeRate(FanSpeed fanSpeed){
        switch (fanSpeed.ordinal()){
            case 0: return FEE_RATE_LOW;
            case 1: return FEE_RATE_MID;
            default:return FEE_RATE_HIGH;
        }
    }
}
