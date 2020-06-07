package app;

import java.util.ArrayList;
import java.util.List;

import Domain.Room;
import Enum.*;

public class RoomList {
    private static List<Room> totalRoomList;

    public RoomList() {
        totalRoomList = new ArrayList<Room>();
    }

    public void addRoom(int customId,int roomID, double currentTemp, double targetTemp,  FanSpeed defaultFanSpeed, double defaultFeeRate) {
        Room room = new Room(customId, roomID, currentTemp, targetTemp, defaultFanSpeed, defaultFeeRate);
        System.out.println(room);
        totalRoomList.add(room);
    }

    public boolean removeRoom(int customId) {
        for (Room room: totalRoomList) {
            if (room.getCustomId()==customId) {
                totalRoomList.remove(room);
                return true;
            }
        }
        return false;
    }

    public Room findRoom(int customId) {
        for (Room room: totalRoomList) {
            if (room.getCustomId() ==customId) {
                return room;
            }
        }
        return null;
    }

    public List<RoomStateForm> getRoomStateFormList(){
        List<RoomStateForm> list=new ArrayList<>();
        for (Room room:totalRoomList){
            int customId=room.getCustomId();
            int roomId=room.getRoomID();
            int state=room.getState().ordinal();
            double fee=room.getFee();
            double currentTemp=room.getCurrentTemp();
            double targetTemp=room.getTargetTemp();
            double feeRate=room.getFeeRate();
            long duration=room.getDuration();
            int fanSpeed=room.getFanSpeed().ordinal();
            RoomStateForm roomStateForm=new RoomStateForm(customId,roomId,state,fee,currentTemp,targetTemp,feeRate,duration,fanSpeed);
            list.add(roomStateForm);
        }
        return list;
    }

}