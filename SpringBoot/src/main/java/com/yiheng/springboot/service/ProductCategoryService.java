package com.yiheng.springboot.service;

import com.yiheng.springboot.domain.ProductCategory;
import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getAllCategories();
    ProductCategory getCategoryById(Integer categoryId);
    boolean addCategory(ProductCategory category);
    boolean updateCategory(ProductCategory category);
    boolean deleteCategory(Integer categoryId);
}