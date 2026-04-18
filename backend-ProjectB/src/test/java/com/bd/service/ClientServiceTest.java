package com.bd.service;

import com.bd.model.Client;
import com.bd.model.Purchase;
import com.bd.repository.ClientRepository;
import com.bd.repository.PurchasesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientService (ms-products-api) Unit Tests")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PurchasesRepository purchasesRepository;

    @InjectMocks
    private ClientService clientService;

    private Client sampleClient;
    private Purchase samplePurchase;

    @BeforeEach
    void setUp() {
        samplePurchase = new Purchase();
        samplePurchase.setId(10);

        sampleClient = new Client();
        sampleClient.setId(1);
        sampleClient.setName("John Doe");
        sampleClient.setEmail("john@example.com");
        sampleClient.setPhoneNumber("5551234567");
        sampleClient.setPurchases(new HashSet<>(Set.of(samplePurchase)));
    }

    @Test
    @DisplayName("saveClient - valid purchases - saves and returns client")
    void saveClient_withValidPurchases_savesSuccessfully() {
        when(purchasesRepository.findByIdPurchase(10)).thenReturn(Optional.of(samplePurchase));
        when(clientRepository.save(sampleClient)).thenReturn(sampleClient);

        Client result = clientService.saveClient(sampleClient);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(purchasesRepository, times(1)).findByIdPurchase(10);
        verify(clientRepository, times(1)).save(sampleClient);
    }

    @Test
    @DisplayName("saveClient - purchase not found - throws RuntimeException")
    void saveClient_withInvalidPurchase_throwsRuntimeException() {
        when(purchasesRepository.findByIdPurchase(10)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> clientService.saveClient(sampleClient));

        assertTrue(ex.getMessage().contains("10"));
        verify(clientRepository, never()).save(any());
    }

    @Test
    @DisplayName("findAllClient - returns all clients")
    void findAllClient_returnsAllClients() {
        List<Client> expected = List.of(sampleClient);
        when(clientRepository.findAllWithPurchases()).thenReturn(expected);

        List<Client> result = clientService.findAllClient();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientRepository, times(1)).findAllWithPurchases();
    }

    @Test
    @DisplayName("findClientById - existing id - returns client")
    void findClientById_existingId_returnsClient() {
        when(clientRepository.findByIdWithPurchases(1)).thenReturn(Optional.of(sampleClient));

        Client result = clientService.findClientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(clientRepository, times(1)).findByIdWithPurchases(1);
    }

    @Test
    @DisplayName("updateClientById - valid data - updates and returns client")
    void updateClientById_withValidData_updatesClient() {
        Client updatedData = new Client();
        updatedData.setName("Jane Doe");
        updatedData.setEmail("jane@example.com");
        updatedData.setPhoneNumber("5559876543");
        updatedData.setPurchases(new HashSet<>(Set.of(samplePurchase)));

        when(clientRepository.findByIdWithPurchases(1)).thenReturn(Optional.of(sampleClient));
        when(purchasesRepository.findById(10)).thenReturn(Optional.of(samplePurchase));
        when(clientRepository.save(sampleClient)).thenReturn(sampleClient);

        Client result = clientService.updateClientById(1, updatedData);

        assertNotNull(result);
        verify(clientRepository, times(1)).save(sampleClient);
    }

    @Test
    @DisplayName("deleteClientById - existing id - deletes and returns client")
    void deleteClientById_existingId_deletesAndReturns() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(sampleClient));

        Client result = clientService.deleteClientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(clientRepository, times(1)).deleteById(1);
    }
}
