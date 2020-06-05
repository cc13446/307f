package app;

import Enum.State;

public class Room {


    //标志服务的唯一ID
    private int ID;
    private State state;
    private int roomID;
    private double currentTemp;
    private double initTemp;
    private double fee;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public Room(int roomID, double currentTemp, double initTemp) {
        this.state = State.OFF;
        this.roomID = roomID;
        this.currentTemp = currentTemp;
        this.initTemp = initTemp;
        this.fee = 0;
        System.out.println("Create room " + roomID + " with CurrentTemp:" + currentTemp + " and initTemp:" + initTemp);
    }
}