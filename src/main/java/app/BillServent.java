package app;

import Dao.LogDao;
import Domain.DetailBillItem;
import Domain.Invoice;
import Domain.Log;
import java.util.LinkedList;
import java.util.List;
import Enum.*;

/*
 *  查看账单的服务对象（来自前台的服务）
 *  最后更新时间：2020/6/8 20:20
 *
 */

public class BillServent {

    private LogDao logDao;


    public BillServent(LogDao logDao) {
        this.logDao = logDao;
    }

    public List<DetailBillItem> CreateDetailBill(int customID){
        //构造一个详单
        List<DetailBillItem> items = new LinkedList<>();
        //构造日志列表，所有顾客号为customID的日志记录
        List<Log> logs = logDao.QueryLog(customID);
        System.out.println(logs);
        System.out.println(logs.size());
        //遍历日志列表，并给详单赋值
        for (int i = 0; i < logs.size() - 1; i++){
            if(logs.get(i).getScheduleType() != ScheduleType.REQUEST_ON && logs.get(i).getScheduleType() != ScheduleType.REQUEST_OFF
                    &&logs.get(i + 1).getScheduleType() != ScheduleType.REQUEST_ON && logs.get(i + 1).getScheduleType() != ScheduleType.REQUEST_OFF ){
                System.out.println(i);
                DetailBillItem item=new DetailBillItem();
                item.setStartTime(logs.get(i).getTime());//服务开始时间
                item.setEndTime(logs.get(i + 1).getTime());//服务结束时间
                item.setMode(logs.get(i).getMode());//模式
                item.setFanSpeed(logs.get(i).getFanSpeed());//风速，高中低
                item.setTargetTemp(logs.get(i).getTargetTemp());//目标温度
                item.setFee(logs.get(i+1).getFee()-logs.get(i).getFee());//每个item的fee=后一个item中的fee-减当前item的fee
                item.setFeeRate(logs.get(i).getFeeRate());//费率
                item.setDuration(item.getEndTime().getTime() - item.getStartTime().getTime());//服务持续时间
                items.add(item);
                System.out.println(item);
            }
        }
        System.out.println(items);
        return items;
    }

    public Invoice CreateInvoice(int roomId){
        //构造一个账单
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
