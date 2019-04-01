package cn.itcast.core.service;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.Item;
import entity.PageResult;
import vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    void add(GoodsVo goodsVo);

    PageResult search(Integer page, Integer rows, Goods goods);

    GoodsVo findOne(Long id);

    void update(GoodsVo goodsVo);

    void  updateStatus(Long[] ids, String status);

    void delete(Long[] ids);

    List<Item> findItemListByGoodsIdListAndStatus(Long[] goodsIds, String status);


    void goodsIsMarkTable(Long[] ids, String status);
}
