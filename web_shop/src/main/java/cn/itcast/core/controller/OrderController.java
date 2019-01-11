package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.entity.SellMoney;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    OrderService orderService;



    @RequestMapping("/search")
    public PageResult<Order> search(String page, String rows, @RequestBody Order order){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(name);
        PageResult<Order> pageResult=orderService.search(page,rows,order);
        return pageResult;
    }

    @RequestMapping("/findSellMoney")
    public Map<String,Object> findSellMoney(String page, String rows, Date startTime, Date endTime){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map=new HashMap();
        PageResult<SellMoney> sellMoneyPageResult=orderService.findSellMoney(page,rows,startTime,endTime,name);
        //开始时间的日历
        Calendar startCalendar=Calendar.getInstance();
        startCalendar.setTime(startTime);
        //结束时间的日历
        Calendar endCalendar=Calendar.getInstance();
        endCalendar.setTime(endTime);
        //用于停止时间
        Calendar testCalendar=Calendar.getInstance();
        testCalendar.setTime(startTime);
        //日期的集合
        List<String> result = new ArrayList<String>();
        //单日销售额的集合
        List<BigDecimal> bigDecimalList=new ArrayList<>();
        //循环比较开始日期是否在截止日期之前
        while(startCalendar.before(endCalendar)){
            //单日停止时间的加一天
            testCalendar.add(Calendar.DATE,1);
            //得到单日销售的集合
            PageResult<SellMoney> sellMoney = orderService.findSellMoney(page, rows, startCalendar.getTime(), testCalendar.getTime(), name);
            List<SellMoney> rows1 = sellMoney.getRows();
            //遍历集合获得单日销售的销售额,添加进单日销售额的集合
            BigDecimal toatlByDay=new BigDecimal("0");
            for (SellMoney money : rows1) {
                toatlByDay=toatlByDay.add(money.getTotalPrice());
            }
            bigDecimalList.add(toatlByDay);
            //获得日期,转换格式,添加进日期集合
            DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String format = dateFormat.format(startCalendar.getTime());
            result.add(format);
            startCalendar.add(Calendar.DATE,1);
        }
        //将单日日期添加进map集合
        map.put("dateList",result);
        //将单日销售额添加进map集合
        map.put("bigDecimalList",bigDecimalList);
        //将各商品销售额添加进map集合
        map.put("pageResult",sellMoneyPageResult);
        return map;
    }
    @RequestMapping("/insertShippingToOrder")
    public Result insertShippingToOrder(@RequestBody Order order){
        order.setStatus("3");
        try{
            orderService.updateShippingStatus(order);
            return new Result(true,"发货成功");
        }catch (Exception e){
            return new Result(false,"发货失败");
        }


    }

}
