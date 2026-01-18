package com.yiheng.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiheng.springboot.domain.Order;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface OrderMapper extends BaseMapper<Order> {  //xml注解的方式
    // 使用MyBatis - Plus的关联查询，查询订单及相关的采购商、供应商和产品信息
    List<Order> findAllOrdersWithRelations();
    List<Order> findOrdersByUserId(Integer userId);


}

