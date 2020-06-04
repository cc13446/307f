package app;

import Enum.*;
import java.util.List;

public class CheckRoomStateServant {
    public static void checkRoomState(List<Integer> roomList, Scheduler scheduler) {
        for (int roomID: roomList) {
            State state;
            double currentTemp;
            double targetTemp;
            FanSpeed fanSpeed;
            double feeRate;
            double fee;
            double duration;

            Room room;
            room = scheduler.roomList.findRoom(roomID);
            if (room != null) {
                state = room.getState();
                fee = room.getFee();
                currentTemp = room.getCurrentTemp();
            } else {
                System.out.println("Invalid room ID: " + roomID);
                continue;
            }

            if (state == State.OFF) {

            }

            Request request;
            if (state == State.HANG_UP) {
                request = scheduler.waitQueue.findRequest(roomID);
                if (request != null) {
                    targetTemp = request.getTargetTemp();
                    fanSpeed = request.getFanSpeed();
                    duration = request.getDuring();
                }
            }
            if (state == State.SERVING){
                request = scheduler.serveQueue.findRequest(roomID);
                if (request != null) {
                    targetTemp = request.getTargetTemp();
                    fanSpeed = request.getFanSpeed();
                    duration = request.getDuring();
                }
            }

        }
    }
}