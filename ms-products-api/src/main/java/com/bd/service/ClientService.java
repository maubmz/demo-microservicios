package com.bd.service;

import com.bd.model.Client;
import com.bd.model.Purchase;
import com.bd.repository.ClientRepository;
import com.bd.repository.PurchasesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final PurchasesRepository purchasesRepository;

    public ClientService(ClientRepository clientRepository, PurchasesRepository purchasesRepository) {
        this.clientRepository = clientRepository;
        this.purchasesRepository = purchasesRepository;
    }

    public Client saveClient(Client client) {
        Set<Purchase> purchases = client.getPurchases().stream()
                .map(p -> purchasesRepository.findByIdPurchase(p.getId())
                                .orElseThrow(() -> new RuntimeException("No se encontro la compra " + p.getId())))
                .collect(Collectors.toSet());
        client.setPurchases(purchases);
        return clientRepository.save(client);
    }

    public List<Client> findAllClient() {
        return clientRepository.findAllWithPurchases();
    }

    public Client findClientById(Integer id) {
        return clientRepository.findByIdWithPurchases(id).orElseThrow();
    }

    public Client updateClientById(Integer id, Client client) {
        Client oldClient = clientRepository.findByIdWithPurchases(id).orElseThrow(() -> new RuntimeException("No se encontro el client con id: " + id));

        Set<Purchase> purchases = client.getPurchases().stream()
                .map(p -> purchasesRepository.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("No se encontro ninguna compra " + p.getId())))
                .collect(Collectors.toSet());

        oldClient.setName(client.getName());
        oldClient.setEmail(client.getEmail());
        oldClient.setPhoneNumber(client.getPhoneNumber());
        oldClient.setPurchases(purchases);

        return clientRepository.save(oldClient);
    }

    public Client deleteClientById(Integer id) {
        Client client = clientRepository.findById(id).orElseThrow();
        clientRepository.deleteById(id);
        return client;
    }



}
