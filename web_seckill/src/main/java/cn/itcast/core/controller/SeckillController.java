package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SecKillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillController {
    @Reference
    SecKillGoodsService secKillGoodsService;


    @RequestMapping("/findList")
    public List<SeckillGoods> findList(){
        List<SeckillGoods> seckillGoodsList=secKillGoodsService.findList();
        return seckillGoodsList;
    }
    @RequestMapping("/findOneFromRedis")
    public SeckillGoods findOneFromRedis(Long id){
        SeckillGoods seckillGoods=secKillGoodsService.findByIdFromRedis(id);
        return seckillGoods;
    }
}
