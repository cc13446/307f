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

        for (int i=0;i<logs.size()-1;i++){
            DetailBillItem item=new DetailBillItem();
            item.setFee(logs.get(i+1).getFee()-logs.get(i).getFee());//每个item的fee 后一个减当前
            item.setFanSpeed(logs.get(i).getFanSpeed());//风速，高中低
            item.setMode(logs.get(i).getMode());//模式
            item.setTargetTemp(logs.get(i).getTargetTemp());//目标温度
            item.setScheduleType(logs.get(i).getScheduleType());
            item.setDuration(logs.get(i+1).getTime().getTime()-logs.get(i).getTime().getTime());//毫秒 后一个log的时间减去这个log的时间
            item.setStartTime(logs.get(i).getTime());
            item.setFeeRate(logs.get(i).getFeeRate());
            items.add(item);


        }
        int len = logs.size() - 1;
        DetailBillItem item = new DetailBillItem();
        item.setFee(0);//每个item的fee 后一个减当前
        item.setFanSpeed(logs.get(len).getFanSpeed());//风速，高中低
        item.setMode(logs.get(len).getMode());//模式
        item.setTargetTemp(logs.get(len).getTargetTemp());//目标温度
        item.setScheduleType(logs.get(len).getScheduleType());
        item.setDuration(0);//毫秒 后一个log的时间减去这个log的时间
        item.setStartTime(logs.get(len).getTime());
        item.setFeeRate(logs.get(len).getFeeRate());
        items.add(item);
        detailBill.setRoomId(roomId);
        detailBill.setDateIn(dateIn);
        detailBill.setDateOut(dateOut);
        detailBill.setDetailBillList(items);
    }

    public void PrintDetailBill(int RoomId)throws IOException {

        File detailBillFile=new File("DetailBillFile.txt");
        //OutputStream outputStream=new FileOutputStream(detailBillFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter("DetailBillFile.txt"));
        writer.write("房间"+RoomId+'\n'+'\n');
        for (int i=0;i<detailBill.getDetailBillList().size();i++){
            //writer.write(detailBill.getDetailBillList().get(i).toString()+'\n');
            writer.write("服务开始时间："+detailBill.getDetailBillList().get(i).getStartTime().toString()+'\n');
            writer.write("服务持续时间："+String.valueOf(detailBill.getDetailBillList().get(i).getDuration())+'\n');
            writer.write("目标温度："+String.valueOf(detailBill.getDetailBillList().get(i).getTargetTemp())+'\n');
            writer.write("风速："+detailBill.getDetailBillList().get(i).getFee()+'\n');
            writer.write("模式："+detailBill.getDetailBillList().get(i).getMode()+'\n');
            writer.write("费率"+detailBill.getDetailBillList().get(i).getFeeRate()+'\n');
            writer.write("服务的费用"+detailBill.getDetailBillList().get(i).getFee()+'\n');
            writer.write("---------------------------------------------" +'\n');




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
        writer.write("房间"+String.valueOf(roomId)+'\n');
        writer.write("总费用"+String.valueOf(invoice.getTotalFee())+'\n');
        writer.write("入住时间"+dateIn.toString()+'\n');
        writer.write("退房时间"+dateOut.toString()+'\n');


    }
}
