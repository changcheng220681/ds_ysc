package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;

public interface OrderService {
    void add(Order order);
    PageResult search(Integer page, Integer rows, Order order);
}
