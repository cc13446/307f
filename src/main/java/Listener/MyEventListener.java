package Listener;


import java.util.EventObject;

public interface MyEventListener extends java.util.EventListener {
    //事件处理
    public void handleEvent(EventObject event);
}
