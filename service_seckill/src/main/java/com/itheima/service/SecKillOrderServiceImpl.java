package com.itheima.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SecKillOrderService;
import cn.itcast.core.util.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.Date;

@Service
public class SecKillOrderServiceImpl implements SecKillOrderService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SeckillGoodsDao seckillGoodsDao;
    @Autowired
    IdWorker idWorker;
    @Autowired
    SeckillOrderDao seckillOrderDao;
    @Override
    public void submitOrder(Long seckillId,String name) {
        SeckillGoods seckillGoods=(SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
        if (seckillGoods==null){
            throw new RuntimeException("商品不存在");
        }
        if (seckillGoods.getStockCount()<=0){
            throw new RuntimeException("已售完");
        }
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        redisTemplate.boundHashOps("seckillGoods").put(seckillId,seckillGoods);
        if (seckillGoods.getStockCount()==0){
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
            redisTemplate.boundHashOps("seckillGoods").delete(seckillId);
        }

        //保存（redis）订单
        long orderId = idWorker.nextId();
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setId(orderId);
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setMoney(seckillGoods.getCostPrice());//秒杀价格
        seckillOrder.setSeckillId(seckillId);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setUserId(name);//设置用户ID
        seckillOrder.setStatus("0");//状态
        redisTemplate.boundHashOps("seckillOrder").put(name, seckillOrder);

    }

    @Override
    public void updateStatus(String out_trade_no, String name, String transaction_id) {
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.boundHashOps("seckillOrder").get(name);
        if (seckillOrder==null){
            throw new RuntimeException("订单不存在");
        }
        if (!out_trade_no.equals(String.valueOf(seckillOrder.getId()))){
            throw new RuntimeException("订单不一致");
        }
        seckillOrder.setTransactionId(transaction_id);//交易流水号
        seckillOrder.setPayTime(new Date());//支付时间
        seckillOrder.setStatus("1");
        seckillOrderDao.insertSelective(seckillOrder);
    }
}
