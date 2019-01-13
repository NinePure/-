package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.SellMoney;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.seckill.SeckillOrder;

import java.util.Date;

public interface OrderService {

    public void add(Order order);

    public PayLog getPayLogByUserName(String userName);

    public void updatePayStatus(String userName);

    PageResult<Order> search(String page, String rows, Order order);

    PageResult<Order> searchByUserId(String page, String rows, Order order);

    PageResult<SellMoney> findSellMoney(String page, String rows, Date startTime, Date endTime, String name);

    void updateShippingStatus(Order order);

    PageResult<SeckillOrder> searchSecByUserId(String page, String rows, SeckillOrder seckillOrder);
}
