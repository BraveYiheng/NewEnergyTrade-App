package com.yiheng.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiheng.springboot.domain.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}
