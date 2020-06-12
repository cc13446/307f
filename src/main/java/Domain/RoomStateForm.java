package Domain;

import java.io.Serializable;

/*
 *  房间状态对象的格式，用于格式化传递对象
 *  最后更新时间：2020/6/5 23:11
 */

public class RoomStateForm implements Serializable {
    //房间号
    public int roomID;
    //唯一标识服务ID
    public int customerID;
    //房间状态
    public int state;
    //当前温度
    public double currentTemp;
    //目标温度
    public double targetTemp;
    //风速
    public int fanSpeed;
    //费率
    public double feeRate;
    //当前费用
    public double fee;
    //当前服务的总时长
    public long duration;

    public RoomStateForm(int customerID,int roomId,int state, double fee, double currentTemp, double targetTemp, double feeRate, long duration, int fanSpeed) {
        this.state = state;
        this.fee = fee;
        this.currentTemp = currentTemp;
        this.targetTemp = targetTemp;
        this.feeRate = feeRate;
        this.duration = duration;
        this.fanSpeed = fanSpeed;
        this.customerID=customerID;
        this.roomID=roomId;
    }

    @Override
    public String toString() {
        return "RoomStateForm{" +
                "state=" + state +
                ", currentTemp=" + currentTemp +
                ", targetTemp=" + targetTemp +
                ", fanSpeed=" + fanSpeed +
                ", feeRate=" + feeRate +
                ", fee=" + fee +
                ", duration=" + duration +
                '}';
    }
}
