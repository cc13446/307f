package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Dao.LogDao;
import Enum.*;

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
                reportForm.setChangeFanSpeedTimes(logDao.QueryChangeFanSpeedTimes(roomId, date));
                reportForm.setChangeTempTimes(logDao.QueryChangeTempTimes(roomId, date));
                reportForm.setDetailBillNumber(logDao.QueryDetailNumbers(roomId, date));
                reportForm.setSchedulerTimes(logDao.QuerySchedulerTimes(roomId, date));
                reportForm.setTurnTimes(logDao.QueryTurnTimes(roomId, date));
                reportForm.setUseTime(logDao.QueryUseTime(roomId, date));

                report.setDate(date);
                report.addReportForm(reportForm);
            }

            //周报
            if (report.getTypeReport().equals(TypeReport.WEEKLY)) {
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, date, date));//总费用直接获得
                //其他信息只能按日期查询，循环，一共计算7天的数据之和
                for (Date durDate = date; (date.getTime() - durDate.getTime()) / (24 * 3600 * 1000) < 7; durDate = subOneDay(durDate)) {
                    reportForm.setChangeFanSpeedTimes(reportForm.getChangeFanSpeedTimes() + logDao.QueryChangeFanSpeedTimes(roomId, date));
                    reportForm.setChangeTempTimes(reportForm.getChangeTempTimes() + logDao.QueryChangeTempTimes(roomId, date));
                    reportForm.setDetailBillNumber(reportForm.getDetailBillNumber() + logDao.QueryDetailNumbers(roomId, date));
                    reportForm.setSchedulerTimes(reportForm.getSchedulerTimes() + logDao.QuerySchedulerTimes(roomId, date));
                    reportForm.setTurnTimes(reportForm.getTurnTimes() + logDao.QueryTurnTimes(roomId, date));
                    reportForm.setUseTime(reportForm.getUseTime() + logDao.QueryUseTime(roomId, date));
                }

                report.setDate(date);
                report.addReportForm(reportForm);
            }

            //月报
            if (report.getTypeReport().equals(TypeReport.MONTHLY)) {
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, date, date));//总费用直接获得
                //其他信息只能按日期查询，循环，一共计算30天的数据之和
                for (Date durDate = date; (date.getTime() - durDate.getTime()) / (24 * 3600 * 1000) < 30; durDate = subOneDay(durDate)) {
                    reportForm.setChangeFanSpeedTimes(reportForm.getChangeFanSpeedTimes() + logDao.QueryChangeFanSpeedTimes(roomId, date));
                    reportForm.setChangeTempTimes(reportForm.getChangeTempTimes() + logDao.QueryChangeTempTimes(roomId, date));
                    reportForm.setDetailBillNumber(reportForm.getDetailBillNumber() + logDao.QueryDetailNumbers(roomId, date));
                    reportForm.setSchedulerTimes(reportForm.getSchedulerTimes() + logDao.QuerySchedulerTimes(roomId, date));
                    reportForm.setTurnTimes(reportForm.getTurnTimes() + logDao.QueryTurnTimes(roomId, date));
                    reportForm.setUseTime(reportForm.getUseTime() + logDao.QueryUseTime(roomId, date));
                }

                report.setDate(date);
                report.addReportForm(reportForm);
            }

            //年报
            if (report.getTypeReport().equals(TypeReport.ANNUAL)) {
                reportForm.setTotalFee(logDao.QueryTotalFee(roomId, date, date));//总费用直接获得
                //其他信息只能按日期查询，循环，一共计算365天的数据之和
                for (Date durDate = date; (date.getTime() - durDate.getTime()) / (24 * 3600 * 1000) < 365; durDate = subOneDay(durDate)) {
                    reportForm.setChangeFanSpeedTimes(reportForm.getChangeFanSpeedTimes() + logDao.QueryChangeFanSpeedTimes(roomId, date));
                    reportForm.setChangeTempTimes(reportForm.getChangeTempTimes() + logDao.QueryChangeTempTimes(roomId, date));
                    reportForm.setDetailBillNumber(reportForm.getDetailBillNumber() + logDao.QueryDetailNumbers(roomId, date));
                    reportForm.setSchedulerTimes(reportForm.getSchedulerTimes() + logDao.QuerySchedulerTimes(roomId, date));
                    reportForm.setTurnTimes(reportForm.getTurnTimes() + logDao.QueryTurnTimes(roomId, date));
                    reportForm.setUseTime(reportForm.getUseTime() + logDao.QueryUseTime(roomId, date));
                }

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
            File file = new File("ReportForm" + new SimpleDateFormat(" yyyyMMdd-HHmmss").format(new Date()) + ".txt");

            //如果文件不存在则创建
            if (!file.exists()) {
                file.createNewFile();
            }

            //覆盖写
            FileWriter fileWritter = new FileWriter(file.getName(), false);

            //写入报表类型
            if (report.getTypeReport().equals(TypeReport.DAILY)) {
                fileWritter.write("日报\n");
                System.out.println("日报");
            } else if (report.getTypeReport().equals(TypeReport.WEEKLY)) {
                fileWritter.write("周报\n");
                System.out.println("周报");
            } else if (report.getTypeReport().equals(TypeReport.MONTHLY)) {
                fileWritter.write("月报\n");
                System.out.println("月报");
            } else if (report.getTypeReport().equals(TypeReport.ANNUAL)) {
                fileWritter.write("年报\n");
                System.out.println("年报");
            }
            //写入报表日期
            System.out.println(report.getDate().toString());
            fileWritter.write(report.getDate().toString() + "\n");
            //写入各个房间的报表信息
            for (ReportForm reportForm : report.getReportFormList()) {
                System.out.println(reportForm.toString());
                fileWritter.write(reportForm.toString() + "\n");
            }
            fileWritter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    //返回前一天的date
    public Date subOneDay(Date date) {
        Date d = new Date(date.getTime() - 24 * 3600 * 1000);
        return d;
    }
}
