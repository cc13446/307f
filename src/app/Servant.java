package app;
enum Mode {
    HOT, COLD, FAN
}
enum FanSpeed {
    LOW, MEDIUM, HIGH
}
public class Servant {
    private int roomId;
    private Mode mode;
    private FanSpeed fanSpeed;
    private double currentTemp;
    private double targetTemp;
    private double fee;
    private double feeRate;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(FanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(double feeRate) {
        this.feeRate = feeRate;
    }
    public boolean changeRoomTemp(){
        return true;
    }

    public boolean storeLog(){
        return true;
    }

    public boolean changeFee(){
        return true;
    }
}
