package Domain;

import java.util.Date;

public class Invoice {
    //入住时间
    private Date dateIn;
    //退房时间
    private Date dateOut;
    //总费用
    private double totalFee;
    //房间号
    private int roomId;
    //顾客号
    private int customId;


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

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "dateIn=" + dateIn +
                ", dateOut=" + dateOut +
                ", totalFee=" + totalFee +
                ", roomId=" + roomId +
                ", customId=" + customId +
                '}';
    }
}
