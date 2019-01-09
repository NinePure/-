package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    public List<Order> orderList(@RequestBody Order order, Integer page, Integer rows){

        return null;
    }

}
