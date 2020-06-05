package app;
import Controller.StartUpController;
import Enum.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        new StartUpController(1, 0.5, 0.33, Mode.HOT, 10.0, 10.0,20.0).powerOn();
    }
}