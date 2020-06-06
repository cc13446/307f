package MyHttpHandler;

import Controller.CheckRoomStateController;
import Controller.StartUpController;
import app.RoomStateForm;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import Enum.*;

import java.io.*;
import java.util.List;

public class InnerHttpHandler implements HttpHandler {
    private StartUpController startUpController;

    public InnerHttpHandler(){
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            JSONObject temp = JSONObject.fromObject(str);
            System.out.println("收到：" + temp);

            int msgType=temp.getInt("msgType");
            switch (msgType){
                case 0://powerOn
                    handlePowerOn(exchange,temp);
                    break;
                case 1://checkRoomState
                    handleCheckRoomState(exchange,temp);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }


            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json=new JSONObject();
            json.put("status",0);

            responseBody.write(json.toString().getBytes());
            responseBody.close();
        }
    }

    private void handlePowerOn(HttpExchange exchange, JSONObject temp) throws IOException {
        int mode=temp.getInt("mode");
        double tempHighLimit=temp.getDouble("tempHighLimit");
        double tempLowLimit=temp.getDouble("tempLowLimit");
        double defaultTargetTemp=temp.getDouble("defaultTargetTemp");
        double feeRateHigh=temp.getDouble("feeRateHigh");
        double feeRateMid=temp.getDouble("feeRateMid");
        double feeRateLow=temp.getDouble("feeRateLow");


        startUpController=new StartUpController(feeRateHigh,feeRateMid,feeRateLow,Mode.values()[mode],tempHighLimit,tempLowLimit,defaultTargetTemp);
        startUpController.powerOn();

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        OutputStream responseBody = exchange.getResponseBody();
        JSONObject json=new JSONObject();
        json.put("state",0);
        responseBody.write(json.toString().getBytes());
        responseBody.close();
    }

    private void handleCheckRoomState(HttpExchange exchange,JSONObject temp) throws IOException {
        JSONArray jsonArray=new JSONArray();
        CheckRoomStateController checkRoomStateController=startUpController.checkRoomStateController;

        List<RoomStateForm> roomStateFormList = checkRoomStateController.getRoomStateFormList();
        for (RoomStateForm roomStateForm:roomStateFormList){
            JSONObject obj=new JSONObject();
            obj.put("customerID",roomStateForm.customerID);
            obj.put("roomID",roomStateForm.roomID);
            obj.put("state",roomStateForm.state);
            obj.put("currentTemp",roomStateForm.currentTemp);
            obj.put("targetTemp",roomStateForm.targetTemp);
            obj.put("feeRate",roomStateForm.feeRate);
            obj.put("duration",roomStateForm.duration);
            obj.put("fanSpeed",roomStateForm.fanSpeed);
            obj.put("fee",roomStateForm.fee);
            jsonArray.add(obj);
        }
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(jsonArray.toString().getBytes());
        responseBody.close();
    }
}
