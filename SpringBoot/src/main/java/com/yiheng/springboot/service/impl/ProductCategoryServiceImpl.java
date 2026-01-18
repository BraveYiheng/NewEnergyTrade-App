package com.yiheng.springboot.service.impl;

import com.yiheng.springboot.domain.ProductCategory;
import com.yiheng.springboot.mapper.ProductCategoryMapper;
import com.yiheng.springboot.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
     ProductCategoryMapper productCategoryMapper;
    public ProductCategory getCategoryById(Integer categoryId) {
        return productCategoryMapper.selectById(categoryId);
    }
    public boolean addCategory(ProductCategory category) {
        return productCategoryMapper.insert(category) > 0;
    }
    public boolean updateCategory(ProductCategory category) {
        return productCategoryMapper.updateById(category) > 0;
    }
    public boolean deleteCategory(Integer categoryId) {
        return productCategoryMapper.deleteById(categoryId) > 0;
    }
    public List<ProductCategory> getAllCategories() {
        return productCategoryMapper.selectList(null);
    }
}
