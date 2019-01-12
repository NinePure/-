package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.UserSpecEntity;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {

    public void sendCode(String phone);

    public Boolean checkSmsCode(String phone , String smsCode);

    public  void  add(User user);

    public  void  addUserSpec(String username,UserSpecEntity userSpecEntity);

    void addHeadPic(String name, String path);

    String getHeadPic(String username);

    List<Item> findCollect(String userName);
}
