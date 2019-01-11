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
        while (true){
            try {
            Map<String, String> map = weiXinPayService.queryPayStatus(out_trade_no);
            if (map==null){
                return new Result(false,"支付失败");
            }
            if ("SUCCESS".equals(map.get("trade_state"))){
                secKillOrderService.updateStatus(out_trade_no,name,map.get("transaction_id"));
                return new Result(true,"支付成功");
            }
            Thread.sleep(3000);
            if (flag>100){
                return new Result(false,"二维码超时");
            }
            flag++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
