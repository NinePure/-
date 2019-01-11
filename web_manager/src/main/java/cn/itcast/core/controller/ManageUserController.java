package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.ManageUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manageUser")
public class ManageUserController {

    @Reference
    private ManageUserService manageUserService;

    //运营商后台-用户管理（冻结）  用户冻结后无法登陆,下单  1冻结  0正常
    @RequestMapping("/frozenUser")
    public Result frozenUser(Long[] ids){
        try {
            manageUserService.frozenUser(ids);
            return new Result(true,"用户冻结成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"用户冻结失败");
        }
    }

    //运营商后台-用户统计  统计用户个数
    @RequestMapping("/userCount")
    public Integer userCount() {
        return manageUserService.userCount();
    }

    //运营商后台-用户活跃度分析  分别统计活跃用户与非活跃用户的个数
    //周活跃用户
    @RequestMapping("/wau")
    public Integer wau() {
        return manageUserService.wau();
    }
    //周非活跃用户
    @RequestMapping("/noWau")
    public Integer noWau() {
        return manageUserService.noWau();
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody User user, Integer page, Integer rows) {
        PageResult result = manageUserService.findPage(user, page, rows);
        return result;
    }


}
