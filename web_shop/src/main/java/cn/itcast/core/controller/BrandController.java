package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    BrandService brandService;

    @RequestMapping("/add")
    public Result save(@RequestBody Brand brand){
            Boolean flag = brandService.brandadd(brand);
        if (flag){

            return new Result(true,"申请成功");
        }
            return new Result(false,"品牌名重复,添加失败");

    }

    /**
     * 获取模板下拉选择框所需要的数据
     * [{id:1,text:xxx},{id:2, text: asdfasdfsa}]
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        List<Map> list = brandService.selectOptionList();
        return list;
    }

}
