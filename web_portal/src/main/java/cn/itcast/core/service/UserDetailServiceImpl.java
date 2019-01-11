package cn.itcast.core.service;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证类:
 * springSecurity和cas集成后, 用户名, 密码的判断认证工作交给cas来完成
 * springSecurity只负责cas验证完后, 给用户赋权工作
 * 如果能进入到这个实现类, 说明cas已经认证通过, 这里只做赋权操作
 */
public class UserDetailServiceImpl implements UserDetailsService {

    private ManageUserService manageUserService;

    public void setManageUserService(ManageUserService manageUserService) {
        this.manageUserService = manageUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //定义权限集合
        List<GrantedAuthority> authList = new ArrayList<>();

        List<cn.itcast.core.pojo.user.User> userList = manageUserService.updateAuthority();
        if (userList!=null){
            for (cn.itcast.core.pojo.user.User user : userList) {
                if (username.equals(user.getUsername())){
                    //向权限集合中加入访问权限
                    authList.add(new SimpleGrantedAuthority("ROLE_NO"));
                    return new User(username, "", authList);
                }
            }
        }
        //向权限集合中加入访问权限
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username, "", authList);

    }
}
