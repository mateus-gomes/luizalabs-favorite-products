package com.mateusgomes.luizalabs.controller;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    @DisplayName("GET /clients - Should return 200 when is successful and there are clients registered")
    void findAllClientsWithContent() throws Exception {
        Client client = new Client();

        when(clientService.findAll()).thenReturn(List.of(client));

        mockMvc.perform(get("/clients")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /clients - Should return 204 when is successful but there are no clients registered")
    void findAllClientsWithNoContent() throws Exception {
        mockMvc.perform(get("/clients")).andExpect(status().isNoContent());
    }
}
