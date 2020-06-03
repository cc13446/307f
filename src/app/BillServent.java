package app;

import Dao.LogDao;
import Domain.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Enum.*;

public class BillServent {
    private DetailBill detailBill=new DetailBill();
    private Invoice invoice=new Invoice();


    public void CreateDetailBill(int roomId, Date dateIn, Date dateOut){

        //DetailBill detailBill=new DetailBill();
        ArrayList<DetailBillItem> items=new ArrayList<>();
        List<Log> logs=new ArrayList<>();
        LogDao logDao=new LogDao();
        logs=logDao.QueryLog(roomId, dateIn, dateOut);

        for (Log log:logs){
            DetailBillItem item=new DetailBillItem();
            item.setFee(log.getFee());//每个item的fee
            item.setFanSpeed(log.getFanSpeed());//风速，高中低
            item.setMode(log.getMode());//模式
            item.setTargetTemp(log.getTargetTemp());//目标温度
            items.add(item);


        }
        detailBill.setDateIn(dateIn);
        detailBill.setDateOut(dateOut);
        detailBill.setDetailBillList(items);
    }

    public void PrintDetailBill(int RoomId)throws IOException {

        File detailBillFile=new File("DetailBillFile.txt");
        //OutputStream outputStream=new FileOutputStream(detailBillFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter("DetailBillFile.txt"));
        for (int i=0;i<detailBill.getDetailBillList().size();i++){
            writer.write(detailBill.getDetailBillList().get(i).toString()+'\n');


        }
        writer.close();



    }

    public void CreateInvoice(int roomId,Date dateIn,Date dateOut){

        LogDao logDao=new LogDao();
        invoice.setDateIn(dateIn);
        invoice.setDateOut(dateOut);
        invoice.setRoomId(roomId);
        invoice.setTotalFee(logDao.QueryTotalFee(roomId,dateIn,dateOut));



    }
    public void PrintInvoice(int roomId,Date dateIn,Date dateOut)throws IOException{

        File Invoice=new File("InvoiceFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter("InvoiceFile.txt"));
        writer.write(dateIn.toString()+'\t');
        writer.write(dateOut.toString()+'\t');
        writer.write(String.valueOf(roomId)+'\n');

    }
}
