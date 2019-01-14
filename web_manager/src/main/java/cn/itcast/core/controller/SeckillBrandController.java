package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillBrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillBrandController {
    @Reference
    SeckillBrandService seckillBrandService;

    @RequestMapping("/findAll")
    public List<SeckillGoods> findAll() {
        List<SeckillGoods> goodsList = seckillBrandService.findAll();
        return goodsList;
    }

    @RequestMapping("/delete")
    public void delete(Long[] ids) {
        seckillBrandService.delete(ids);
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody SeckillGoods seckillGoods, Integer page, Integer rows) {
        PageResult search = seckillBrandService.search(seckillGoods, page, rows);
        return search;
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            seckillBrandService.updateStatus(ids,status);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }
}
