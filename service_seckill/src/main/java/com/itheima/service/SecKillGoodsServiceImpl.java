package com.itheima.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.service.SecKillGoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
@Service
public class SecKillGoodsServiceImpl implements SecKillGoodsService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    SeckillGoodsDao seckillGoodsDao;
    @Override
    public List<SeckillGoods> findList() {
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        if (seckillGoodsList==null||seckillGoodsList.size()==0){
            SeckillGoodsQuery seckillGoodsQuery=new SeckillGoodsQuery();
            SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
            criteria.andStatusEqualTo("1");
            criteria.andStockCountGreaterThan(0);//剩余库存大于0
            criteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间
            criteria.andEndTimeGreaterThan(new Date());//结束时间大于当前时间
            List<SeckillGoods> seckillGoods = seckillGoodsDao.selectByExample(seckillGoodsQuery);
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps("seckillGoods").put(seckillGood.getId(),seckillGood);
            }
            List<SeckillGoods> seckillGoodsList1=redisTemplate.boundHashOps("seckillGoods").values();
            return seckillGoodsList1;
        }else{
            return seckillGoodsList;
        }

    }

    @Override
    public SeckillGoods findByIdFromRedis(Long id) {
        SeckillGoods seckillGoods = (SeckillGoods)redisTemplate.boundHashOps("seckillGoods").get(id);
        return seckillGoods;
    }
}
