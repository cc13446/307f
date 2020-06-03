package app;

import java.util.ArrayList;
import java.util.Date;

public class DetailBill {
    private int roomId;
    private Date dateIn;
    private Date dateOut;
    //private String information;
    private ArrayList<DetailBillItem> detailBillList;



//    public double getTotal(){
//        double total=0;
//        for(DetailBillItem item:detailBillList){
//            total=total+item.getFee();
//        }
//        return total;
//    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

//    public String getInformation() {
//        return information;
//    }
//
//    public void setInformation(String information) {
//        this.information = information;
//    }

    public ArrayList<DetailBillItem> getDetailBillList() {
        return detailBillList;
    }

    public void setDetailBillList(ArrayList<DetailBillItem> detailBillList) {
        this.detailBillList = detailBillList;
    }
}
