package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @创建人 cc
 * @创建时间 2019/4/3
 * @描述
 */
@Service
public class PayLogServiceImpl implements PayLogService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Override
    public void add(Long orderId) {
        //收货人  地址  手机  支付方式
        Order order = orderDao.selectByPrimaryKey(orderId);
        //很多订单的金额之和
        //订单ID集合
        List<Long> ids = new ArrayList<>();
        ids.add(orderId);
        //日志表  （将上面的所有订单合并起来  一起付钱）
        PayLog payLog = new PayLog();
        //ID
        payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
        //日志产生 时间
        payLog.setCreateTime(new Date());
        //总金额
        payLog.setTotalFee(order.getPayment().longValue()*100);

        //用户ID
        payLog.setUserId(order.getUserId());

        //支付状态
        payLog.setTradeState("0");

        //订单集合  [341,32414213,2131412]
        payLog.setOrderList(ids.toString().replace("[","").replace("]",""));
        //支付方式
        payLog.setPayType("1");

        payLogDao.insertSelective(payLog);
        //保存缓存一份
        redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);

    }
}
