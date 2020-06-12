package MyHttpHandler;

import Controller.UseController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONObject;
import java.io.*;

/*
 *  调节温度请求的通信handler
 *  最后更新时间：2020/6/5 17:58
 */

public class TempHttpHandler implements HttpHandler {

    private UseController useController;


    public TempHttpHandler(UseController useController){
        super();
        this.useController=useController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            System.out.println(exchange.getRequestURI());
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            JSONObject temp = JSONObject.fromObject(str);
            System.out.println("收到：" + temp);
            int id=temp.getInt("id");
            double targetTemperature=temp.getInt("targetTemperature");

            useController.changeTargetTemp(id,targetTemperature);

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
