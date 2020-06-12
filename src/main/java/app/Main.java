package app;

import MyHttpHandler.InnerHttpHandler;
import MyHttpServe.HttpToServe;
import java.io.IOException;

/*
 *  服务器程序主入口
 *  最后更新时间：2020/6/5 23:11
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //服务器与服务器界面的HTTP通信，使用8080端口
        HttpToServe innerServe = new HttpToServe(8080);
        innerServe.beginServe("/",new InnerHttpHandler());
    }
}