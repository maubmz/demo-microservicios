package com.java.client.feign;

import com.java.model.Purchase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "purchase-service", url = "${microservicio.url}")
public interface PurchaseFeignClient {

    @GetMapping("/api/compras")
    List<Purchase> getAllPurchases();

    @GetMapping("/api/compras/{id}")
    Purchase getPurchaseById(@PathVariable Integer id);

    @PostMapping("/api/compras")
    Purchase createPurchase(@RequestBody Purchase purchase);

    @PutMapping("/api/compras/{id}")
    Purchase updatePurchaseById(@PathVariable Integer id, @RequestBody Purchase purchase);

    @DeleteMapping("/api/compras/{id}")
    Purchase deletePurchaseById(@PathVariable Integer id);

}
