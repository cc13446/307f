package app;
import Controller.StartUpController;
import Enum.*;
import MyHttpHandler.InnerHttpHandler;
import MyHttpServe.HttpToServe;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpToServe innerServe = new HttpToServe(8080);
        innerServe.beginServe("/",new InnerHttpHandler());

    }
}