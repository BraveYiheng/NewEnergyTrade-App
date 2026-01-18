package com.yiheng.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiheng.springboot.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
     List< Product> selectAll();
     List<Product> searchProducts(Product searchForm);
     List<Product> selectBySupplierId(Integer userId);


}
