package Controller;

import app.CheckRoomStateServant;
import Domain.RoomStateForm;
import app.Scheduler;
import java.util.List;

/**
 * 检测房间状态控制器：负责接收和处理服务器前端的查看房间状态请求
 * 最后修改时间：2020/6/12 2:55
 */

public class CheckRoomStateController {
    // 调度对象
    private Scheduler scheduler;

    // 构造函数
    public CheckRoomStateController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    // 获取房间状态列表
    public List<RoomStateForm> getRoomStateFormList(){
        System.out.println("CheckRoomStateController:getRoomStateFormList");
        return new CheckRoomStateServant().getRoomStateFormList(scheduler);
    }
}