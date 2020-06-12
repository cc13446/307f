package Controller;

import Dao.LogDao;
import Domain.Report;
import Domain.ReportForm;
import app.ReportServant;
import Enum.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PrintReportController {
    private LogDao logDao;


    //构造方法
    public PrintReportController(LogDao logDao) {
        this.logDao = logDao;
    }

    //生成报表对象
    public List<ReportForm> QueryReport(LinkedList<Integer> listRoomId, Report report) {
        ReportServant reportServant = new ReportServant(report,logDao);
        return reportServant.QueryReport(listRoomId);
    }



}
