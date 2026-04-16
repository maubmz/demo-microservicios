package com.java.service;

import com.java.client.feign.PurchaseFeignClient;
import com.java.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseFeignClient purchaseFeignClient;

    public List<Purchase> getAllPurchases() {
        return purchaseFeignClient.getAllPurchases();
    }

    public Purchase getPurchaseById(Integer id) {
        return purchaseFeignClient.getPurchaseById(id);
    }

    public Purchase createPurchase(@RequestBody Purchase purchase) {
        return purchaseFeignClient.createPurchase(purchase);
    }

    public Purchase updatePurchase(Integer id, Purchase purchase) {
        return purchaseFeignClient.updatePurchaseById(id, purchase);
    }

    public Purchase deletePurchase(Integer id) {
        return purchaseFeignClient.deletePurchaseById(id);
    }

}
