package Dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Domain.Log;
import Enum.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateUtils;
/*
 * 有关日志的说明：
 * 在日志记录中，日志中调度类型代表着这一条记录发生的事件
 * NEW_REQUEST：空调开启
 * CHANGE_TEMP：仅仅表示温度改变
 * CHANGE_FAN_SPEED：仅仅表示风速改变
 * CLOSE：空调关闭
 *
 * 调度类型举例
 * 1. OPEN
 * 2. CHANGE_TEMP
 * 3. CLOSE
 * 代表：
 * 1时刻空调开启
 * 2时刻空调的目标温度改变
 * 3时刻空调关闭
 * 在计算详单时，应该将其划分为12,23两个阶段
 * 同时2时刻记录的温度和费用为2时刻当时的温度和费用，其他属性都为更改温度之后的属性。
 */
public class LogDao {

    public boolean storeLog(Log log){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        //save方法会返回id
        System.out.println(new Date());
        log.setTime(new Date());
        Serializable id = session.save(log);
        System.out.println(id);
        tx.commit();
        session.close();
        return true;
    }
    public double QueryTotalFee(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select sum(fee) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and scheduleType = ?4");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CLOSE);
        double result = (double)query.list().get(0);
        tx.commit();
        session.close();
        return result;
    }

    public int QueryTurnTimes(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and scheduleType = ?4");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CLOSE);
        Long result = (Long)query.list().get(0);
        tx.commit();
        session.close();
        return result.intValue();
    }

    public int QuerySchedulTimes(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and (scheduleType = ?4 or scheduleType = ?5) order by time");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CLOSE);
        query.setParameter(5, ScheduleType.NEW_REQUEST);
        Long result = (Long)query.list().get(0);
        tx.commit();
        session.close();
        return result.intValue();
    }

    public int QueryChangeTempTimes(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and scheduleType = ?4");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CHANGE_TEMP);
        Long result = (Long)query.list().get(0);
        tx.commit();
        session.close();
        return result.intValue();
    }

    public int QueryChangeFanTimes(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and scheduleType = ?4");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CHANGE_FAN_SPEED);
        Long result = (Long)query.list().get(0);
        tx.commit();
        session.close();
        return result.intValue();
    }
    @SuppressWarnings("unchecked")
    public ArrayList<Log> QueryLog(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Log where roomId = ?1 and time >= ?2 and time <= ?3");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        List<Log> list = query.list();
        tx.commit();
        session.close();
        return (ArrayList<Log>)list;
    }
    @SuppressWarnings("unchecked")
    public ArrayList<Log> QueryLog(int customId){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Log where customId = ?1");
        query.setParameter(1, customId);
        List<Log> list = query.list();
        tx.commit();
        session.close();
        return (ArrayList<Log>)list;
    }
}