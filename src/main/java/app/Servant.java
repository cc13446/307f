package app;
import Domain.Log;
import Domain.Request;
import Domain.Room;
import Enum.*;
import Dao.LogDao;
import Listener.MyEventListener;
import Listener.MyEventObject;

public class Servant{

    private Request request;
    private State state;
    private Room room;
    private double fee;
    private double feeRate;
    private LogDao logDao;
    private RoomList roomList;

    private MyEventListener Listener;
    //注册监听器
    public Servant(double FEE_RATE_HIGH, double FEE_RATE_MID, double FEE_RATE_LOW, Request request, LogDao logDao, RoomList roomList) {

        this.request = request;
        this.state = State.ON;
        this.roomList = roomList;
        this.logDao = logDao;
        this.room = roomList.findRoom(request.getCustomId());
        this.fee = room.getFee();
        switch (request.getFanSpeed().ordinal()){
            case 0: feeRate = FEE_RATE_LOW;break;
            case 1: feeRate = FEE_RATE_MID;break;
            case 2: feeRate = FEE_RATE_HIGH;break;
        }
    }


    public MyEventListener getListener() {
        return Listener;
    }

    public void setListener(MyEventListener listener) {
        Listener = listener;
    }

    //接受外部事件
    public void notifyListenerEvents(MyEventObject event){
        Listener.handleEvent(event);
    }
    public int getCustomId(){
        return request.getCustomId();
    }

    public Request getRequest() {
        return request;
    }


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(double feeRate) {
        this.feeRate = feeRate;
    }

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

    public RoomList getRoomList() {
        return roomList;
    }

    public void setRoomList(RoomList roomList) {
        this.roomList = roomList;
    }


    public boolean beginServe() throws InterruptedException {
        this.state = State.ON;
        if(!storeLog(ScheduleType.OPEN)){
            return false;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(state == State.ON){
                        Thread.sleep(60000/80);
                        changeFee(80);
                        room.setDuration(room.getDuration()+60000/80);
                        if (Math.abs(request.getTargetTemp() - room.getCurrentTemp()) < 0.1){
                            room.setState(State.HOLDON);
                            if(endServe()) {
                                notifyListenerEvents(new MyEventObject(request.getCustomId()));
                                return;
                            }

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return true;
    }

    public Boolean endServe()
    {
        this.state = State.OFF;
        storeLog(ScheduleType.CLOSE);
        return true;
    }


    private boolean storeLog(ScheduleType scheduleType){
        Log log = new Log(request.getCustomId(),request.getRoomId(), scheduleType, request.getTargetMode(), request.getFanSpeed(), room.getCurrentTemp(), request.getTargetTemp(), fee, feeRate);
        return logDao.storeLog(log);
    }

    private void changeFee(int divide){
        fee += feeRate/divide;
        room.setFee(fee);
    }

}
