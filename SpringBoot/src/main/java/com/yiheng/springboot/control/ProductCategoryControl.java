package com.yiheng.springboot.control;

import com.yiheng.springboot.domain.ProductCategory;
import com.yiheng.springboot.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productCategory")
public class ProductCategoryControl {
    @Autowired
    private ProductCategoryService productCategoryService;
    @RequestMapping("/all")
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        return new ResponseEntity<>(productCategoryService.getAllCategories(), HttpStatus.OK);

    }
    @RequestMapping("/{categoryId}")
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(productCategoryService.getCategoryById(categoryId), HttpStatus.OK);
    }
    @RequestMapping("/add")
    public ResponseEntity<Boolean> addProductCategory(@RequestBody ProductCategory productCategory) {
        return new ResponseEntity<>(productCategoryService.addCategory(productCategory), HttpStatus.OK);
    }
    @RequestMapping("/update")
    public ResponseEntity<Boolean> updateProductCategory(@RequestBody ProductCategory productCategory) {
        return new ResponseEntity<>(productCategoryService.updateCategory(productCategory), HttpStatus.OK);
    }
    @RequestMapping("/delete/{categoryId}")
    public ResponseEntity<Boolean> deleteProductCategory(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(productCategoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
