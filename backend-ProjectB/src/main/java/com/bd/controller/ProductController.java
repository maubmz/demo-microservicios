package com.bd.controller;

import com.bd.model.Product;
import com.bd.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/productos")
    public List<Product> findAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/productos/{id}")
    public Product findProductById(@PathVariable Integer id) {
        return productService.findByProductId(id);
    }

    @PostMapping("/productos")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/productos/{id}")
    public Product updateProductbyId(@PathVariable Integer id,@RequestBody Product product) {
        return productService.updateProductById(id, product);
    }

    @DeleteMapping("/productos/{id}")
    public Product deleteProductById(@PathVariable Integer id) {
        return productService.deleteProductById(id);
    }

}
