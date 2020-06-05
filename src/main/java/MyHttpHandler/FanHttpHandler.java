package MyHttpHandler;

import Controller.UseController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONObject;
import Enum.*;
import java.io.*;

public class FanHttpHandler implements HttpHandler {
    private UseController useController;

    public FanHttpHandler(UseController useController) {
        super();
        this.useController = useController;
    }

    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            System.out.println(exchange.getRequestURI());
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            JSONObject temp = JSONObject.fromObject(str);
            System.out.println("收到：" + temp);
            System.out.println(temp.getInt("roomId"));
            int id=temp.getInt("id");
            int fanSpeed=temp.getInt("fanSpeed");

            useController.changeFanSpeed(id, FanSpeed.values()[fanSpeed]);

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
