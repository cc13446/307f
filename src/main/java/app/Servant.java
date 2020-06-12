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
    private int duration;
    private Boolean RRFlag;
    private Mode mode;

    private MyEventListener Listener;
    //注册监听器
    public Servant(double FEE_RATE_HIGH, double FEE_RATE_MID, double FEE_RATE_LOW, Request request, LogDao logDao, RoomList roomList,Mode mode) {
        this.mode = mode;
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
        duration = 120000;
        RRFlag = false;
    }
    public boolean getRRFlag(){
        return RRFlag;
    }

    public void setRRFlag(boolean RRFlag){
        this.RRFlag = RRFlag;
        duration = 120000;
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

    // 开始服务
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
                        // 关闭服务对象线程退出
                        if(state==State.OFF) return;
                        // 时间片轮转计时
                        if(RRFlag){
                            duration -= 60000/80;
                        }
                        // 计费
                        changeFee(80);
                        // 计时
                        room.setDuration(room.getDuration()+60000/80);
                        // 达到目标温度
                        if ((mode == Mode.HOT && room.getCurrentTemp() >= request.getTargetTemp()) || (mode == Mode.COLD && room.getCurrentTemp() <= request.getTargetTemp())){
                            room.setState(State.HOLDON);
                            if(endServe()) {
                                notifyListenerEvents(new MyEventObject(request.getCustomId()));
                                return;
                            }
                        }
                        // 时间片轮转到时间
                        if(0 == duration && RRFlag){
                            room.setState(State.WAIT);
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
    // 结束服务
    public Boolean endServe()
    {
        this.state = State.OFF;
        storeLog(ScheduleType.CLOSE);
        this.state = room.getState();
        return true;
    }

    // 存储日志
    private boolean storeLog(ScheduleType scheduleType){
        Log log = new Log(request.getCustomId(),request.getRoomId(), scheduleType, request.getTargetMode(), request.getFanSpeed(), room.getCurrentTemp(), request.getTargetTemp(), fee, feeRate);
        return logDao.storeLog(log);
    }
    // 计费
    private void changeFee(int divide){
        fee += feeRate/divide;
        room.setFee(fee);
    }

}
