package Controller;

import app.Report;
import app.ReportForm;
import app.ReportServant;
import Enum.*;

import java.util.ArrayList;
import java.util.Date;

public class PrintReportController {
    private ReportServant reportServant = new ReportServant(new Report());//报表服务对象


    //构造方法
    public PrintReportController() {
    }

    //生成报表对象
    public ArrayList<ReportForm> QueryReport(ArrayList<Integer> listRoomId, TypeReport typeReport, Date date) {
        reportServant = new ReportServant(new Report(typeReport));
        return reportServant.QueryReport(listRoomId, date);
    }

    //将报表打印到文件中
    public boolean PrintReport() {
        return reportServant.PrintReport();
    }

    //get方法
    public ReportServant getReportServant() {
        return reportServant;
    }

    //set方法
    public void setReportServant(ReportServant reportServant) {
        this.reportServant = reportServant;
    }

//    //main函数
//    public static void main(String args[]) {
//        PrintReportController printReportController = new PrintReportController();
//        ArrayList<Integer> listRoomId = new ArrayList<>();
//        for (int i = 101; i < 120; i++)
//            listRoomId.add(i);
//
//        printReportController.QueryReport(listRoomId, TypeReport.ANNUAL, new Date());
//        if (!printReportController.PrintReport())
//            System.out.println("error");
//    }
}
