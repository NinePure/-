package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserLog;

import java.util.List;


public interface ManageUserService {

    //运营商后台-用户管理（冻结）  用户冻结后无法登陆,下单  1冻结  0正常
    void frozenUser(Long[] ids);

    //运营商后台-用户统计  统计用户个数
    Integer userCount();

    //运营商后台-用户活跃度分析  分别统计活跃用户与非活跃用户的个数
    //周活跃用户
    Integer wau();
    //周非活跃用户
    Integer noWau();


    PageResult findPage(User user, Integer page, Integer rows);

    //修改权限
    String updateAuthority(String username);

    //根据用户名查询id
    Long findIdByUsername(String username);

    //封装UserLog表数据
    void addUserLog(UserLog userLog);

    //根据UserLog表查询User表countWeek数据
    void queryCount();


}
