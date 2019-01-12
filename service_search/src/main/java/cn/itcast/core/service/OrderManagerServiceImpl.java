package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.SellMoney;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
@Service
public class OrderManagerServiceImpl implements OrderManagerService {

    @Autowired
    private OrderDao orderDao;


    //订单查询分页
    @Override
    public PageResult<Order> search(String page, String rows, Order order) {
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(rows));
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        if (order!=null){
            if (order.getOrderId()!=null && !"".equals(order.getOrderId()))
                criteria.andOrderIdEqualTo(order.getOrderId());
        }
        Page<Order> orders =(Page<Order>) orderDao.selectByExample(orderQuery);
        return new PageResult <>(orders.getTotal(),orders.getResult());
    }



}
