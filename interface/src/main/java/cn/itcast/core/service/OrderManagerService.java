package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

public interface OrderManagerService {

    PageResult<Order> search(String page, String rows, Order order);
}
