package com.java.service;

import com.java.client.feign.ClientFeignClient;
import com.java.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ClientService {
    /**
     * SIN OPEN FEIGN
    @Value("${microservicio.url}")
    public String url;
    RestTemplate restTemplate = new RestTemplate();
    public Object getAllClients() {
        return restTemplate.getForObject(url + "/api/clientes", Object.class);
    }
    **/

    @Autowired
    private ClientFeignClient clientFeignClient;

    public List<Client> getAllClients() {
        return clientFeignClient.getAllClients();
    }

    public Client getClientById(@PathVariable Integer id) {
        return clientFeignClient.getClientById(id);
    }

    public Client createClient(@RequestBody Client client) {
        return clientFeignClient.createClient(client);
    }

    public Client updateClientById(@PathVariable Integer id, @RequestBody Client client) {
        return clientFeignClient.updateClientById(id, client);
    }

    public Client deleteClientById(@PathVariable Integer id) {
        return clientFeignClient.deleteClientById(id);
    }

}
