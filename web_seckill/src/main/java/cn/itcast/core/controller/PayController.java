package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.PayService;
import cn.itcast.core.service.SecKillOrderService;
import cn.itcast.core.service.SecKillPayService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    SecKillPayService payService;
    @Reference
    PayService weiXinPayService;
    @Reference
    SecKillOrderService secKillOrderService;

    @RequestMapping("/createNative")
    public Map createNative(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        SeckillOrder orderFromRedis = payService.getOrderFromRedis(name);
        if (orderFromRedis!=null){
            long fen=  (long)(orderFromRedis.getMoney().doubleValue()*100);
            Map aNative = weiXinPayService.createNative(String.valueOf(orderFromRedis.getId()), fen + "");
            return aNative;
        }
        return new HashMap();
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        int flag=0;

        Result result=new Result(false,"正在查询");

        while (true){
            try {
                Map<String, String> map = weiXinPayService.queryPayStatus(out_trade_no);
                if (map==null){
                    result=new Result(false,"支付失败");
                    break;
                }
                if ("SUCCESS".equals(map.get("trade_state"))){
                    secKillOrderService.updateStatus(out_trade_no,name,map.get("transaction_id"));
                    result=new Result(true,"支付成功");
                    break;
                }
                Thread.sleep(3000);
                if (flag>100){
                    result=new Result(false,"二维码超时");
                    break;
                }
                flag++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/closePayOrder")
    public Result closePayOrder(String out_trade_no){
        Map map1 = weiXinPayService.closeOrderPay(out_trade_no);
        if ("SUCCESS".equals(map1.get("return_code"))){
            return new Result(true,"已关闭支付,请重新订购");
        }else {
            return new Result(false,"不能关闭支付");
        }
    }



}
