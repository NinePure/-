package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SeckillBrandService {
    public List<SeckillGoods> findAll();

    public void delete(Long[] ids);

    public PageResult search(SeckillGoods seckillGoods, Integer page, Integer rows);

    public void updateStatus(Long[] ids, String status);
}
