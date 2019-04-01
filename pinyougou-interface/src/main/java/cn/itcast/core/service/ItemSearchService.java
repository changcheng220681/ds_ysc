package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    Map<String,Object> search(Map<String, String> searchMap);
    void importList(List<Item> list);

    void deleteSearch(List goodsId);

    void importSolrDbById(Long id);
}
