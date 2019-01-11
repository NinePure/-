package com.itheima.service;

import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SecKillPayService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class PayServiceImpl implements SecKillPayService {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public SeckillOrder getOrderFromRedis(String name) {
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.boundHashOps("seckillOrder").get(name);
        return seckillOrder;
    }
}
