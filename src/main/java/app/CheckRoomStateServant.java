package app;

import Enum.FanSpeed;
import Enum.State;

public class CheckRoomStateServant {
    public static RoomStateForm checkRoomState(int roomID, Scheduler scheduler) {
        State state;
        double currentTemp;
        double fee;
        double targetTemp = 0;
        FanSpeed fanSpeed = FanSpeed.MEDIUM;
        double feeRate = 1;
        double duration = 0;

        Room room;
        room = scheduler.roomList.findRoom(roomID);
        if (room != null) {
            state = room.getState();
            fee = room.getFee();
            currentTemp = room.getCurrentTemp();
        } else {
            System.out.println("Invalid room ID: " + roomID);
            return null;
        }

        if (state == State.OFF) {
            return new RoomStateForm(state, fee, currentTemp);
        }

        Request request = null;
        if (state == State.HANG_UP) {
            request = scheduler.waitQueue.findRequest(roomID);
        }
        if (state == State.ON){
            request = scheduler.serveQueue.findRequest(roomID);
        }

        if (request != null) {
            targetTemp = request.getTargetTemp();
            fanSpeed = request.getFanSpeed();
            duration = request.getDuring();
            if (fanSpeed == FanSpeed.LOW) {
                feeRate = scheduler.getFEE_RATE_LOW();
            } else if (fanSpeed == FanSpeed.MEDIUM) {
                feeRate = scheduler.getFEE_RATE_MID();
            } else {
                feeRate = scheduler.getFEE_RATE_HIGH();
            }
        }

        return new RoomStateForm(state, fee, currentTemp, targetTemp, feeRate, duration, fanSpeed);
    }
}