package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.UserDataHandler;
import vo.OrderVo;

import javax.jms.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 */
@Service
@Transactional
public class UserServiceImpl implements  UserService {


    //发消息 Map
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Destination smsDestination;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private SellerDao sellerDao;

    @Override
    public void sendCode(final String phone) {
        //1:生成验证码
        final String randomNumeric = RandomStringUtils.randomNumeric(6);
        //2:保存验证码到缓存中
        redisTemplate.boundValueOps(phone).set(randomNumeric);
        //redisTemplate.boundValueOps(phone).expire(1, TimeUnit.MINUTES);
        //2:发消息 Map
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage map = session.createMapMessage();
                //手机号
                map.setString("iphone",phone);//"17801040609"
                //验证码
                map.setString("templateParam","{'number':'"+randomNumeric+"'}");
                //签名
                map.setString("signName","品优购商城");
                //模板ID
                map.setString("templateCode","SMS_126462276");


                return map;
            }
        });
    }

    //添加
    @Override
    public void add(User user, String smscode) {
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        //判断验证码是否失效
        if(null == code){
            throw new RuntimeException("验证码失败");
        }

        if(code.equals(smscode)){
            //保存用户信息
            user.setCreated(new Date());
            user.setUpdated(new Date());
            //密码加密


            userDao.insertSelective(user);


        }else{
            throw new RuntimeException("验证码不正确");
        }
    }

    @Override
    public List<OrderVo> findByOrderList(String status,String name) {
        List<OrderVo> orderVoList = new ArrayList<>();
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria().andUserIdEqualTo(name);
        if (!"0".equals(status)){
            criteria.andStatusEqualTo(status);
        }
        List<Order> orderList = orderDao.selectByExample(orderQuery);
        if (orderList==null || orderList.size()==0){
            return null;
        }
        for (Order order : orderList) {
            OrderVo orderVo = new OrderVo();
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());
            List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
            Seller seller = sellerDao.selectByPrimaryKey(order.getSellerId());
            orderVo.setNickName(seller.getNickName());
            orderVo.setOrder(order);
            for (OrderItem orderItem : orderItemList) {
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                String itemSpec = item.getSpec();
                Map<String,String> map = JSON.parseObject(itemSpec, Map.class);
                StringBuffer stringBuffer = new StringBuffer();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    stringBuffer.append(entry.getKey()+" "+entry.getValue()+" ");
                }
                orderItem.setSpec(stringBuffer.toString());
            }
            orderVo.setOrderItemList(orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;

    }
}
