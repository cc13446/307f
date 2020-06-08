package app;

import Dao.LogDao;
import Domain.DetailBillItem;
import Domain.Invoice;
import Domain.Log;
import java.util.LinkedList;
import java.util.List;
import Enum.*;

public class BillServent {
    private LogDao logDao;

    public BillServent(LogDao logDao) {
        this.logDao = logDao;
    }

    public List<DetailBillItem> CreateDetailBill(int customID){

        List<DetailBillItem> items = new LinkedList<>();
        List<Log> logs = logDao.QueryLog(customID);
        System.out.println(logs);
        System.out.println(logs.size());
        for (int i = 0; i < logs.size() - 1; i++){
            if(logs.get(i).getScheduleType() != ScheduleType.REQUEST_ON && logs.get(i).getScheduleType() != ScheduleType.REQUEST_OFF
                    &&logs.get(i + 1).getScheduleType() != ScheduleType.REQUEST_ON && logs.get(i + 1).getScheduleType() != ScheduleType.REQUEST_OFF ){
                System.out.println(i);
                DetailBillItem item=new DetailBillItem();
                item.setStartTime(logs.get(i).getTime());
                item.setEndTime(logs.get(i + 1).getTime());
                item.setMode(logs.get(i).getMode());//模式
                item.setFanSpeed(logs.get(i).getFanSpeed());//风速，高中低
                item.setTargetTemp(logs.get(i).getTargetTemp());//目标温度
                item.setFee(logs.get(i+1).getFee()-logs.get(i).getFee());//每个item的fee 后一个减当前
                item.setFeeRate(logs.get(i).getFeeRate());
                item.setDuration(item.getEndTime().getTime() - item.getStartTime().getTime());
                items.add(item);
                System.out.println(item);
            }
        }
        System.out.println(items);
        return items;
    }

    public Invoice CreateInvoice(int roomId){
        Invoice invoice = new Invoice();
        int customId = logDao.QueryCustomId(roomId);
        invoice.setCustomId(customId);
        if(invoice.getCustomId() != 0){
            invoice.setDateIn(logDao.QueryRequestDateIn(customId));
            invoice.setDateOut(logDao.QueryRequestDateOut(customId));
            invoice.setRoomId(roomId);
            invoice.setTotalFee(logDao.QueryTotalFee(customId));
        }
        System.out.println(invoice);
        return invoice;
    }
}
