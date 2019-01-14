package com.itheima.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillBrandService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SeckillBrandServiceImpl  implements SeckillBrandService {
    @Autowired
    SeckillGoodsDao seckillGoodsDao;
    @Override
    public List<SeckillGoods> findAll() {
        List<SeckillGoods> seckillGoods = seckillGoodsDao.selectByExample(null);
        return seckillGoods;
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                seckillGoodsDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public PageResult search(SeckillGoods seckillGoods, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Page<SeckillGoods> seckillGoods1 = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(null);
        return new PageResult(seckillGoods1.getTotal(),seckillGoods1.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null) {
            for (Long id : ids) {
                SeckillGoods seckillGoods = new SeckillGoods();
                seckillGoods.setId(id);
                seckillGoods.setStatus(status);
                seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
            }
        }
    }
}
