package Dao;

import java.util.Date;

enum ScheduleType {
    NEW_REQUEST, CHANGE_TEMP, CHANGE_FAN_SPEED, CLOSE
}
enum Mode {
    HOT, COLD, FAN
}
enum FanSpeed {
    LOW, MEDIUM, HIGH
}
public class LogDao {
    public boolean storeLog(int roomId, ScheduleType scheduleType, Mode mode, FanSpeed fanSpeed, double current,
                     double targetTemp, double fee, double feeRate){
        return true;
    }

    public int QueryTotalFee(int roomId, Date dateIn, Date dateOut){
        return 1;
    }

    public int QueryTurnTimes(int roomId, Date date){
        return 1;
    }

    public Date QueryUseTime(int roonmId, Date date){
        return new Date();
    }

    public int QuerySchedulerTimes(int roomId, Date date){
        return 1;
    }

    public int QueryDetailNumbers(int roomId, Date date){
        return 1;
    }

    public int QueryChangeTempTimes(int roomId, Date date){
        return 1;
    }

    public int QueryChangeFanTimes(int roomId, Date date){
        return 1;
    }

    public int QueryLog(int roomId, Date dateIn, Date dateOut){
        return 1;
    }

}