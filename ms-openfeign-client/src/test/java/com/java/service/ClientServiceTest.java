package com.java.service;

import com.java.client.feign.ClientFeignClient;
import com.java.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientService (ms-openfeign-client) Unit Tests")
class ClientServiceTest {

    @Mock
    private ClientFeignClient clientFeignClient;

    @InjectMocks
    private ClientService clientService;

    private Client sampleClient;

    @BeforeEach
    void setUp() {
        sampleClient = new Client();
        sampleClient.setId(1);
        sampleClient.setName("John Doe");
        sampleClient.setEmail("john@example.com");
    }

    @Test
    @DisplayName("getAllClients - returns list from FeignClient")
    void getAllClients_returnsList() {
        List<Client> expected = List.of(sampleClient);
        when(clientFeignClient.getAllClients()).thenReturn(expected);

        List<Client> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(clientFeignClient, times(1)).getAllClients();
    }

    @Test
    @DisplayName("getClientById - returns client when found")
    void getClientById_returnsClient() {
        when(clientFeignClient.getClientById(1)).thenReturn(sampleClient);

        Client result = clientService.getClientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John Doe", result.getName());
        verify(clientFeignClient, times(1)).getClientById(1);
    }

    @Test
    @DisplayName("createClient - delegates creation to FeignClient")
    void createClient_returnsCreatedClient() {
        when(clientFeignClient.createClient(sampleClient)).thenReturn(sampleClient);

        Client result = clientService.createClient(sampleClient);

        assertNotNull(result);
        assertEquals(sampleClient.getEmail(), result.getEmail());
        verify(clientFeignClient, times(1)).createClient(sampleClient);
    }

    @Test
    @DisplayName("updateClientById - delegates update to FeignClient")
    void updateClientById_returnsUpdatedClient() {
        Client updated = new Client();
        updated.setId(1);
        updated.setName("Jane Doe");
        updated.setEmail("jane@example.com");
        when(clientFeignClient.updateClientById(1, updated)).thenReturn(updated);

        Client result = clientService.updateClientById(1, updated);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(clientFeignClient, times(1)).updateClientById(1, updated);
    }

    @Test
    @DisplayName("deleteClientById - delegates deletion to FeignClient")
    void deleteClientById_returnsDeletedClient() {
        when(clientFeignClient.deleteClientById(1)).thenReturn(sampleClient);

        Client result = clientService.deleteClientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(clientFeignClient, times(1)).deleteClientById(1);
    }
}
