package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Override
    public void add(SeckillGoods seckillGoods) {

    }

    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page,rows);
        /*SeckillGoodsQuery query = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = query.createCriteria();
        //状态未审核"0"
        if (null!=seckillGoods.getStatus()&&!"".equals(seckillGoods.getStatus())){
            criteria.andStatusEqualTo("0");
        }
        criteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间
        criteria.andEndTimeGreaterThan(new Date());//结束时间大于当前时间*/
        Page<SeckillGoods> p = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(null);
        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public SeckillGoods findOne(Long id) {
        return null;
    }

    @Override
    public void update(SeckillGoods seckillGoods) {

    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setStatus(status);
        for (Long id : ids) {
            seckillGoods.setId(id);
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
        }
    }

    @Override
    public void delete(Long[] ids) {

    }
}
