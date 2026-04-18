package com.java.service;

import com.java.client.feign.ClientFeignClient;
import com.java.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    /**
     * SIN OPEN FEIGN
     * @Value("${microservicio.url}")
     * public String url;
     * RestTemplate restTemplate = new RestTemplate();
     * public Object getAllClients() {
     *     return restTemplate.getForObject(url + "/api/clientes", Object.class);
     * }
     */

    private final ClientFeignClient clientFeignClient;

    public ClientService(ClientFeignClient clientFeignClient) {
        this.clientFeignClient = clientFeignClient;
    }

    public List<Client> getAllClients() {
        return clientFeignClient.getAllClients();
    }

    public Client getClientById(Integer id) {
        return clientFeignClient.getClientById(id);
    }

    public Client createClient(Client client) {
        return clientFeignClient.createClient(client);
    }

    public Client updateClientById(Integer id, Client client) {
        return clientFeignClient.updateClientById(id, client);
    }

    public Client deleteClientById(Integer id) {
        return clientFeignClient.deleteClientById(id);
    }

}
