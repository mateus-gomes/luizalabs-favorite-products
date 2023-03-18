package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    @DisplayName("Should return true when client used as param is the same as the found client")
    void emailIsAvailableForUse() {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setIdClient(uuid);
        client.setClientEmail("mateus@gmail.com");

        when(clientRepository.findByClientEmail(client.getClientEmail())).thenReturn(Optional.of(client));

        boolean isEmailAvailableForUse = clientService.isEmailAvailableForUse(client);

        assertTrue(isEmailAvailableForUse);
    }

    @Test
    @DisplayName("Should return false when " +
            "client used as param is different from the found client and the both client email are the same"
    )
    void emailIsAlreadyInUse() {
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        Client client = new Client();
        Client client2 = new Client();
        client.setIdClient(uuid);
        client2.setIdClient(uuid2);
        client.setClientEmail("mateus@gmail.com");

        when(clientRepository.findByClientEmail(client.getClientEmail())).thenReturn(Optional.of(client2));

        boolean isEmailAvailableForUse = clientService.isEmailAvailableForUse(client);

        assertFalse(isEmailAvailableForUse);
    }

    @Test
    @DisplayName("Should return true when no client is found")
    void emailIsNotUsedByAnyone() {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setIdClient(uuid);
        client.setClientEmail("mateus@gmail.com");

        when(clientRepository.findByClientEmail(client.getClientEmail())).thenReturn(Optional.empty());

        boolean isEmailAvailableForUse = clientService.isEmailAvailableForUse(client);

        assertTrue(isEmailAvailableForUse);
    }
}
