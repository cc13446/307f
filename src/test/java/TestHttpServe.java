import MyHttpHandler.FanHttpHandler;
import MyHttpServe.HttpToServe;

import java.io.IOException;

public class TestHttpServe {
    public static void main(String[] args) throws IOException {
        FanHttpHandler fanHttpHandler = new FanHttpHandler();
        HttpToServe fanServe = new HttpToServe("/room/fan", 80);
        fanServe.beginServe(fanHttpHandler);
    }
}
