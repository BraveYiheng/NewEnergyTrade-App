package com.yiheng.springboot.control;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiheng.springboot.domain.Order;

import com.yiheng.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderControl {
    @Autowired
    OrderService orderService;
    @GetMapping("/all")
    public  ResponseEntity<List<Order>> getAllOrdersWithRelations(){
        List<Order> allOrders = orderService.getAllOrdersWithRelations();
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public  ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        Order orderById = orderService.getOrderById(orderId);
        return  new ResponseEntity<>(orderById, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public  ResponseEntity<List<Order>> getOrderByUserId(@PathVariable Integer userId) {
        List<Order> ordersByUserId = orderService.getOrdersByUserId(userId);
        return  new ResponseEntity<>(ordersByUserId, HttpStatus.OK);
    }
    @DeleteMapping("/{orderId}")
    public  ResponseEntity<Boolean> deleteOrderById(@PathVariable Integer orderId) {
        boolean b = orderService.deleteOrderById(orderId);
        return  new ResponseEntity<>(b ,HttpStatus.OK);
    }
    // 添加
    @PostMapping("/add")
    public ResponseEntity<Boolean> addOrder(@RequestBody Order order) {
        boolean result = orderService.addOrder(order);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<Boolean> updateOrderStatus(@RequestBody Order order) {
        boolean result = orderService.updateOrderStatus(order);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
