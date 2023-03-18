package com.mateusgomes.luizalabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /clients - Should return 200 when is successful and there are clients registered")
    void findAllClientsWithContent() throws Exception {
        Client client = new Client();

        when(clientService.findAll()).thenReturn(List.of(client));

        mockMvc.perform(get("/clients")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /clients - Should return 204 when is successful but there are no clients registered")
    void findAllClientsNoContent() throws Exception {
        mockMvc.perform(get("/clients")).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /clients/{idClient} - Should return 200 when is successful and there is a client registered")
    void findByIdClient() throws Exception {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();

        when(clientService.findById(uuid)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/{idClient}", uuid)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /clients/{idClient} - Should return 204 when is successful but there is no client registered")
    void findByIdClientNoContent() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/clients/{idClient}", uuid)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /clients - Should return 201 when client is successfully created")
    void createClientSuccessfully() throws Exception {
        Client client = new Client();
        client.setClientName("Mateus");
        client.setClientEmail("mateus@gmail.com");

        mockMvc.perform(post("/clients")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /clients - Should return 400 when email is already in use")
    void createClientEmailAlreadyInUse() throws Exception {
        Client client = new Client();
        client.setClientName("Mateus");
        client.setClientEmail("mateus@gmail.com");

        when(clientService.existsByEmail(client.getClientEmail())).thenReturn(true);

        mockMvc.perform(post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /clients - Should return 422 when validation fail")
    void createClientUnprocessableEntity() throws Exception {
        Client client = new Client();
        client.setClientName("Mateus");

        mockMvc.perform(post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("DELETE /clients/{idClient} - Should return 400 when client does not exists")
    void deleteClientClientDoesNotExists() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/clients/{idClient}", uuid)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /clients/{idClient} - Should return 204 when client register is deleted")
    void deleteClientClientSuccessfully() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(clientService.existsById(uuid)).thenReturn(true);

        mockMvc.perform(delete("/clients/{idClient}", uuid)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /clients - Should return 204 when client is successfully updated")
    void updateClientSuccessfully() throws Exception {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setIdClient(uuid);
        client.setClientName("Mateus");
        client.setClientEmail("mateus@gmail.com");

        when(clientService.existsById(uuid)).thenReturn(true);

        when(clientService.isEmailAvailableForUse(any())).thenReturn(true);

        mockMvc.perform(put("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /clients - Should return 400 when client does not exists")
    void updateClientClientDoesNotExists() throws Exception {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setIdClient(uuid);
        client.setClientName("Mateus");
        client.setClientEmail("mateus@gmail.com");

        mockMvc.perform(put("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /clients - Should return 400 when trying to update to an existing email")
    void updateClientUpdateToExistingEmail() throws Exception {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setIdClient(uuid);
        client.setClientName("Mateus");
        client.setClientEmail("mateus@gmail.com");

        when(clientService.existsById(uuid)).thenReturn(true);

        mockMvc.perform(put("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /clients - Should return 422 when client validation fail")
    void updateClientUnprocessableEntity() throws Exception {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setIdClient(uuid);

        mockMvc.perform(put("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isUnprocessableEntity());
    }
}
