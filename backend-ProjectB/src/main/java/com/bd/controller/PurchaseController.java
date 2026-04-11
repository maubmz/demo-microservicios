package com.bd.controller;

import com.bd.model.Purchase;
import com.bd.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/compras")
    public List<Purchase> getAllPurchases() {
        return purchaseService.findAll();
    }

    @GetMapping("/compras/{id}")
    public Purchase findPurchaseById(@PathVariable("id") Integer id) {
        return purchaseService.findPurchaseById(id);
    }

    @PostMapping("/compras")
    public Purchase createPurchase(@RequestBody Purchase purchase) {
        return purchaseService.savePurchase(purchase);
    }

    @PutMapping("/compras/{id}")
    public Purchase updatePurchase(@PathVariable("id") Integer id, @RequestBody Purchase purchase) {
        return purchaseService.updatePurchaseById(id, purchase);
    }

    @DeleteMapping("/compras/{id}")
    public Purchase deletePurchase(@PathVariable("id") Integer id) {
        return purchaseService.deletePurchaseById(id);
    }


}
