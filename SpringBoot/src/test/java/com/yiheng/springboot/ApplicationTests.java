package com.yiheng.springboot;

import com.yiheng.springboot.mapper.OrderMapper;
import com.yiheng.springboot.mapper.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Test
    void contextLoads() {

        System.out.println(orderMapper.selectOrdersByUserIdWithRelations(2));
    }

}
