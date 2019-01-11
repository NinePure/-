package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ManageUserServiceImpl implements ManageUserService {

    @Autowired
    private UserDao userDao;


    //运营商后台-用户管理（冻结）  用户冻结后无法登陆,下单  1冻结  0正常
    @Override
    public void frozenUser(Long[] ids) {
        if (ids!=null){
            for (Long id : ids) {
                User user = new User();
                user.setId(id);
                user.setIsFrozen("1");
                userDao.updateByPrimaryKeySelective(user);
            }
        }
    }

    //运营商后台-用户统计  统计用户个数
    @Override
    public Integer userCount() {
        Integer size = userDao.countByExample(null);
        return size;
    }

    //运营商后台-用户活跃度分析  分别统计活跃用户与非活跃用户的个数
    //周活跃用户
    @Override
    public Integer wau() {
//TODO ...
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        criteria.andCountWeekGreaterThanOrEqualTo("3");
        List<User> userList = userDao.selectByExample(query);
        if (userList!=null && !"".equals(userList)){
            return userList.size();
        }
        return 0;
    }

    //周非活跃用户
    @Override
    public Integer noWau() {
//TODO ...
        return userCount()-wau();
    }

    @Override
    public PageResult findPage(User user, Integer page, Integer rows) {
        //使用分页助手, 传入当前页和每页查询多少条数据
        PageHelper.startPage(page, rows);
        //创建查询对象
        //创建where查询条件
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        if (user != null) {
            if (user.getUsername() != null && !"".equals(user.getUsername())){
                criteria.andUsernameLike("%"+user.getUsername()+"%");
            }
            if (user.getId() != null && !"".equals(user.getId())) {
                criteria.andIdEqualTo(user.getId());
            }
        }
        //使用分页助手的page对象接收查询到的数据, page对象继承了ArrayList所以可以接收查询到的结果集数据.
        Page<User> userList = (Page<User>)userDao.selectByExample(query);
        return new PageResult(userList.getTotal(), userList.getResult());
    }


    //修改权限
    @Override
    public List<User> updateAuthority() {
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        criteria.andIsFrozenEqualTo("1");
        List<User> userList = userDao.selectByExample(query);
        return userList;
    }


}
