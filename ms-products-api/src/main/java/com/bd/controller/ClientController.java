package com.bd.controller;

import com.bd.model.Client;
import com.bd.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clientes")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Client>> findAllClients() {
        return ResponseEntity.ok(clientService.findAllClient());
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Client> findClientById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.findClientById(id));
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Client> updateClientById(@PathVariable("id") Integer id, @RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClientById(id, client));
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Client> deleteClientById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.deleteClientById(id));
    }

}
