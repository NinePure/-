package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    BrandService brandService;
    @RequestMapping("/add")
    public Result save(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            return new Result(true,"申请成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"品牌名重复,添加失败");
        }
    }
}
