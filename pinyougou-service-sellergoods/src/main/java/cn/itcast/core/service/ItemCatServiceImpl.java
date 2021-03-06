package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类管理
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        //1:从Mysql数据库将所有数据查询出来 保存到缓存中一份
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        //2:保存到缓存中  五大数据类型hash

        for (ItemCat itemCat : itemCats) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
        }

        //正常商品分类列表查询
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }


    //查询一个
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }



    @Override
    public List<ItemCat> findByItemCat3(Long parentId) {
            //1先从redis缓存中，获取三级分类信息
        List<ItemCat> itemCat01List = (List<ItemCat>) redisTemplate.boundValueOps("itemCat03").get();
        //2若缓存中没有数据，从数据库中查询，并放到缓存中
                if (itemCat01List == null) {
                    //缓存穿透 -->排队等候
                    synchronized (this) {
                        //进行二次效验?
                        itemCat01List = (List<ItemCat>) redisTemplate.boundValueOps("itemCat03").get();
                        if (itemCat01List == null) {
                            //创建一个集合，存放一级分类
                            itemCat01List = new ArrayList<>();
                            //根据parent_id = 0，获取一级分类信息！
                            List<ItemCat> itemCatList = itemCatDao.selectByParentId(parentId);
                            for (ItemCat itemCat : itemCatList) {
                                //设置一级分类信息
                                ItemCat itemCat01 = new ItemCat();
                                itemCat01.setId(itemCat.getId());
                                itemCat01.setName(itemCat.getName());
                                itemCat01.setParentId(itemCat.getParentId());

                                //根据一级分类的id -> 找到对应的二级分类
                                List<ItemCat> itemCatList02 = new ArrayList<>();
                                ItemCatQuery itemCatQuery02 = new ItemCatQuery();
                                itemCatQuery02.createCriteria().andParentIdEqualTo(itemCat.getId());
                                List<ItemCat> itemCat02List = itemCatDao.selectByExample(itemCatQuery02);
                                for (ItemCat itemCat2 : itemCat02List) {
                                    //设置二级分类信息！
                                    ItemCat itemCat02 = new ItemCat();
                                    itemCat02.setId(itemCat2.getId());
                                    itemCat2.setName(itemCat2.getName());
                                    itemCat2.setParentId(itemCat2.getParentId());

                                    //根据二级分类id->找到对应的三级分类！
                                    List<ItemCat> itemCatList03 = new ArrayList<>();
                                    ItemCatQuery itemCatQuery03 = new ItemCatQuery();
                                    itemCatQuery03.createCriteria().andParentIdEqualTo(itemCat02.getId());
                                    List<ItemCat> itemCat03List = itemCatDao.selectByExample(itemCatQuery03);
                                    for (ItemCat itemCat3 : itemCat03List) {
                                        itemCatList03.add(itemCat3);
                                    }
                                    itemCat2.setItemCatList(itemCatList03);//二级分类中 添加三级分类
                                    itemCatList02.add(itemCat02);

                                }
                                itemCat01.setItemCatList(itemCatList02);//一级 分类中 添加 二级分类
                                itemCat01List.add(itemCat01);//添加一级分类

                            }
                            //将查询到的数据放入到缓存中
                            redisTemplate.boundValueOps("itemCat03").set(itemCat01List);
                            return itemCat01List;

                        }
                    }
                }
        //3若缓存中没有数据，直接返回

        return itemCat01List;
    }
}
