package com.yiheng.springboot.service.impl;

import com.yiheng.springboot.mapper.ProductMapper;
import com.yiheng.springboot.domain.Product;
import com.yiheng.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Override
    public List<Product> searchProducts(Product searchForm) {
        return productMapper.searchProducts(searchForm);
    }

    @Override
    public List<Product> getAllProduct() {
        return productMapper.selectAll();
    }

    @Override
    public List<Product> getProductsByUserId(Integer userId) {
       return productMapper.selectBySupplierId(userId);
    }


    @Override
    public boolean addProduct(Product product) {
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean updateProduct(Product product) {
        return productMapper.updateById(product) > 0;
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        return productMapper.deleteById(productId) > 0;
    }
}
