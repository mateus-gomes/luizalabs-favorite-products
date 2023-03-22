package com.mateusgomes.luizalabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusgomes.luizalabs.data.domain.UserData;
import com.mateusgomes.luizalabs.data.model.Client;
import com.mateusgomes.luizalabs.data.domain.Meta;
import com.mateusgomes.luizalabs.data.domain.PageableClientList;
import com.mateusgomes.luizalabs.service.AuthService;
import com.mateusgomes.luizalabs.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("User")
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /clients - find all methods")
    class FindAllMethods {
        @Test
        @DisplayName("Should return 200 when is successful and there are clients registered")
        void findAllClientsOk() throws Exception {
            int pageNumber = 0;
            Client client = new Client();

            when(clientService.findAll(pageNumber)).thenReturn(
                    new PageableClientList(new Meta(pageNumber, 10), List.of(client))
            );

            mockMvc.perform(get(String.format("/clients?page=%d", pageNumber))).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 400 when no page is send in the request")
        void findAllClientsBadRequest() throws Exception {
            mockMvc.perform(get("/clients"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("No page was provided"));
        }

        @Test
        @DisplayName("Should return 403 when user is not authenticated")
        @WithAnonymousUser
        void findAllClientsForbidden() throws Exception {
            int pageNumber = 0;

            mockMvc.perform(get(String.format("/clients?page=%d", pageNumber))).andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 404 when is successful but there are no clients registered")
        void findAllClientsNoContent() throws Exception {
            int pageNumber = 0;

            when(clientService.findAll(pageNumber)).thenReturn(
                    new PageableClientList(new Meta(pageNumber, 10), new ArrayList<>())
            );

            mockMvc.perform(get("/clients?page=0")).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /clients/{idClient} - find by id")
    class FindByIdMethods {
        @Test
        @DisplayName("Should return 200 when is successful and there is a client registered")
        void findClientByIdOk() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            Client client = new Client();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.findById(uuid)).thenReturn(Optional.of(client));

            mockMvc.perform(get("/clients/{idClient}", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 401 when user tries to read another user's information")
        void findClientByIdUnauthorized() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            Client client = new Client();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(false);

            mockMvc.perform(get("/clients/{idClient}", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 404 when is successful but there is no client registered")
        void findClientByIdNoContent() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);

            mockMvc.perform(get("/clients/{idClient}", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /clients - create client")
    class CreateMethods{
        @Test
        @DisplayName("Should return 201 when client is successfully created")
        void createClientCreated() throws Exception {
            UserData userData = new UserData();
            userData.setClientName("Name");
            userData.setClientEmail("email@email.com");
            userData.setPassword("password");

            mockMvc.perform(post("/clients")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(userData)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should return 400 when email is already in use")
        void createClientBadRequest() throws Exception {
            UserData userData = new UserData();
            userData.setClientName("Name");
            userData.setClientEmail("email@email.com");
            userData.setPassword("password");

            when(clientService.existsByEmail(userData.getClientEmail())).thenReturn(true);

            mockMvc.perform(post("/clients")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(userData)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 422 when validation fail")
        void createClientUnprocessableEntity() throws Exception {
            UserData userData = new UserData();
            userData.setClientName("Name");
            userData.setClientEmail("email@email.com");

            mockMvc.perform(post("/clients")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(userData)))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    @DisplayName("DELETE /clients/{idClient} - delete by id")
    class DeleteMethods{
        @Test
        @DisplayName("Should return 204 when client register is deleted")
        void deleteClientNoContent() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.existsById(uuid)).thenReturn(true);

            mockMvc.perform(delete("/clients/{idClient}", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 400 when client does not exists")
        void deleteClientBadRequest() throws Exception {
            UUID uuid = UUID.randomUUID();

            mockMvc.perform(delete("/clients/{idClient}", uuid)).andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 401 when user tries to delete another user's information")
        void deleteClientUnauthorized() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            Client client = new Client();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(false);

            mockMvc.perform(delete("/clients/{idClient}", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("PUT /clients - update clients")
    class UpdateMethods{
        @Test
        @DisplayName("Should return 204 when client is successfully updated")
        void updateClientNoContent() throws Exception {
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
        @DisplayName("Should return 400 when client does not exists")
        void updateClientBadRequestClientDoesNotExists() throws Exception {
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
        @DisplayName("Should return 400 when trying to update to an existing email")
        void updateClientBadRequestEmailAlreadyInUse() throws Exception {
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
        @DisplayName("Should return 422 when client validation fail")
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
}
