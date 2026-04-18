package com.java.service;

import com.java.client.feign.PurchaseFeignClient;
import com.java.model.Purchase;
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
@DisplayName("PurchaseService (ms-openfeign-client) Unit Tests")
class PurchaseServiceTest {

    @Mock
    private PurchaseFeignClient purchaseFeignClient;

    @InjectMocks
    private PurchaseService purchaseService;

    private Purchase samplePurchase;

    @BeforeEach
    void setUp() {
        samplePurchase = new Purchase();
        samplePurchase.setId(1);
    }

    @Test
    @DisplayName("getAllPurchases - returns list from FeignClient")
    void getAllPurchases_returnsList() {
        List<Purchase> expected = List.of(samplePurchase);
        when(purchaseFeignClient.getAllPurchases()).thenReturn(expected);

        List<Purchase> result = purchaseService.getAllPurchases();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchaseFeignClient, times(1)).getAllPurchases();
    }

    @Test
    @DisplayName("getPurchaseById - returns purchase when found")
    void getPurchaseById_returnsPurchase() {
        when(purchaseFeignClient.getPurchaseById(1)).thenReturn(samplePurchase);

        Purchase result = purchaseService.getPurchaseById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(purchaseFeignClient, times(1)).getPurchaseById(1);
    }

    @Test
    @DisplayName("createPurchase - delegates creation to FeignClient")
    void createPurchase_returnsCreatedPurchase() {
        when(purchaseFeignClient.createPurchase(samplePurchase)).thenReturn(samplePurchase);

        Purchase result = purchaseService.createPurchase(samplePurchase);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(purchaseFeignClient, times(1)).createPurchase(samplePurchase);
    }

    @Test
    @DisplayName("updatePurchase - delegates update to FeignClient")
    void updatePurchase_returnsUpdatedPurchase() {
        Purchase updated = new Purchase();
        updated.setId(1);
        when(purchaseFeignClient.updatePurchaseById(1, updated)).thenReturn(updated);

        Purchase result = purchaseService.updatePurchase(1, updated);

        assertNotNull(result);
        verify(purchaseFeignClient, times(1)).updatePurchaseById(1, updated);
    }

    @Test
    @DisplayName("deletePurchase - delegates deletion to FeignClient")
    void deletePurchase_returnsDeletedPurchase() {
        when(purchaseFeignClient.deletePurchaseById(1)).thenReturn(samplePurchase);

        Purchase result = purchaseService.deletePurchase(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(purchaseFeignClient, times(1)).deletePurchaseById(1);
    }
}
