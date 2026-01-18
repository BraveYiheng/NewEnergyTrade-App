package com.yiheng.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("product_categories")
public class ProductCategory {
    @TableId(type = IdType.AUTO)
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
    private String imageUrl;
    private Date createTime;
    private Date updateTime;
    private Boolean isActive;

}