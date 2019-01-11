package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.SecKillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillOrder")
public class SecKillOrderController {
    @Reference
    SecKillOrderService secKillOrderService;
    @RequestMapping("/submitOrder")
    public Result submitOrder(Long seckillId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(name)){
            return new Result(false,"请登录");
        }
        try{
            secKillOrderService.submitOrder(seckillId,name);
            return new Result(true,"提交成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"提交失败");
        }


    }
}
