package Domain;

import Domain.ReportForm;
import Enum.TypeReport;

import java.util.ArrayList;
import java.util.Date;

public class Report {
    private TypeReport typeReport;
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
