package MyHttpHandler;

import Controller.UseController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONObject;
import Enum.*;

import java.io.*;

/*
 *  请求查看费用的通信handler
 *  最后更新时间：2020/6/8 23:13
 */

public class FeeHttpHandler implements HttpHandler {
    private UseController useController;

    public FeeHttpHandler(UseController useController){
        super();
        this.useController=useController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            System.out.println(exchange.getRequestURI());
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            JSONObject temp = JSONObject.fromObject(str);
            int id=temp.getInt("id");
            Double currentTemp=temp.getDouble("currentTemperature");
            Double changeTemperature=temp.getDouble("changeTemperature");

            double fee = useController.requestFee(id, currentTemp);
            State state = useController.requestState(id);
            int roomState = 0;
            switch (state){
                case SERVE:roomState=0;break;
                case WAIT:roomState=1;break;
                case HOLDON:roomState=2;break;
                default:roomState=2;break;
            }
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json=new JSONObject();
            json.put("status",0);
            JSONObject data=new JSONObject();
            data.put("fee", fee);
            data.put("roomState", roomState);
            data.put("id", id);
            json.put("data", data);
            responseBody.write(json.toString().getBytes());
            responseBody.close();
        }
    }
}
