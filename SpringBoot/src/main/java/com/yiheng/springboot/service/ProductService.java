package com.yiheng.springboot.service;

import com.yiheng.springboot.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    public List<Product> searchProducts(Product searchForm);
    public List<Product> getAllProduct();
    List<Product> getProductsByUserId(Integer userId);
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(Integer productId);
}
