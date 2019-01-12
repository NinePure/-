package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    public List<ItemCat> findByParentId(Long parentId);

    public ItemCat findOne(Long id);

    public List<ItemCat> findAll();

    public void updateStatus(Long[] ids, String status);

    public PageResult search(ItemCat itemCat, Integer page, Integer rows);

    public Boolean save(String itemcat,Long id);

    //    添加分类申请
    Boolean itemcatadd(ItemCat itemCat);
}
