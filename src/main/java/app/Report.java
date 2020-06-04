package app;

import Enum.*;

import java.util.ArrayList;
import java.util.Date;

public class Report {
    private TypeReport typeReport;
    private Date date;
    private ArrayList<ReportForm> reportFormList = new ArrayList<>();


    //构造函数
    public Report() {
        this.typeReport = TypeReport.DAILY;
    }

    public Report(TypeReport typeReport) {
        this.typeReport = typeReport;
    }

    public Report(TypeReport typeReport, Date date, ArrayList<ReportForm> reportFormList) {
        this.typeReport = typeReport;
        this.date = date;
        this.reportFormList = reportFormList;
    }

    //get和set方法
    public TypeReport getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(TypeReport typeReport) {
        this.typeReport = typeReport;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<ReportForm> getReportFormList() {
        return reportFormList;
    }

    public void setReportFormList(ArrayList<ReportForm> reportFormList) {
        this.reportFormList = reportFormList;
    }

    //对列表的add方法
    public void addReportForm(ReportForm reportForm) {
        this.reportFormList.add(reportForm);
    }
}
