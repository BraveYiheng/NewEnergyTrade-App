package com.yiheng.springboot.service.impl;

import com.yiheng.springboot.domain.Order;
import com.yiheng.springboot.mapper.OrderMapper;
import com.yiheng.springboot.service.OrderService;
import com.yiheng.springboot.util.OrderNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private OrderNumberGenerator orderNumberGenerator;

    @Override
    public List<Order> getAllOrdersWithRelations() {
        return orderMapper.findAllOrdersWithRelations();
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderMapper.findOrdersByUserId(userId);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public boolean deleteOrderById(Integer orderId) {
        return orderMapper.deleteById(orderId) > 0;
    }

    @Override
    public boolean addOrder(Order order) {
        String s = orderNumberGenerator.generateOrderNumber();
        order.setOrderNumber(s);
        return orderMapper.insert(order) > 0;
    }

    @Override
    public boolean updateOrderStatus(Order order) {
        return orderMapper.updateById(order)>0;
    }
} 