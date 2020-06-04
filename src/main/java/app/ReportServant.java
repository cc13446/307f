package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Dao.LogDao;
import Domain.Log;
import Enum.*;

import java.io.*;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

public class ReportServant {
    private Report report;//报表对象


    //构造方法
    public ReportServant(Report report) {
        this.report = report;
    }

    //向报表对象内存储信息
    public ArrayList<ReportForm> QueryReport(ArrayList<Integer> listRoomId, Date date) {
        //遍历房间号列表，使用LogDao从数据库中查询各个房间的信息
        for (Integer roomId : listRoomId) {
            LogDao logDao = new LogDao();
            ReportForm reportForm = new ReportForm();//报表里面需要的每个房间的信息
            reportForm.setRoomId(roomId);//设置房间ID号

            //日报
            if (report.getTypeReport().equals(TypeReport.DAILY)) {
                //获取当天的数据
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, date, date));
                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, date, date));
                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, date, date));
                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, date, date));
                reportForm.setUseTime(getRoomUseTime(logDao.QueryLog(roomId, date, date), date));
//                reportForm.setDetailBillNumber(logDao.QueryDetailNumbers(roomId, date, date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, date, date));

                report.setDate(date);
                report.addReportForm(reportForm);
            }

            //周报
            if (report.getTypeReport().equals(TypeReport.WEEKLY)) {
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, subDays(date, 6), date));
                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 6), date));
                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 6), date));
                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 6), date));
                reportForm.setUseTime(getRoomUseTime(logDao.QueryLog(roomId, subDays(date, 6), date), date));
//                reportForm.setDetailBillNumber(logDao.QueryDetailNumbers(roomId, subDays(date,6), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date,6), date));

                report.setDate(date);
                report.addReportForm(reportForm);
            }

            //月报
            if (report.getTypeReport().equals(TypeReport.MONTHLY)) {
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, subDays(date, 29), date));
                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 29), date));
                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 29), date));
                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 29), date));
                reportForm.setUseTime(getRoomUseTime(logDao.QueryLog(roomId, subDays(date, 29), date), date));
//                reportForm.setDetailBillNumber(logDao.QueryDetailNumbers(roomId, subDays(date,29), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date,29), date));

                report.setDate(date);
                report.addReportForm(reportForm);
            }

            //年报
            if (report.getTypeReport().equals(TypeReport.ANNUAL)) {
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, subDays(date, 364), date));
                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanTimes(roomId, subDays(date, 364), date));
                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, subDays(date, 364), date));
                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, subDays(date, 364), date));
                reportForm.setUseTime(getRoomUseTime(logDao.QueryLog(roomId, subDays(date, 364), date), date));
//                reportForm.setDetailBillNumber(logDao.QueryDetailNumbers(roomId, subDays(date,364), date));
//                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, subDays(date,364), date));

                report.setDate(date);
                report.addReportForm(reportForm);
            }
        }
        return report.getReportFormList();
    }

    //打印报表，存储到文件，文件名为ReportForm加上当前时间
    public boolean PrintReport() {
        try {
            //打开文件
            WritableWorkbook book = Workbook.createWorkbook(new File("ReportForm" + new SimpleDateFormat(" yyyyMMdd-HHmmss").format(new Date()) + ".xls"));
            //生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("报表", 0);

            //（0,0）处写入报表类型
            Label label = new Label(0, 0, "日报");
            if (report.getTypeReport().equals(TypeReport.DAILY)) {
                label = new Label(0, 0, "日报");
            } else if (report.getTypeReport().equals(TypeReport.WEEKLY)) {
                label = new Label(0, 0, "周报");
            } else if (report.getTypeReport().equals(TypeReport.MONTHLY)) {
                label = new Label(0, 0, "月报");
            } else if (report.getTypeReport().equals(TypeReport.ANNUAL)) {
                label = new Label(0, 0, "年报");
            }
            sheet.addCell(label);//将定义好的单元格添加到工作表中

            //写入报表日期
            sheet.addCell(new Label(0, 1, new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss").format(report.getDate())));

            sheet.addCell(new Label(0, 2, "房间号"));
            sheet.addCell(new Label(1, 2, "房间开关次数"));
            sheet.addCell(new Label(2, 2, "空调使用时长"));
            sheet.addCell(new Label(3, 2, "总费用"));
            sheet.addCell(new Label(4, 2, "被调度的次数"));
            sheet.addCell(new Label(5, 2, "详单数"));
            sheet.addCell(new Label(6, 2, "调温次数"));
            sheet.addCell(new Label(7, 2, "调风次数"));

            //写入各个房间的报表信息
            ReportForm reportForm;
            for (int i = 0; i < report.getReportFormList().size(); i++) {
                reportForm = report.getReportFormList().get(i);
                sheet.addCell(new jxl.write.Number(0, 3 + i, reportForm.getRoomId()));
                sheet.addCell(new jxl.write.Number(1, 3 + i, reportForm.getTurnTimes()));
                sheet.addCell(new Label(2, 3 + i, reportForm.getStringUseTime()));
                sheet.addCell(new jxl.write.Number(3, 3 + i, reportForm.getTotalFee()));
                sheet.addCell(new jxl.write.Number(4, 3 + i, reportForm.getSchedulerTimes()));
                sheet.addCell(new jxl.write.Number(5, 3 + i, reportForm.getDetailBillNumber()));
                sheet.addCell(new jxl.write.Number(6, 3 + i, reportForm.getChangeTempTimes()));
                sheet.addCell(new jxl.write.Number(7, 3 + i, reportForm.getChangeFanSpeedTimes()));
            }

            //写入数据并关闭文件
            book.write();
            book.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    //获取前days天的date
    public Date subDays(Date date, int days) {
        Date d = new Date(date.getTime() - days * 24 * 3600 * 1000);
        return d;
    }

    //从logList中获取房间的空调使用时间
    public long getRoomUseTime(ArrayList<Log> logList, Date date) {
        int flag = 0;//flag为1表示已开机，flag为0表示已关机
        //对list里面的元素按照日期由早到晚排序
        Collections.sort(logList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Log c1 = (Log) o1;
                Log c2 = (Log) o2;
                if (c1.getTime().getTime() > c2.getTime().getTime()) {
                    return 1;
                } else if (c1.getTime().getTime() == c2.getTime().getTime()) {
                    return 0;
                }
                return -1;
            }
        });
        long useTime = 0;//使用时间
        long startTime = 0, endTime = 0;//开始时间和结束时间
        Log log = new Log();
        //遍历log列表
        for (int i = 0; i < logList.size(); i++) {
            log = logList.get(i);
            //如果空调处于关机状态且遇到开机请求
            if (log.getScheduleType().equals(ScheduleType.NEW_REQUEST) && flag == 0) {
                flag = 1;
                startTime = log.getTime().getTime();
            }

            //如果空调处于开机状态且遇到关机请求
            if (log.getScheduleType().equals(ScheduleType.CLOSE) && flag == 1) {
                flag = 0;
                endTime = log.getTime().getTime();
                useTime += endTime - startTime;
            }
        }
        //如果结束时空调处于开机状态，则认为空调到截止时间仍未关机，计算到截止时间的总时长
        if (flag == 1)
            if (date.getTime() - startTime >= 0)
                useTime += date.getTime() - startTime;

        return useTime;
    }
}
