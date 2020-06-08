package Domain;

public class ReportForm {
    private int roomId;//房间号
    private int turnTimes;//每个房间的开关次数
    private long useTime;//使用空调的时长
    private double totalFee;//总费用
    private int schedulerTimes;//被调度的次数
    private int customNumber;//详单数
    private int changeTempTimes;//调温次数
    private int changeFanSpeedTimes;//调风次数



    //构造方法
    public ReportForm() {
        turnTimes = 0;
        totalFee = 0;
        schedulerTimes = 0;
        customNumber = 0;
        changeTempTimes = 0;
        changeFanSpeedTimes = 0;
        roomId = 0;
        useTime = 0;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTurnTimes() {
        return turnTimes;
    }

    public void setTurnTimes(int turnTimes) {
        this.turnTimes = turnTimes;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public int getSchedulerTimes() {
        return schedulerTimes;
    }

    public void setSchedulerTimes(int schedulerTimes) {
        this.schedulerTimes = schedulerTimes;
    }

    public int getCustomNumber() {
        return customNumber;
    }

    public void setCustomNumber(int customNumber) {
        this.customNumber = customNumber;
    }

    public int getChangeTempTimes() {
        return changeTempTimes;
    }

    public void setChangeTempTimes(int changeTempTimes) {
        this.changeTempTimes = changeTempTimes;
    }

    public int getChangeFanSpeedTimes() {
        return changeFanSpeedTimes;
    }

    public void setChangeFanSpeedTimes(int changeFanSpeedTimes) {
        this.changeFanSpeedTimes = changeFanSpeedTimes;
    }

    @Override
    public String toString() {
        return "ReportForm{" +
                "roomId=" + roomId +
                ", turnTimes=" + turnTimes +
                ", useTime=" + useTime +
                ", totalFee=" + totalFee +
                ", schedulerTimes=" + schedulerTimes +
                ", customNumber=" + customNumber +
                ", changeTempTimes=" + changeTempTimes +
                ", changeFanSpeedTimes=" + changeFanSpeedTimes +
                '}';
    }
}
