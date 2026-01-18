package com.yiheng.springboot.control;

import com.yiheng.springboot.domain.Product;
import com.yiheng.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestBody Product searchForm) {
        searchForm.setProductName(searchForm.getProductName());
        List<Product> products = productService.searchProducts(searchForm);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    // 根据用户ID获取产品列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable Integer userId) {
        List<Product> products = productService.getProductsByUserId(userId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    // 添加产品
    @PostMapping("/add")
    public ResponseEntity<Boolean> addProduct(@RequestBody Product product) {
        boolean result = productService.addProduct(product);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    // 更新产品
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateProduct(@RequestBody Product product) {
        boolean result = productService.updateProduct(product);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    // 删除产品
    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Integer productId) {
        boolean result = productService.deleteProduct(productId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




}
