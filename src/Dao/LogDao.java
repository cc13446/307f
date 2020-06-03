package Dao;

import java.util.Date;

import Domain.Log;
import Enum.*;

public class LogDao {
    public boolean storeLog(Log log) {
        return true;
    }

    public int QueryTotalFee(int roomId, Date dateIn, Date dateOut) {
        return 1;
    }

    public int QueryTurnTimes(int roomId, Date date) {
        return 1;
    }

    public long QueryUseTime(int roonmId, Date date) {
        return new Date(1000).getTime();
    }

    public int QuerySchedulerTimes(int roomId, Date date) {
        return 1;
    }

    public int QueryDetailNumbers(int roomId, Date date) {
        return 1;
    }

    public int QueryChangeTempTimes(int roomId, Date date) {
        return 1;
    }

    public int QueryChangeFanSpeedTimes(int roomId, Date date) {
        return 1;
    }

    public Log QueryLog(int roomId, Date dateIn, Date dateOut) {
        return new Log();
    }

}