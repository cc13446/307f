package app;
import Enum.FanSpeed;

public class Main {
    public static void main(String[] args) {
        Scheduler scheduler=new Scheduler(3,2,1);

        Request request1=new Request(1,35,FanSpeed.LOW,1000,Mode.FAN);
        scheduler.dealWithRequest(request1);
        Request request2=new Request(2,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request2);
        Request request3=new Request(3,40,FanSpeed.LOW,100,Mode.FAN);
        scheduler.dealWithRequest(request3);
        Request request4=new Request(4,40,FanSpeed.MEDIUM,100,Mode.FAN);
        scheduler.dealWithRequest(request4);
    }
}