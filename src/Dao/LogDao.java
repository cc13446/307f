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
import org.junit.Test;
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
 * 1. NEW_REQUEST
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

    @Test
    public void test() {
        // 1. 存储测试
//        for(int i = 3; i < 8; i++) {
//            Log log = new Log(1, ScheduleType.CLOSE, Mode.COLD, FanSpeed.HIGH, 23.2, 30.2, i, 1.4);
//            storeLog(log);
//        }

        // 2. 查询测试
//        ArrayList<Log> temp = QueryLog(1, format.parse("2020-06-03 10:41:00"), format.parse("2020-06-03 11:07:23"));
//        for(Log l : temp){
//            System.out.println(l);
//        }

        // 3. 查询总费用测试
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(QueryTotalFee(1, format.parse("2020-06-03 10:41:00"), format.parse("2020-06-03 11:07:23")));

        // 4. 查询开关次数
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(QueryTurnTimes(1, format.parse("2020-06-03 10:41:00"), format.parse("2020-06-03 11:07:23")));

    }

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

    public Date QueryUseTime(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Log where roomId = ?1 and time >= ?2 and time <= ?3 and (scheduleType = ?4 or scheduleType = ?5) order by time");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CLOSE);
        query.setParameter(5, ScheduleType.NEW_REQUEST);
        System.out.println(query.list());
        tx.commit();
        session.close();
        return new Date();
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
}