package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.SellMoney;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.util.Constants;
import cn.itcast.core.util.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private PayLogDao payLogDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsDescDao goodsDescDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public void add(Order order) {
        //获取当前登录用户的用户名
        String userId = order.getUserId();
        //根据用户名到redis中获取当前用户的购物车集合
        List<BuyerCart> cartList= (List<BuyerCart>)redisTemplate.boundHashOps(Constants.CART_LIST_REDIS).get(userId);

        List<String> orderIdList=new ArrayList();//订单ID列表
        double total_money=0;//总金额 （元）

        if (cartList != null) {
            //1. 遍历购物车集合
            for (BuyerCart cart : cartList) {
                //TODO 2. 根据购物车来形成订单记录
                long orderId = idWorker.nextId();
                System.out.println("sellerId:"+cart.getSellerId());
                Order tborder=new Order();//新创建订单对象
                tborder.setOrderId(orderId);//订单ID
                tborder.setUserId(order.getUserId());//用户名
                tborder.setPaymentType(order.getPaymentType());//支付类型
                tborder.setStatus("1");//状态：未付款
                tborder.setCreateTime(new Date());//订单创建日期
                tborder.setUpdateTime(new Date());//订单更新日期
                tborder.setReceiverAreaName(order.getReceiverAreaName());//地址
                tborder.setReceiverMobile(order.getReceiverMobile());//手机号
                tborder.setReceiver(order.getReceiver());//收货人
                tborder.setSourceType(order.getSourceType());//订单来源
                tborder.setSellerId(cart.getSellerId());//商家ID
                //循环购物车明细
                double money=0;

                //3. 从购物车中获取订单明细集合
                List<OrderItem> orderItemList = cart.getOrderItemList();
                if (orderItemList != null) {
                    //4. 遍历购物明细集合
                    for (OrderItem orderItem : orderItemList) {
                        //TODO 5.根据购物明细对象形成订单详情记录
                        orderItem.setId(idWorker.nextId());
                        orderItem.setOrderId( orderId  );//订单ID
                        orderItem.setSellerId(cart.getSellerId());
                        money+=orderItem.getTotalFee().doubleValue();//金额累加
                        orderItemDao.insertSelective(orderItem);

                    }
                }
                tborder.setPayment(new BigDecimal(money));
                orderDao.insertSelective(tborder);
                orderIdList.add(orderId+"");//添加到订单列表
                total_money+=money;//累加到总金额

            }

            //TODO 6. 计算所有购物车中的总价钱, 形成支付日志记录
            if("1".equals(order.getPaymentType())){//如果是微信支付
                PayLog payLog=new PayLog();
                String outTradeNo=  idWorker.nextId()+"";//支付订单号
                payLog.setOutTradeNo(outTradeNo);//支付订单号
                payLog.setCreateTime(new Date());//创建时间
                //订单号列表，逗号分隔
                String ids=orderIdList.toString().replace("[", "").replace("]", "").replace(" ", "");
                payLog.setOrderList(ids);//订单号列表，逗号分隔
                payLog.setPayType("1");//支付类型
                payLog.setTotalFee( (long)(total_money*100 ) );//总金额(分)
                payLog.setTradeState("0");//支付状态
                payLog.setUserId(order.getUserId());//用户ID
                payLogDao.insertSelective(payLog);//插入到支付日志表

                //TODO 7. 使用用户名作为key, 支付日志对象作为value保存到redis中, 供支付使用
                redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//放入缓存
            }
            //TODO 8. 删除购物车
            redisTemplate.boundHashOps(Constants.CART_LIST_REDIS).delete(order.getUserId());

        }
    }

    @Override
    public PayLog getPayLogByUserName(String userName) {
        PayLog payLog = (PayLog)redisTemplate.boundHashOps("payLog").get(userName);
        return payLog;
    }

    @Override
    public void updatePayStatus(String userName) {
        //1. 根据用户名获取redis中的支付日志对象
        PayLog payLog = (PayLog)redisTemplate.boundHashOps("payLog").get(userName);

        if (payLog != null) {
            //2. 更改支付日志表中的支付状态为已支付
            payLog.setTradeState("1");
            payLogDao.updateByPrimaryKeySelective(payLog);

            //3. 更改订单表中的支付状态
            String orderListStr = payLog.getOrderList();
            String[] split = orderListStr.split(",");
            if (split != null) {
                for (String orderId : split) {
                    Order order = new Order();
                    order.setOrderId(Long.parseLong(orderId));
                    order.setStatus("1");
                    order.setPaymentTime(new Date());
                    orderDao.updateByPrimaryKeySelective(order);
                }
            }

            //4. 删除redis中的支付日志数据
            redisTemplate.boundHashOps("payLog").delete(userName);
        }
    }
    @Override
    public PageResult<Order> search(String page, String rows, Order order) {
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        OrderQuery orderQuery = new OrderQuery();
        if (order != null) {
            OrderQuery.Criteria criteria = orderQuery.createCriteria();
            criteria.andSellerIdEqualTo(order.getSellerId());
            if (order.getUserId() != null && !"".equals(order.getUserId())) {
                criteria.andUserIdLike("%" + order.getUserId() + "%");
            }
            if (order.getOrderId() != null && !"".equals(order.getOrderId())) {
                criteria.andOrderIdEqualTo(order.getOrderId());
            }
            if (order.getStatus() != null && !"".equals(order.getStatus())) {
                criteria.andStatusEqualTo(order.getStatus());
            }
        }
        Page<Order> page1 = (Page<Order>) orderDao.selectByExample(orderQuery);
        List<Order> result = page1.getResult();

        if (result != null && result.size() > 0) {
            for (Order order1 : result) {
                Long orderId = order1.getOrderId();
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                criteria1.andOrderIdEqualTo(orderId);
                criteria1.andSellerIdEqualTo(order1.getSellerId());
                List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
                order1.setOrderItemList(orderItemList);
            }
        }
        PageResult<Order> pageResult = new PageResult<>(page1.getTotal(), result);
        return pageResult;
    }

    @Override
    public PageResult<Order> searchByUserId(String page, String rows, Order order) {
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        OrderQuery orderQuery = new OrderQuery();
        if (order != null) {
            OrderQuery.Criteria criteria = orderQuery.createCriteria();
            criteria.andUserIdEqualTo(order.getUserId());
            if (order.getStatus() != null && !"".equals(order.getStatus())) {
                criteria.andStatusEqualTo(order.getStatus());
            }
        }
        Page<Order> page1 = (Page<Order>) orderDao.selectByExample(orderQuery);
        List<Order> result = page1.getResult();
        if (result != null && result.size() > 0) {
            for (Order order1 : result) {
                Long orderId = order1.getOrderId();
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                criteria1.andOrderIdEqualTo(orderId);
                List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
                for (OrderItem orderItem : orderItemList) {
                    ItemQuery itemQuery = new ItemQuery();
                    ItemQuery.Criteria criteria = itemQuery.createCriteria();
                    criteria.andTitleEqualTo(orderItem.getTitle());
                    List<Item> itemList = itemDao.selectByExample(itemQuery);
                    orderItem.setItemSpec(itemList.get(0).getSpec());
                }
                order1.setOrderItemList(orderItemList);
            }
        }

        PageResult<Order> pageResult = new PageResult<>(page1.getTotal(), result);
        return pageResult;
    }

    @Override
    public PageResult<SellMoney> findSellMoney(String page, String rows, Date startTime, Date endTime, String name) {
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        GoodsQuery goodsQuery=new GoodsQuery();
        GoodsQuery.Criteria criteria2 = goodsQuery.createCriteria();
        criteria2.andSellerIdEqualTo(name);
        Page<Goods> goodsPage=(Page<Goods>)goodsDao.selectByExample(goodsQuery);
        List<Goods> goodsList = goodsPage.getResult();
        List<SellMoney> sellMoneyList = new ArrayList<>();
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        if (startTime != null && endTime != null) {
            criteria.andPaymentTimeBetween(startTime, endTime);
        }
        criteria.andSellerIdEqualTo(name);
        List<Order> orders = orderDao.selectByExample(orderQuery);
        Integer num=0;
        BigDecimal bigDecimal=new BigDecimal("0");
        for (Goods goods : goodsList) {
            SellMoney sellMoney = new SellMoney();
            sellMoney.setStatus(goods.getIsDelete());
            sellMoney.setName(goods.getGoodsName());
            sellMoney.setPrice(goods.getPrice());
            GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(goods.getId());
            String itemImages = goodsDesc.getItemImages();
            List<Map> list = JSON.parseArray(itemImages, Map.class);
            sellMoney.setImageUrl(String.valueOf(list.get(0).get("url")));
            if (orders.size()>0){
                for (Order order : orders) {
                    OrderItemQuery orderItemQuery = new OrderItemQuery();
                    OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                    criteria1.andGoodsIdEqualTo(goods.getId());
                    criteria1.andOrderIdEqualTo(order.getOrderId());
                    List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
                    if (orderItemList.size()==0){
                        sellMoney.setNum(0);
                        sellMoney.setTotalPrice(new BigDecimal(0));
                    }
                    for (OrderItem orderItem : orderItemList) {
                        num+=orderItem.getNum();
                        bigDecimal = bigDecimal.add(orderItem.getTotalFee());
                        sellMoney.setNum(num);
                        sellMoney.setTotalPrice(bigDecimal);

                    }
                }
            }else{
                sellMoney.setNum(0);
                sellMoney.setTotalPrice(new BigDecimal("0"));
            }

            sellMoneyList.add(sellMoney);
        }


        return new PageResult<SellMoney>(goodsPage.getTotal(),sellMoneyList);
    }
    @Override
    public void updateShippingStatus(Order order) {
        orderDao.updateByPrimaryKeySelective(order);
    }

    @Override
    public PageResult<SeckillOrder> searchSecByUserId(String page, String rows, SeckillOrder seckillOrder) {
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(null);
        for (SeckillOrder order : seckillOrders) {
            Long seckillId = order.getSeckillId();
            SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(seckillId);
            order.setSeckillGoods(seckillGoods);
        }
        Page<SeckillOrder> seckillOrderPage = (Page<SeckillOrder>) seckillOrders;
        return new PageResult<>(seckillOrderPage.getTotal(), seckillOrderPage.getResult());

    }

}
