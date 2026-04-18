package com.bd.service;

import com.bd.model.Client;
import com.bd.model.Product;
import com.bd.model.Purchase;
import com.bd.repository.ClientRepository;
import com.bd.repository.ProductRepository;
import com.bd.repository.PurchasesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseService (ms-products-api) Unit Tests")
class PurchaseServiceTest {

    @Mock
    private PurchasesRepository purchasesRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    private Client sampleClient;
    private Product sampleProduct;
    private Purchase samplePurchase;

    @BeforeEach
    void setUp() {
        sampleClient = new Client();
        sampleClient.setId(1);
        sampleClient.setName("John Doe");

        sampleProduct = new Product();
        sampleProduct.setId(10);
        sampleProduct.setName("Laptop");
        sampleProduct.setPrice(1500.00);
        sampleProduct.setStock(5);

        samplePurchase = new Purchase();
        samplePurchase.setId(100);
        samplePurchase.setClient(sampleClient);
        samplePurchase.setProducts(new ArrayList<>(List.of(sampleProduct)));
        samplePurchase.setQuantity(1);
    }

    @Test
    @DisplayName("savePurchase - valid client and products - saves and returns purchase")
    void savePurchase_withValidClientAndProducts_savesSuccessfully() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(sampleClient));
        when(productRepository.findById(10)).thenReturn(Optional.of(sampleProduct));
        when(purchasesRepository.save(samplePurchase)).thenReturn(samplePurchase);

        Purchase result = purchaseService.savePurchase(samplePurchase);

        assertNotNull(result);
        assertEquals(1, result.getQuantity());
        verify(clientRepository, times(1)).findById(1);
        verify(productRepository, times(1)).findById(10);
        verify(purchasesRepository, times(1)).save(samplePurchase);
    }

    @Test
    @DisplayName("savePurchase - product not found - throws RuntimeException")
    void savePurchase_withInvalidProduct_throwsRuntimeException() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(sampleClient));
        when(productRepository.findById(10)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> purchaseService.savePurchase(samplePurchase));

        assertTrue(ex.getMessage().contains("10"));
        verify(purchasesRepository, never()).save(any());
    }

    @Test
    @DisplayName("findPurchaseById - existing id - returns purchase")
    void findPurchaseById_existingId_returnsPurchase() {
        when(purchasesRepository.findByIdPurchase(100)).thenReturn(Optional.of(samplePurchase));

        Purchase result = purchaseService.findPurchaseById(100);

        assertNotNull(result);
        assertEquals(100, result.getId());
        verify(purchasesRepository, times(1)).findByIdPurchase(100);
    }

    @Test
    @DisplayName("findAll - returns all purchases")
    void findAll_returnsAllPurchases() {
        List<Purchase> expected = List.of(samplePurchase);
        when(purchasesRepository.findAllPurchases()).thenReturn(expected);

        List<Purchase> result = purchaseService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchasesRepository, times(1)).findAllPurchases();
    }

    @Test
    @DisplayName("deletePurchaseById - existing id - deletes and returns purchase")
    void deletePurchaseById_existingId_deletesAndReturns() {
        when(purchasesRepository.findByIdPurchase(100)).thenReturn(Optional.of(samplePurchase));

        Purchase result = purchaseService.deletePurchaseById(100);

        assertNotNull(result);
        assertEquals(100, result.getId());
        verify(purchasesRepository, times(1)).deleteById(100);
    }
}
