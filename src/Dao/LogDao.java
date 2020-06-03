package Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Domain.Log;
import Enum.*;
public class LogDao {
    public boolean storeLog(Log log){
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

    //TODO Change Type
    public List<Log> QueryLog(int roomId, Date dateIn, Date dateOut){
        return new ArrayList<Log>();
    }

}