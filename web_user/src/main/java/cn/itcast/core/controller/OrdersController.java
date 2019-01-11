package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Order order, Integer page, Integer rows) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setUserId(userName);
        PageResult<Order> pageResult = orderService.searchByUserId(String.valueOf(page), String.valueOf(rows), order);
        return pageResult;
    }
}
