package MyHttpServe;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.sf.json.JSONObject;


public class HttpToServe {
    private InetSocketAddress addr;
    private HttpServer server;
    private String url;
    private int port;

    public HttpToServe(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public void beginServe(HttpHandler httpHandler) throws IOException {
        addr = new InetSocketAddress(port);
        server = HttpServer.create(addr, 0);
        server.createContext(url, httpHandler);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port " + port + " on url " + url);
    }
}

