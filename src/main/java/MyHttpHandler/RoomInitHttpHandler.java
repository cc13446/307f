package MyHttpHandler;

import Controller.UseController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONObject;

import java.io.*;
import Enum.*;

public class RoomInitHttpHandler implements HttpHandler {
    private UseController useController;
    private static int roomNum=0;

    public RoomInitHttpHandler(UseController useController){
        super();
        this.useController=useController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            System.out.println(str);
            JSONObject resJson = JSONObject.fromObject(str);
            System.out.println("收到：" + resJson);
            int roomId=resJson.getInt("roomId");
            double currentTemperature=resJson.getDouble("currentTemperature");
            double defaultTargetTemperature;
            double highestTemperature;
            double lowestTemperature=18;
            int defaultFanSpeed=1;

            Mode mode=useController.getMode();
            if (mode==Mode.COLD){
                defaultTargetTemperature=25;
                highestTemperature=28;
            }else{
                defaultTargetTemperature=22;
                highestTemperature=25;
            }

            useController.addRoom(roomNum, roomId,currentTemperature,defaultTargetTemperature);

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json=new JSONObject();
            json.put("status",0);
            JSONObject data=new JSONObject();
            data.put("id",roomNum);
            data.put("highestTemperature",highestTemperature);
            data.put("lowestTemperature",lowestTemperature);
            data.put("defaultFanSpeed",defaultFanSpeed);
            data.put("defaultTargetTemperature",defaultTargetTemperature);
            roomNum++;
            json.put("data",data);
            responseBody.write(json.toString().getBytes());
            responseBody.close();
        }
    }
}
