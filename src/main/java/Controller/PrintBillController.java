package Controller;

import app.BillServent;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Date;

public class PrintBillController {
    private BillServent billServent=new BillServent();

    public void CreateInvoice(int RoomId, Date dateIn, Date dateOut){
        billServent.CreateInvoice(RoomId,dateIn,dateOut);


    }
    public void PrintInvoice(int RoomId, Date dateIn, Date dateOut) throws IOException {
        billServent.PrintInvoice(RoomId,dateIn,dateOut);
    }
}
