package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.service.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Override
    public void pageDetail(Long goodsId) {
        Map<String,Object> root=new HashMap<>(16);

        //根据goodsId获得商品相关信息
        ItemQuery example=new ItemQuery();
        ItemQuery.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        criteria.andStatusEqualTo("1");
        root.put("itemList",itemDao.selectByExample(example));
        //添加goods商品对象到rootMap
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        root.put("goods",goods);
        //添加商品详情表到rootMap
        root.put("goodsDesc",goodsDescDao.selectByPrimaryKey(goodsId));
        root.put("itemCat1",itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
        root.put("itemCat2",itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
        root.put("itemCat3",itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());

        Writer out=null;
        try {
            //freemark
            Configuration conf = freeMarkerConfigurer.getConfiguration();
            Template template = conf.getTemplate("item.ftl");
            String realPath=getRealPath(goodsId+".html");
            out=new OutputStreamWriter(new FileOutputStream(realPath),"UTF-8");

            template.process(root,out);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean deleteItemHtml(Long[] ids) {
        try {
            for (Long id : ids) {
                String realPath = getRealPath(id + ".html");
                new File(realPath).delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private ServletContext servletContext;

    private String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;

    }
}
