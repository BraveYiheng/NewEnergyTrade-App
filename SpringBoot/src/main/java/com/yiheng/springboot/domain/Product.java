package com.yiheng.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.xml.transform.sax.SAXResult;
import java.util.Date;

@Data
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId;
    private String productName;
    private Integer productCategoryId;//关联类别id
    private String technicalParameters;
    private String productDescription;
    private String imagePath;
    private Double price;
    private Integer stockQuantity;
    private Integer supplierId;//关联供应商id
    private Date createTime;

    @TableField(exist = false)
    private String categoryName;//类别名称
    @TableField(exist = false)
    private String supplierName;//供应商名称
    @TableField(exist = false)
    private String companyName;
    @TableField(exist = false)
    private String contactPhone;
    @TableField(exist = false)
    private String email;

    @TableField(exist = false)
    private Double minPrice;
    @TableField(exist = false)
    private Double maxPrice;
    @TableField(exist = false)
    private Integer selectedQuantity;



}