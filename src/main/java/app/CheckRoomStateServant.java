package app;

import java.util.List;
import Enum.*

public class CheckRoomStateServant {
    public static void checkRoomState(List<Integer> roomList) {
        for (int roomID:roomList) {
            State state;

            Request request = WaitQueue.findRequest(roomID);

        }
    }
}