package com.bd.service;

import com.bd.model.Product;
import com.bd.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findByProductId(Integer id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product updateProductById(Integer id, Product product) {
        Product oldProduct = productRepository.findById(id).orElseThrow();
        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setStock(product.getStock());
        return productRepository.save(oldProduct);
    }

    public Product deleteProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.deleteById(id);
        return product;
    }

}
