package Dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
    // 存储一条日志
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
    // 查询日期内的总钱数
    public double QueryTotalFee(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select sum(fee) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and scheduleType = ?4");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.REQUEST_OFF);
        double result = (double)query.list().get(0);
        tx.commit();
        session.close();
        return result;
    }
    // 查询日期内的总开关数
    public int QueryTurnTimes(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and scheduleType = ?4");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.REQUEST_OFF);
        Long result = (Long)query.list().get(0);
        tx.commit();
        session.close();
        return result.intValue();
    }
    // 查询日期内的调度次数
    public int QuerySchedulerTimes(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Log where roomId = ?1 and time >= ?2 and time <= ?3 and (scheduleType = ?4 or scheduleType = ?5) order by time");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.CLOSE);
        query.setParameter(5, ScheduleType.OPEN);
        Long result = (Long)query.list().get(0);
        System.out.println(result);
        tx.commit();
        session.close();
        return result.intValue();
    }
    // 查询房间日期内的改变温度次数
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
    // 查询房间日期内的改变风速次数
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
    // 查询所有开关的日志，计算使用时间报表时用
    @SuppressWarnings("unchecked")
    public List<Log> QueryOnOffLog(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Log where roomId = ?1 and time >= ?2 and time <= ?3 and (scheduleType = ?4 or scheduleType = ?5) order by time");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        query.setParameter(4, ScheduleType.REQUEST_ON);
        query.setParameter(5, ScheduleType.REQUEST_OFF);
        List<Log> list = query.list();
        tx.commit();
        session.close();
        return list;
    }
    // 查询组这个房间的客户数量
    @SuppressWarnings("unchecked")
    public int QueryCustomNumber(int roomId, Date dateIn, Date dateOut){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select distinct customId from Log where roomId = ?1 and time >= ?2 and time <= ?3");
        query.setParameter(1, roomId);
        query.setParameter(2, dateIn);
        query.setParameter(3, dateOut);
        int result = query.list().size();
        System.out.println(result);
        tx.commit();
        session.close();
        return result;
    }
    // 查询一个客户所有日志记录，计算详单时使用
    @SuppressWarnings("unchecked")
    public List<Log> QueryLog(int customId){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Log where customId = ?1 and scheduleType != ?2 and scheduleType != ?3");
        query.setParameter(1, customId);
        query.setParameter(2, ScheduleType.REQUEST_ON);
        query.setParameter(3, ScheduleType.REQUEST_OFF);
        List<Log> list = query.list();
        tx.commit();
        session.close();
        return list;
    }
    // 查询最大的客户ID
    @SuppressWarnings("unchecked")
    public int QueryMaxCustomId(){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select max(customId) from Log ");
        int result = 0;
        List<Integer> list = query.list();
        if(list != null && list.get(0) != null){
            result = list.get(0);
        }
        tx.commit();
        session.close();
        return result;
    }
    // 查询这个房间最后入住的客户ID
    @SuppressWarnings("unchecked")
    public int QueryCustomId(int roomId){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select max(customId) from Log where roomId = ?1 ");
        query.setParameter(1,roomId);
        int result = 0;
        List<Integer> list = query.list();
        if(list != null && list.get(0) != null){
            result = list.get(0);
        }
        tx.commit();
        session.close();
        return result;
    }
    // 查询最近客户的入住日期
    @SuppressWarnings("unchecked")
    public Date QueryRequestDateIn(int customId){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select max(time) from Log where customId = ?1 and scheduleType = ?2");
        query.setParameter(1,customId);
        query.setParameter(2,ScheduleType.REQUEST_ON);
        Date result = null;
        List<Date> list = query.list();
        if(list != null && list.get(0) != null){
            result = list.get(0);
        }
        tx.commit();
        session.close();
        return result;
    }
    // 查询最近客户的退房日期
    @SuppressWarnings("unchecked")
    public Date QueryRequestDateOut(int customId){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select max(time) from Log where customId = ?1 and scheduleType = ?2");
        query.setParameter(1,customId);
        query.setParameter(2,ScheduleType.REQUEST_OFF);
        Date result = null;
        List<Date> list = query.list();
        if(list != null && list.get(0) != null){
            result = list.get(0);
        }
        tx.commit();
        session.close();
        return result;
    }
    // 查询最近客户的总费用
    public double QueryTotalFee(int customId){
        Session session = HibernateUtils.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("select sum(fee) from Log where customId = ?1 and scheduleType = ?2");
        query.setParameter(1, customId);
        query.setParameter(2, ScheduleType.CLOSE);
        double result = (double)query.list().get(0);
        tx.commit();
        session.close();
        return result;
    }
}