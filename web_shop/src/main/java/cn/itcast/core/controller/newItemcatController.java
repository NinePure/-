package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newitemcat")
public class newItemcatController {

    @Reference
    ItemCatService itemCatService;
    @RequestMapping("/itemcatadd")
    public Result itemcatadd (ItemCat itemCat){
        Boolean flag = itemCatService.itemcatadd(itemCat);
        if (flag){
            return new Result(true,"添加成功");
        }
        return new Result(true,"添加失败,用户名已存在");
    }
}
