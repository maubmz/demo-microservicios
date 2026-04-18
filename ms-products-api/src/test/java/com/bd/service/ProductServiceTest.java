package com.bd.service;

import com.bd.model.Product;
import com.bd.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService (ms-products-api) Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1);
        sampleProduct.setName("Laptop");
        sampleProduct.setPrice(1500.00);
        sampleProduct.setStock(10);
    }

    @Test
    @DisplayName("saveProduct - persists and returns product")
    void saveProduct_persistsAndReturnsProduct() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.saveProduct(sampleProduct);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(1500.00, result.getPrice());
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    @DisplayName("findAll - returns all products")
    void findAll_returnsAllProducts() {
        List<Product> expected = List.of(sampleProduct);
        when(productRepository.findAll()).thenReturn(expected);

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findByProductId - existing id - returns product")
    void findByProductId_existingId_returnsProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.findByProductId(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("findByProductId - non-existing id - throws NoSuchElementException")
    void findByProductId_nonExistingId_throwsException() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> productService.findByProductId(99));
        verify(productRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("updateProductById - existing product - updates fields and saves")
    void updateProductById_existingProduct_updatesAndReturns() {
        Product updated = new Product();
        updated.setName("Gaming Laptop");
        updated.setPrice(2000.00);
        updated.setStock(5);

        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.updateProductById(1, updated);

        assertNotNull(result);
        assertEquals("Gaming Laptop", result.getName());
        assertEquals(2000.00, result.getPrice());
        assertEquals(5, result.getStock());
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    @DisplayName("deleteProductById - existing id - deletes and returns product")
    void deleteProductById_existingId_deletesAndReturns() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.deleteProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productRepository, times(1)).deleteById(1);
    }
}
