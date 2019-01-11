package cn.itcast.core.service;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.Order;

import java.util.List;


public interface ExcelService {
    public List<Goods> findAll();
    public List<Order> OrderFindAll();
}
