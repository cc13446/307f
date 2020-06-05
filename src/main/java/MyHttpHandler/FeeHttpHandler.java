package MyHttpHandler;

import Controller.UseController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class FeeHttpHandler implements HttpHandler {
    private UseController useController;

    public FeeHttpHandler(UseController useController){
        super();
        this.useController=useController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
