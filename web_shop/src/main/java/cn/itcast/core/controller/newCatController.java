package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newitemcat")
public class newCatController {

    @Reference
    ItemCatService catService;
    //分类申请
    @RequestMapping("/save")
    public Result save(String itemcat,Long id){
        Boolean save = catService.save(itemcat,id);
        if (save){
            return new Result(true,"申请成功");
        }else  {
            return new Result(false,"申请失败,该分类已存在");
        }
    }

}
