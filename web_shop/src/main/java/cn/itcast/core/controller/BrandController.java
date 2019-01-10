package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newBrand")
public class BrandController {
    @Reference
    BrandService brandService;
    @RequestMapping("/save")
    public Result save(String username){
        Boolean save = brandService.save(username);
        if (save){
            return new Result(true,"申请成功");
        }
        return new Result(false,"品牌名重复,添加失败");
    }
}
