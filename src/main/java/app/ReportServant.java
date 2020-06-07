package app;

import java.util.*;

import Dao.LogDao;
import Domain.Log;
import Domain.Report;
import Domain.ReportForm;
import Enum.*;

public class ReportServant {
    private LogDao logDao;
    private Report report;//报表对象


    //构造方法
    public ReportServant(Report report, LogDao logDao) {
        this.report = report;
        this.logDao = logDao;
    }

    //向报表对象内存储信息
    public LinkedList<ReportForm> QueryReport(LinkedList<Integer> listRoomId) {
        LinkedList<ReportForm> ReportFormList = new LinkedList<>();
        //遍历房间号列表，使用LogDao从数据库中查询各个房间的信息
        Date date = report.getDate();
        for (Integer roomId : listRoomId) {
            ReportForm reportForm = new ReportForm();//报表里面需要的每个房间的信息
            reportForm.setRoomId(roomId);//设置房间ID号
            ReportFormList.add(reportForm);
//            //日报
//            if (report.getTypeReport().equals(TypeReport.DAILY)) {
//                //获取当天的数据
//                reportForm.setTotalFee(logDao.QueryTotalFee(roomId,subDays(date, 1), date));
//                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 1), date));
//                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 1), date));
//                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 1), date));
//                reportForm.setUseTime(getRoomUseTime(logDao.QueryOnOffLog(roomId, subDays(date, 1), date)));
//                reportForm.setCustomNumber(logDao.QueryCustomNumber(roomId, subDays(date, 1), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date, 1), date));
//
//                ReportFormList.add(reportForm);
//            }
//
//            //周报
//            if (report.getTypeReport().equals(TypeReport.WEEKLY)) {
//                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, subDays(date, 7), date));
//                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 7), date));
//                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 7), date));
//                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 7), date));
//                reportForm.setUseTime(getRoomUseTime(logDao.QueryOnOffLog(roomId, subDays(date, 7), date)));
//                reportForm.setCustomNumber(logDao.QueryCustomNumber(roomId, subDays(date,7), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date,7), date));
//
//                ReportFormList.add(reportForm);
//            }
//
//            //月报
//            if (report.getTypeReport().equals(TypeReport.MONTHLY)) {
//                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, subDays(date, 30), date));
//                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 30), date));
//                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 30), date));
//                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 30), date));
//                reportForm.setUseTime(getRoomUseTime(logDao.QueryOnOffLog(roomId, subDays(date, 30), date)));
//                reportForm.setCustomNumber(logDao.QueryCustomNumber(roomId, subDays(date,30), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date,30), date));
//
//                ReportFormList.add(reportForm);
//            }
//
//            //年报
//            if (report.getTypeReport().equals(TypeReport.ANNUAL)) {
//                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, subDays(date, 365), date));
//                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 365), date));
//                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 365), date));
//                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 365), date));
//                reportForm.setUseTime(getRoomUseTime(logDao.QueryOnOffLog(roomId, subDays(date, 365), date)));
//                reportForm.setCustomNumber(logDao.QueryCustomNumber(roomId, subDays(date,365), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date,365), date));
//
//                ReportFormList.add(reportForm);
//            }
        }
        return ReportFormList;
    }


    //获取前days天的date
    private Date subDays(Date date, int days) {
        return new Date(date.getTime() - days * 24 * 3600 * 1000);
    }

    private long getRoomUseTime(LinkedList<Log> logList){
        if(logList.get(0).getScheduleType() == ScheduleType.REQUEST_OFF){
            logList.remove(0);
        }
        if(logList.get(logList.size() - 1).getScheduleType() == ScheduleType.REQUEST_ON){
            logList.remove(logList.size() - 1);
        }
        long time = 0;
        for(int i = 0; i < logList.size() - 1;){
            if(logList.get(i).getScheduleType() == ScheduleType.REQUEST_ON && logList.get(i + 1).getScheduleType() == ScheduleType.REQUEST_OFF){

                time += (logList.get(i + 1).getTime().getTime() - logList.get(i).getTime().getTime());
                i += 2;
            }
            else{
                i++;
            }
        }
        return time;
    }

}
