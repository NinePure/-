package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.GoodsUpDown;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



    /*
    * 管理员上下架Controller
    * */
@RestController
@RequestMapping("/goods_upanddown")
public class UpAndDownController {
    @Reference
    GoodsUpDown goodsUpDown;

    /**
     * 查询所有的商家上下架申请
     * @param:
     * @return:
     */
    @RequestMapping("/search.do")
    public PageResult search(@RequestBody Goods goods, Integer page , Integer rows){
        try {
            //获取登录商家的用户名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置商家名
            goods.setSellerId(name);
            //调用service
            return  goodsUpDown.search(goods,page,rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *上架审核
     * @param: isMarketable:上下架状态
     * @return:
     */
    @RequestMapping("/updateIsMarketable.do")
    public Result updateIsMarketable(Long[] ids, String isMarketable){
        try {
            if (ids!=null){
                for (Long id : ids) {
                    goodsUpDown.updateIsMarketable(id,isMarketable);
                }
            }
            return new Result(true,"申请通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"申请失败");
        }
    }
    //商品下架删除
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            if (ids != null){
                for (Long id : ids) {
                    goodsUpDown.delete(id);
                }
            }
            return new Result(true,"下架成功");
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false,"下架失败");
        }
    }


}
