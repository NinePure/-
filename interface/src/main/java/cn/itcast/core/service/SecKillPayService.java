package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillOrder;

public interface SecKillPayService {
    SeckillOrder getOrderFromRedis(String name);

}
