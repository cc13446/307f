package Controller;

import Dao.LogDao;
import Domain.DetailBillItem;
import Domain.Log;
import app.BillServent;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class PrintDetailBillController {
    private LogDao logDao;
    private BillServent billServant;

    public PrintDetailBillController(LogDao logDao) {
        this.logDao = logDao;
        this.billServant = new BillServent(logDao);
    }

    public List<DetailBillItem> CreateDetailBill(int customID){
        return billServant.CreateDetailBill(customID);

    }
}
