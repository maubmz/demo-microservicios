package com.bd.service;

import com.bd.model.Client;
import com.bd.model.Product;
import com.bd.model.Purchase;
import com.bd.repository.ClientRepository;
import com.bd.repository.ProductRepository;
import com.bd.repository.PurchasesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseService {
    private final PurchasesRepository purchasesRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public PurchaseService(PurchasesRepository purchasesRepository, ProductRepository productRepository, ClientRepository clientRepository) {
        this.purchasesRepository = purchasesRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public Purchase savePurchase(Purchase purchase) {
        purchase.setClient(clientRepository.findById(purchase.getClient().getId()).orElseThrow());

        List<Product> products = purchase.getProducts().stream()
                .map(p -> productRepository.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("No se encontro el producto " + p.getId()))
                ).toList();

        purchase.setProducts(products);
        purchase.setQuantity(purchase.getProducts().size());

        return purchasesRepository.save(purchase);
    }

    @Transactional
    public Purchase updatePurchaseById(Integer id, Purchase purchase) {
        Purchase oldPurchase = purchasesRepository.findById(id).orElseThrow();

        Client client = clientRepository.findById(purchase.getClient().getId()).orElseThrow();

        List<Product> products = purchase.getProducts().stream()
                .map(p -> productRepository.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("No se encontro el producto " + p.getId()))
                ).toList();
        purchase.setProducts(products);

        oldPurchase.setClient(client);
        oldPurchase.getProducts().clear();
        oldPurchase.getProducts().addAll(products);
        oldPurchase.setQuantity(purchase.getProducts().size());

        return purchasesRepository.save(oldPurchase);
    }

    public Purchase findPurchaseById(Integer id) {
        return purchasesRepository.findByIdPurchase(id).orElseThrow();
    }

    public List<Purchase> findAll() {
        return purchasesRepository.findAllPurchases();
    }

    public Purchase deletePurchaseById(Integer id) {
        Purchase purchase = purchasesRepository.findByIdPurchase(id).orElseThrow();
        purchasesRepository.deleteById(id);
        return purchase;
    }

}
