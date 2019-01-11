package cn.itcast.core.controller;


import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    UserService userService;

    @RequestMapping("/name")
    public Map<String, String> name() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        String headPic = userService.getHeadPic(username);
        map.put("loginName", username);
        map.put("picPath", headPic);
        return map;
    }
}
