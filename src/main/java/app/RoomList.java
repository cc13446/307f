package app;

import java.util.ArrayList;
import java.util.List;

public class RoomList {
    private static List<Room> totalRoomList;

    public RoomList() {
        totalRoomList = new ArrayList<Room>();
    }

    public void addRoom(int customId,int roomID, double currentTemp, double initTemp) {
        Room room = new Room(customId, roomID, currentTemp, initTemp);
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

}