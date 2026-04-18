package com.java.client.feign;

import com.java.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "client-service", url = "${microservicio.url}")
public interface ClientFeignClient {

    @GetMapping("/api/clientes")
    List<Client> getAllClients();

    @GetMapping("/api/clientes/{id}")
    Client getClientById(@PathVariable Integer id);

    @PostMapping("/api/clientes")
    Client createClient(@RequestBody Client client);

    @PutMapping("/api/clientes/{id}")
    Client updateClientById(@PathVariable Integer id, @RequestBody Client client);

    @DeleteMapping("/api/clientes/{id}")
    Client deleteClientById(@PathVariable Integer id);

}
