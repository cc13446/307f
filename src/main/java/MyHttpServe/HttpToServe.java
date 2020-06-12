package MyHttpServe;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 *  通信工具类，绑定url与handler
 *  最后更新时间：2020/6/5 17:05
 */

public class HttpToServe {
    private InetSocketAddress addr;
    private HttpServer server;
    private int port;

    public HttpToServe(int port) {
        this.port = port;
    }

    public void beginServe(String str, HttpHandler handler) throws IOException {
        addr = new InetSocketAddress(port);
        server = HttpServer.create(addr, 0);
        server.createContext(str,handler);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    public void beginServe(List<String> urlList, List<HttpHandler> handlerList) throws IOException {
        addr = new InetSocketAddress(port);
        server = HttpServer.create(addr, 0);
        createContext(urlList,handlerList);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    private void createContext(List<String> urlList, List<HttpHandler> handlerList){
        //绑定列表中的url与handler
        int len = urlList.size();
        for(int i=0;i<len;i++){
            server.createContext(urlList.get(i),handlerList.get(i));
        }
    }
}

