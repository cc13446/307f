package app;

import java.util.ArrayList;
import java.util.List;

public class RoomList {
    private static List<Room> totalRoomList;

    public RoomList() {
        totalRoomList = new ArrayList<Room>();
    }

    public void addRoom(int roomID, double currentTemp, double initTemp) {
        Room room = new Room(roomID, currentTemp, initTemp);
        totalRoomList.add(room);
    }

    public boolean removeRoom(int roomID) {
        for (Room room: totalRoomList) {
            if (room.getRoomID()==roomID) {
                totalRoomList.remove(room);
                return true;
            }
        }
        return false;
    }

    public Room findRoom(int roomID) {
        for (Room room: totalRoomList) {
            if (room.getRoomID()==roomID) {
                return room;
            }
        }
        return null;
    }

}