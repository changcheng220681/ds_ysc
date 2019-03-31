package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import vo.OrderVo;

import java.util.List;

public interface UserService {
    void sendCode(String phone);

    void add(User user, String smscode);


    List<OrderVo> findByOrderList(String status,String name);
}

