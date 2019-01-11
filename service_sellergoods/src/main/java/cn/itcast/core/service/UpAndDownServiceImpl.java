package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

    /*
    * 商品上下架管理页面
    */
@Service
@Transactional
public class UpAndDownServiceImpl implements GoodsUpDown {
        @Autowired
        GoodsDao goodsDao;
        //中间件
        @Autowired
        JmsTemplate jmsTemplate;
         //商品上架
         @Autowired
         ActiveMQTopic topicPageAndSolrDestination;
         //商品下架
         @Autowired
         ActiveMQQueue queueSolrDeleteDestination;

    @Override
    public PageResult search(Goods goods, Integer page, Integer rows) {
        //使用分页助手
        PageHelper.startPage(page,rows);
        //创建查询条件
        GoodsQuery query = new GoodsQuery();
        GoodsQuery.Criteria criteria = query.createCriteria();
        if (goods!=null){
            if (goods.getIsMarketable()!=null&&!"".equals(goods.getIsMarketable())){
                //商品审核状态查询
                criteria.andIsMarketableEqualTo(goods.getIsMarketable());
            }

            if (goods.getGoodsName()!=null&&!"".equals(goods.getGoodsName())){
                //商品名称模糊查询
                criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
            if (goods.getSellerId()!=null&&!"".equals(goods.getSellerId())
                    &&!"admin".equals(goods.getSellerId())&&!"wc".equals(goods.getSellerId())){
                //登录用户名和商家名对比
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
                //只允许审核通过的商品展示在页面
                criteria.andAuditStatusEqualTo("1");

        }
        Page<Goods> goodsList=(Page<Goods>) goodsDao.selectByExample(query);
        //将结果集赋值给参数
        return new PageResult(goodsList.getTotal(),goodsList.getResult());
    }

    //商品上架申请,修改上架状态
     /* isMarketable:是否上架
                    0         1           2
       审核状态: "未审核","审核通过","审核未通过",
                   3       4          5         6       7         8
        是否上架:"关闭","上架申请","已上架","下架申请","已下架","申请未通过"
     */
    @Override
    public void updateIsMarketable(final Long id, String isMarketable) {
        //1.创建商品对象.修改上下架状态
        Goods goods = new Goods();
        //设置上下架状态
        goods.setId(id);
        goods.setIsMarketable(isMarketable);
        goodsDao.updateByPrimaryKeySelective(goods);

        //2.判断上架的审核状态
        if ("5".equals(isMarketable)){
            //发送消息,第一个参数是发送到的队列,第二个参数是一个接口,定义发送的内容
            jmsTemplate.send(topicPageAndSolrDestination,new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                    return textMessage;
                }
            });
        }
    }

        //商品下架删除
        /*
                    0         1           2
       审核状态: "未审核","审核通过","审核未通过",
                    3       4       5       6           7         8
        是否上架:"关闭","上架申请","已上架","下架申请","已下架","申请未通过"
     */
        public void delete(final Long id){
        //1.创建商品对象.修改上下架状态
        Goods goods = new Goods();
        goods.setId(id);
        //是否上下架
        goods.setIsMarketable("7");
        //修改假删除状态
        goods.setIsDelete("1");
        goodsDao.updateByPrimaryKeySelective(goods);

        //2.判断商品isDelete的状态
        if ("1".equals(goods.getIsDelete())){
            //发送消息,第一个参数是发送到的队列,第二个参数是一个接口,定义发送的内容
            jmsTemplate.send(queueSolrDeleteDestination,new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                    return textMessage;
                }
            });
        }
    }





}

