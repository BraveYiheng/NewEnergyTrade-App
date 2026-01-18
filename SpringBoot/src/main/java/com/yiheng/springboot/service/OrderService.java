package com.yiheng.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiheng.springboot.domain.Order;
import com.yiheng.springboot.domain.Product;

import java.util.List;

public interface OrderService {
    public List<Order> getAllOrdersWithRelations();
    public Order getOrderById(Integer orderId);
    public List<Order> getOrdersByUserId(Integer userId);
    public boolean deleteOrderById(Integer orderId);
    boolean addOrder(Order order);
    boolean updateOrderStatus(Order order);
}
