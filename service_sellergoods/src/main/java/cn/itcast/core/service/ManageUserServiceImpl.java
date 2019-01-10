package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;

@Service
@Transactional
public class ManageUserServiceImpl implements ManageUserService {

    @Autowired
    private UserDao userDao;


    //运营商后台-用户管理（冻结）  用户冻结后无法登陆,下单  1冻结  0正常
    @Override
    public void frozenUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setIsFrozen("1");
        userDao.updateByPrimaryKeySelective(user);
    }

    //运营商后台-用户统计  统计用户个数
    @Override
    public void UserCount() {

    }

    //运营商后台-用户活跃度分析  分别统计活跃用户与非活跃用户的个数
    //周活跃用户
    @Override
    public Integer wau(Long id) {
        return null;
    }

    //周非活跃用户
    @Override
    public Integer noWau(Long id) {
        return null;
    }
}
