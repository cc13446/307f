package MyHttpHandler;

import Controller.UseController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONObject;
import java.io.*;

/*
 *  退房的通信handler
 *  最后更新时间：2020/6/7 01:40
 */

public class RoomExitHttpHandler implements HttpHandler {
    private UseController useController;

    public RoomExitHttpHandler(UseController useController){
        this.useController=useController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("PUT")) {
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            System.out.println(str);
            JSONObject resJson = JSONObject.fromObject(str);
            System.out.println("收到：" + resJson);
            //这里不是roomId
            int id=resJson.getInt("id");
            useController.requestEnd(id);
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
}
