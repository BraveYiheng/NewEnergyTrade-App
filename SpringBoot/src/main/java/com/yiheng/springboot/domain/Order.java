package com.yiheng.springboot.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

@Data
@TableName("`order`")//order 特殊字段
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private Integer purchaserId;
    private Integer supplierId;
    private Integer productId;
    private String orderNumber;
    private Integer quantity;//数量
    private Double unitPrice;
    private Double orderAmount;//总价格
    private Integer orderStatus;
    private Date createTime;
    private Date updateTime;
    private String categoryName;

    @TableField(exist = false)
    private User purchaser;
    // 供应商信息（可通过关联查询填充)
    @TableField(exist = false)
    private User supplier;
    // 产品信息（可通过关联查询填充)
    @TableField(exist = false)
    private Product product;


}