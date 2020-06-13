package MyHttpHandler;

import Controller.UseController;
import Domain.Request;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONObject;

import java.io.*;
import Enum.*;

/*
 *  开启关闭空调的通信handler
 *  最后更新时间：2020/6/10 16:47
 */

public class RequestOnAndOffHandler implements HttpHandler {
    private UseController useController;

    public RequestOnAndOffHandler(UseController useController){
        super();
        this.useController=useController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            //处理开启空调请求
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            System.out.println(str);
            JSONObject resJson = JSONObject.fromObject(str);
            System.out.println("收到：" + resJson);
            int id=resJson.getInt("id");
            double targetTemperature=resJson.getDouble("targetTemperature");
            int fanSpeed=resJson.getInt("fanSpeed");
            double currentTemperature=resJson.getDouble("currentTemperature");

            useController.requestOn(new Request(id,useController.findRoomId(id),targetTemperature, FanSpeed.values()[fanSpeed],0,useController.getMode()),currentTemperature);

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json=new JSONObject();
            json.put("status",0);
            responseBody.write(json.toString().getBytes());
            responseBody.close();
        }else if(requestMethod.equalsIgnoreCase("PUT")){
            //处理关闭空调请求
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            System.out.println(str);
            JSONObject resJson = JSONObject.fromObject(str);
            System.out.println("收到：" + resJson);

            int id=resJson.getInt("id");

            useController.requestOff(id);
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json=new JSONObject();
            json.put("status",0);
            System.out.println(json);
            responseBody.write(json.toString().getBytes());
            responseBody.close();
        }
    }
}
