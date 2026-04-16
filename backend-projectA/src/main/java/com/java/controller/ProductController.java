package com.java.controller;

import com.java.model.Product;
import com.java.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/allProducts")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping("/create")
    public Product creatProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/update/{id}")
    public Product updateProdyct(@RequestBody Product product, @PathVariable Integer id) {
        return productService.updateProductById(product, id);
    }

    @DeleteMapping("/delete/{id}")
    public Product deletePRoductById(@PathVariable Integer id) {
        return productService.deletePRoductById(id);
    }

}
