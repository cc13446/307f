package app;

import Enum.State;

public class Room {


    //标志服务的唯一ID
    private int customId;
    private State state;
    private int roomID;
    private double currentTemp;
    private double initTemp;
    private double fee;

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public State getState() {
        return state;
    }

    public int getRoomID() {
        return roomID;
    }

    public double getFee() {
        return fee;
    }

    public double getInitTemp() {
        return initTemp;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setInitTemp(double initTemp) {
        this.initTemp = initTemp;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public Room(int customId, int roomID, double currentTemp, double initTemp) {
        this.customId = customId;
        this.state = State.OFF;
        this.roomID = roomID;
        this.currentTemp = currentTemp;
        this.initTemp = initTemp;
        this.fee = 0;
        System.out.println("Create room " + roomID + " with CurrentTemp:" + currentTemp + " and initTemp:" + initTemp);
    }

    @Override
    public String toString() {
        return "Room{" +
                "customId=" + customId +
                ", state=" + state +
                ", roomID=" + roomID +
                ", currentTemp=" + currentTemp +
                ", initTemp=" + initTemp +
                ", fee=" + fee +
                '}';
    }
}