package app;

import java.util.LinkedList;
import java.util.List;

public class HoldOnQueue {
    public static List<Request> holdOnRequestList = new LinkedList<Request>();
    public static Request findRequst(int customId){
        for (Request req:holdOnRequestList){
            if(customId==req.getCustomId()){
                return req;
            }
        }
        return null;
    }
    public static boolean addRequst(Request request){
        holdOnRequestList.add(request);
        return true;
    }
    public static Request removeRequst(int customId){
        Request req =  findRequst(customId);
        holdOnRequestList.remove(req);
        return req;
    }

}
