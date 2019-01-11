package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import cn.itcast.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao catDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TypeTemplateDao templateDao;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //1. 查询所有分类数据
        List<ItemCat> itemAllList = catDao.selectByExample(null);
        //2. 将所有分类数据以分类名称为key, 对应的模板id为value, 存入redis中
        for (ItemCat itemCat : itemAllList) {
            redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).put(itemCat.getName(), itemCat.getTypeId());
        }

        //3. 到数据库中查询数据返回到页面展示
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        //根据父级id查询
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = catDao.selectByExample(query);
        return itemCats;
    }

    @Override
    public ItemCat findOne(Long id) {
        return catDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return catDao.selectByExample(null);
    }

    /**
     * 分类审核
     *
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null) {
            for (Long id : ids) {
                ItemCat itemCat = new ItemCat();
                itemCat.setId(id);
                itemCat.setCatStatus(status);
                catDao.updateByPrimaryKeySelective(itemCat);
               /* ItemCatQuery itemCatQuery = new ItemCatQuery();
                ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
                criteria.andParentIdEqualTo(id);
                catDao.updateByExampleSelective(itemCat, itemCatQuery);*/
            }
        }
    }

    @Override
    public PageResult search(ItemCat itemCat, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Page<ItemCat> itemCats = (Page<ItemCat>) catDao.selectByExample(null);
        return new PageResult(itemCats.getTotal(),itemCats.getResult());
    }




}
