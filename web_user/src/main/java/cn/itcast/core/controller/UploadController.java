package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.UserService;
import cn.itcast.core.util.FastDFSClient;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Reference
    private UserService userService;
    /**
     * 从application.properties文件中的FILE_SERVER_URL键获取它的值复制给变量
     * 这里读取的是文件服务器的地址
     */
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER;

    /**
     * 上传文件
     * @param file
     * @throws Exception
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) throws Exception {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //上传
            String path = fastDFSClient.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            String realPath = FILE_SERVER + path;
            userService.addHeadPic(name,realPath);
            return new Result(true, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败!");
        }
    }
}
