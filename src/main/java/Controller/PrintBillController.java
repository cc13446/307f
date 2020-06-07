package Controller;

import Dao.LogDao;
import app.BillServent;
import Domain.Invoice;

public class PrintBillController {
    private LogDao logDao;
    private BillServent billServant;

    public PrintBillController(LogDao logDao) {
        this.logDao = logDao;
        billServant =new BillServent(logDao);
    }
    public Invoice CreateInvoice(int RoomId){
       return billServant.CreateInvoice(RoomId);

    }
}
