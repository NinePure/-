package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;

;

public interface GoodsUpDown {
    //上下架页面高级查询
    PageResult search(Goods goods, Integer page, Integer rows);
    //提交商品申请上架
    void updateIsMarketable(Long id, String isMarketable);
    //商品下架删除
    void delete(Long id);
}
