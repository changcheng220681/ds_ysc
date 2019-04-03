package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.order.Order;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Override
    public void add(Order order) {

    }

    @Override
    public PageResult search(Integer page, Integer rows, Order order) {
        PageHelper.startPage(page,rows);
        Page<Order> p= (Page<Order>) orderDao.selectByExample(null);
        return new PageResult(p.getTotal(),p.getResult());
    }
}
