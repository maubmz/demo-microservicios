package com.java.controller;

import com.java.model.Purchase;
import com.java.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/allPurchases")
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/{id}")
    public Purchase getPurchaseById(@PathVariable Integer id) {
        return purchaseService.getPurchaseById(id);
    }

    @PostMapping("/create")
    public Purchase createPurchase(@RequestBody Purchase purchase) {
        return purchaseService.createPurchase(purchase);
    }

    @PutMapping("/update/{id}")
    public Purchase updatePurchaseById(@PathVariable Integer id, @RequestBody Purchase purchase) {
        return purchaseService.updatePurchase(id, purchase);
    }

    @DeleteMapping("/delete/{id}")
    public Purchase deletePurchaseById(@PathVariable Integer id) {
        return purchaseService.deletePurchase(id);
    }

}
