package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SecKillGoodsService {
    List<SeckillGoods> findList();

    SeckillGoods findByIdFromRedis(Long id);
}
