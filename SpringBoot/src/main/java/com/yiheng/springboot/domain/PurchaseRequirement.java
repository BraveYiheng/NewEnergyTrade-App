package com.yiheng.springboot.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class PurchaseRequirement {
    @TableId(type = IdType.AUTO)
    private Integer requirementId;
    private Integer purchaserId;
    private String productName;
    private String specificationRequirements;
    private Integer quantity;
    private Double expectedPrice;
    private Date deliveryTime;
    private Date createTime;
}
