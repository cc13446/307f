package Controller;

import app.BillServent;

import java.io.IOException;
import java.util.Date;

public class PrintDetailBillController {
    private BillServent billServant=new BillServent();

    public void CreateDetailBill(int roomId, Date dateIn, Date dateOut){
        billServant.CreateDetailBill(roomId,dateIn,dateOut);
    }
    public void PrintDetailBill(int RoomId)throws IOException{
        billServant.PrintDetailBill(RoomId);
    }
}
