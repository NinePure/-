package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.Order;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public List<Goods> findAll() {
        List<Goods> list = goodsDao.selectByExample(null);
        return list;
    }

    @Override
    public List<Order> OrderFindAll() {
        List<Order> ordersList = orderDao.selectByExample(null);
        return ordersList;
    }
}
