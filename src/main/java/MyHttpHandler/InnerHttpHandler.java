package MyHttpHandler;

import Controller.*;
import Domain.DetailBillItem;
import Domain.Invoice;
import Domain.Report;
import Domain.ReportForm;
import Domain.RoomStateForm;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import Enum.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/*
 *  来自服务器控制面板的内部通信handler
 *  最后更新时间：2020/06/10 18:23
 */

public class InnerHttpHandler implements HttpHandler {

    private StartUpController startUpController;

    public InnerHttpHandler(){
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            InputStream in = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = reader.readLine();
            JSONObject temp = JSONObject.fromObject(str);
            System.out.println("收到：" + temp);

            int msgType=temp.getInt("msgType");
            switch (msgType){
                case 0://powerOn
                    handlePowerOn(exchange,temp);
                    break;
                case 1://checkRoomState
                    handleCheckRoomState(exchange,temp);
                    break;
                case 2:
                    // 查看账单
                    handleCheckBill(exchange,temp);
                    break;
                case 3:
                    // 查看详单
                    handleCheckDetailBill(exchange,temp);
                    break;
                case 4:
                    // 查看报表
                    try {
                        handleCheckReport(exchange,temp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                case 5:
                    // 系统关机
                    systemOut(exchange,temp);
                    break;
            }

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json=new JSONObject();
            json.put("status",0);

            responseBody.write(json.toString().getBytes());
            responseBody.close();
        }
    }

    private void handlePowerOn(HttpExchange exchange, JSONObject temp) throws IOException {
        //开启服务器
        int mode=temp.getInt("mode");
        double tempHighLimit=temp.getDouble("tempHighLimit");
        double tempLowLimit=temp.getDouble("tempLowLimit");
        double defaultTargetTemp=temp.getDouble("defaultTargetTemp");
        double feeRateHigh=temp.getDouble("feeRateHigh");
        double feeRateMid=temp.getDouble("feeRateMid");
        double feeRateLow=temp.getDouble("feeRateLow");


        startUpController=new StartUpController(feeRateHigh,feeRateMid,feeRateLow,Mode.values()[mode],tempHighLimit,tempLowLimit,defaultTargetTemp);
        startUpController.powerOn();

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        OutputStream responseBody = exchange.getResponseBody();
        JSONObject json=new JSONObject();
        json.put("state",0);
        System.out.println(json);
        responseBody.write(json.toString().getBytes());
        responseBody.close();
    }

    private void handleCheckRoomState(HttpExchange exchange,JSONObject temp) throws IOException {
        //查看房间状态
        JSONArray jsonArray=new JSONArray();
        CheckRoomStateController checkRoomStateController=startUpController.checkRoomStateController;

        List<RoomStateForm> roomStateFormList = checkRoomStateController.getRoomStateFormList();
        for (RoomStateForm roomStateForm:roomStateFormList){
            JSONObject obj=new JSONObject();
            obj.put("customerID",roomStateForm.customerID);
            obj.put("roomID",roomStateForm.roomID);
            obj.put("state",roomStateForm.state);
            obj.put("currentTemp",roomStateForm.currentTemp);
            obj.put("targetTemp",roomStateForm.targetTemp);
            obj.put("feeRate",roomStateForm.feeRate);
            obj.put("duration",roomStateForm.duration);
            obj.put("fanSpeed",roomStateForm.fanSpeed);
            obj.put("fee",roomStateForm.fee);
            jsonArray.add(obj);
        }
        System.out.println("jsonArray构造完毕");
        System.out.println(jsonArray);
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(jsonArray.toString().getBytes());
        responseBody.close();
    }
    private void handleCheckBill(HttpExchange exchange,JSONObject temp) throws IOException {
        // 查看账单
        JSONArray jsonArray=new JSONArray();
        PrintBillController printBillController =startUpController.printBillController;
        int roomId = temp.getInt("roomId");

        Invoice invoice = printBillController.CreateInvoice(roomId);

        JSONObject obj=new JSONObject();
        obj.put("customId",invoice.getCustomId());
        obj.put("totalFee",invoice.getTotalFee());
        obj.put("requestOnDate",invoice.getDateIn().toString());
        obj.put("requestOffDate",invoice.getDateOut().toString());
        System.out.println(obj);
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(obj.toString().getBytes());
        responseBody.close();
    }
    private void handleCheckDetailBill(HttpExchange exchange,JSONObject temp) throws IOException {
        // 查看详单
        JSONArray jsonArray=new JSONArray();
        PrintDetailBillController printDetailBillController =startUpController.printDetailBillController;
        PrintBillController printBillController =startUpController.printBillController;
        int roomId = temp.getInt("roomId");
        Invoice invoice = printBillController.CreateInvoice(roomId);
        int customID = invoice.getCustomId();
        List<DetailBillItem> detailBillItemList = printDetailBillController.CreateDetailBill(customID);
        System.out.println(detailBillItemList);
        for (DetailBillItem detailBillItem:detailBillItemList){
            JSONObject obj=new JSONObject();
            obj.put("startTime",detailBillItem.getStartTime().toString());
            obj.put("endTime",detailBillItem.getEndTime().toString());
            obj.put("mode",detailBillItem.getMode().ordinal());
            obj.put("fanSpeed",detailBillItem.getFanSpeed().ordinal());
            obj.put("targetTemp",detailBillItem.getTargetTemp());
            obj.put("fee",detailBillItem.getFee());
            obj.put("feeRate",detailBillItem.getFeeRate());
            obj.put("duration",detailBillItem.getDuration());

            jsonArray.add(obj);
        }
        System.out.println(jsonArray.toString());
        JSONObject json = new JSONObject();
        json.put("customId", customID);
        json.put("requestOnDate", invoice.getDateIn().toString());
        json.put("requestOffDate", invoice.getDateOut().toString());
        json.put("data", jsonArray);
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(json.toString().getBytes());
        responseBody.close();
    }
    private void handleCheckReport(HttpExchange exchange,JSONObject temp) throws IOException, ParseException {
        // 查看报表
        SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        TypeReport reportType = TypeReport.values()[temp.getInt("reportType")];
        Date reportDate = sdf.parse( temp.get("reportDate").toString());
        Report report = new Report(reportType,reportDate);
        JSONArray array = temp.getJSONArray("roomList");
        LinkedList<Integer> roomList = new LinkedList<>();
        for(int i = 0; i < array.size(); i++){
            roomList.add(JSONObject.fromObject(array.get(i)).getInt("roomId"));
        }

        JSONArray jsonArray=new JSONArray();
        PrintReportController printReportController = startUpController.printReportController;
        List<ReportForm> reportFormList = printReportController.QueryReport(roomList,report);
        System.out.println(reportFormList);
        for (ReportForm reportForm:reportFormList){
            JSONObject obj=new JSONObject();
            obj.put("roomId",reportForm.getRoomId());
            obj.put("turnTimes",reportForm.getTurnTimes());
            obj.put("useTime",reportForm.getUseTime());
            obj.put("totalFee",reportForm.getTotalFee());
            obj.put("schedulerTimes",reportForm.getSchedulerTimes());
            obj.put("customNumber",reportForm.getCustomNumber());
            obj.put("changeTempTimes",reportForm.getChangeTempTimes());
            obj.put("changeFanSpeedTimes",reportForm.getChangeFanSpeedTimes());
            jsonArray.add(obj);
        }
        System.out.println(reportFormList);
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(jsonArray.toString().getBytes());
        responseBody.close();
    }
    private void systemOut(HttpExchange exchange,JSONObject temp) throws IOException{
        // 系统关机
        JSONObject obj=new JSONObject();
        obj.put("state",0);
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(obj.toString().getBytes());
        responseBody.close();
        System.exit(0);
    }
}
