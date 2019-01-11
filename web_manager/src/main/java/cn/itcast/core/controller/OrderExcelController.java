package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.ExcelService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderExcelController {
    @Reference
    private ExcelService excelService;

    @RequestMapping("/excelOrder")
    public void findAll() throws Exception {

        List<Order> ordersList = excelService.OrderFindAll();

        List<HashMap<String, Object>> listMap = new ArrayList<>();
        for (Order order : ordersList) {


            HashMap<String, Object> dataMap = new HashMap<>();
           dataMap.put("1",order.getOrderId());
           dataMap.put("2",order.getPayment());
           dataMap.put("3",order.getPaymentType());
           dataMap.put("4",order.getPostFee());
           dataMap.put("5",order.getStatus());
           dataMap.put("6",order.getCreateTime());
           dataMap.put("7",order.getUpdateTime());
           dataMap.put("8",order.getPaymentTime());
           dataMap.put("9",order.getUserId());
           dataMap.put("10",order.getReceiverAreaName());
           dataMap.put("11",order.getReceiverMobile());
           dataMap.put("12",order.getReceiver());
           dataMap.put("13",order.getSourceType());
           dataMap.put("14",order.getSellerId());
            listMap.add(dataMap);

        }



        String title = "订单Excel表";
        String[] rowsName = new String[] { "OrderId", "Payment", "PaymentType", "PostFee", "Status", "CreateTime", "UpdateTime", "PaymentTime", "UserId",
                "ReceiverAreaName", "ReceiverMobile", "Receiver", "SourceType", "SellerId"};
        List<Object[]> dataList = new ArrayList <Object[]>();
        Object[] objs = null;
        for (int i = 0; i < listMap.size(); i++) {
            HashMap<String, Object> data = listMap.get(i);// 获取5次
            objs = new Object[rowsName.length];
            objs[0] = data.get("1");
            objs[1] = data.get("2");
            objs[2] = data.get("3");
            objs[3] = data.get("4");
            objs[4] = data.get("5");
            objs[5] = data.get("6");
            objs[6] = data.get("7");
            objs[7] = data.get("8");
            objs[8] = data.get("9");
            objs[9] = data.get("10");
            objs[10] = data.get("11");
            objs[11] = data.get("12");
            objs[12] = data.get("13");
            objs[13] = data.get("14");

            dataList.add(objs);
        }
        ExportExcelorder ex = new ExportExcelorder(title, rowsName, dataList);
        ex.export();
    }

}
