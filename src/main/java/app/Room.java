package app;

import Enum.State;

public class Room {
    private State state;
    private int roomID;
    private double CurrentTemp;
    private double Fee;

    private State getState() {
        return state;
    }

    public int getRoomID() {
        return roomID;
    }

    public double getFee() {
        return Fee;
    }

    public double getCurrentTemp() {
        return CurrentTemp;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setFee(double fee) {
        Fee = fee;
    }

    public void setCurrentTemp(double currentTemp) {
        CurrentTemp = currentTemp;
    }

}