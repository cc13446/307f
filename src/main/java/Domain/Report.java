package Domain;

import Enum.TypeReport;
import java.util.Date;

/*
 *  报表对象
 *  最后更新时间：2020/6/12 23:21
 */

public class Report {
    //报表类型
    private TypeReport typeReport;
    //报表日期
    private Date date;


    public Report(TypeReport typeReport, Date date) {
        this.typeReport = typeReport;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TypeReport getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(TypeReport typeReport) {
        this.typeReport = typeReport;
    }
}
