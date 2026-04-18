package com.java.service;

import com.java.client.feign.ProductFeignClient;
import com.java.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService (ms-openfeign-client) Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductFeignClient productFeignClient;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1);
        sampleProduct.setName("Laptop");
    }

    @Test
    @DisplayName("getAllProducts - returns list from FeignClient")
    void getAllProducts_returnsList() {
        List<Product> expected = List.of(sampleProduct);
        when(productFeignClient.getAllProducts()).thenReturn(expected);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(productFeignClient, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("getProductById - returns product when found")
    void getProductById_returnsProduct() {
        when(productFeignClient.getProductById(1)).thenReturn(sampleProduct);

        Product result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productFeignClient, times(1)).getProductById(1);
    }

    @Test
    @DisplayName("saveProduct - delegates creation to FeignClient")
    void saveProduct_returnsCreatedProduct() {
        when(productFeignClient.createProduct(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.saveProduct(sampleProduct);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productFeignClient, times(1)).createProduct(sampleProduct);
    }

    @Test
    @DisplayName("updateProductById - delegates update to FeignClient")
    void updateProductById_returnsUpdatedProduct() {
        Product updated = new Product();
        updated.setId(1);
        updated.setName("Gaming Laptop");
        when(productFeignClient.updateProductById(1, updated)).thenReturn(updated);

        Product result = productService.updateProductById(updated, 1);

        assertNotNull(result);
        assertEquals("Gaming Laptop", result.getName());
        verify(productFeignClient, times(1)).updateProductById(1, updated);
    }

    @Test
    @DisplayName("deleteProductById - delegates deletion to FeignClient")
    void deleteProductById_returnsDeletedProduct() {
        when(productFeignClient.deleteProductById(1)).thenReturn(sampleProduct);

        Product result = productService.deletePRoductById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productFeignClient, times(1)).deleteProductById(1);
    }
}
