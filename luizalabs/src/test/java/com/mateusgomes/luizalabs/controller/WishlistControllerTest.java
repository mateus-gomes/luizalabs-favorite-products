package com.mateusgomes.luizalabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusgomes.luizalabs.data.domain.Meta;
import com.mateusgomes.luizalabs.data.domain.PageableProductList;
import com.mateusgomes.luizalabs.data.domain.ProductAPIResponse;
import com.mateusgomes.luizalabs.data.domain.ProductRequest;
import com.mateusgomes.luizalabs.data.model.*;
import com.mateusgomes.luizalabs.service.AuthService;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("User")
public class WishlistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /clients/{idClient}/wishlists - find wishlist from client by id")
    class FindWishlistByClient{
        @Test
        @DisplayName("Should return 400 when no page is send in the request")
        void findAllClientsBadRequest() throws Exception {
            UUID uuid = UUID.randomUUID();

            mockMvc.perform(get("/clients/{idClient}/wishlists", uuid))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("No page was provided"));
        }

        @Test
        @DisplayName("Should return 200 when the client exists but there are no products on the wishlist")
        void findWishlistByClientOk() throws Exception {
            String token = "BEARER_TOKEN";
            int pageNumber = 0;
            UUID uuid = UUID.randomUUID();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.existsById(uuid)).thenReturn(true);
            when(wishlistService.findByClientId(uuid, pageNumber)).thenReturn(
                    new PageableProductList(new Meta(pageNumber, 10), List.of(new Product()))
            );

            mockMvc.perform(get(String.format("/clients/{idClient}/wishlists?page=%d", pageNumber), uuid)
                    .header("Authorization", token))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 404 when the client exists but there are no products on the wishlist")
        void findWishlistByClientNoContent() throws Exception {
            String token = "BEARER_TOKEN";
            int pageNumber = 0;
            UUID uuid = UUID.randomUUID();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.existsById(uuid)).thenReturn(true);
            when(wishlistService.findByClientId(uuid, pageNumber)).thenReturn(
                    new PageableProductList(new Meta(pageNumber, 10), new ArrayList<>())
            );

            mockMvc.perform(get("/clients/{idClient}/wishlists?page=0", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 401 when user tries to read another user's wishlist")
        void findWishlistByClientUnauthorized() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            Client client = new Client();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(false);

            mockMvc.perform(get("/clients/{idClient}/wishlists?page=0", uuid)
                    .header("Authorization", token))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 400 when the client does not exists")
        void findWishlistByClientBadRequest() throws Exception {
            UUID uuid = UUID.randomUUID();

            mockMvc.perform(get("/clients/{idClient}/wishlists", uuid)).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /clients/{idClient}/wishlists - Add a product to wishlist from client by id")
    class AddProductToWishlist{
        @Test
        @DisplayName("Should return 400 when client does not exists")
        void addProductToWishlistBadRequestClientDoesNotExists() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            ProductRequest productRequest = new ProductRequest();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);

            mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(productRequest))
                            .header("Authorization", token))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Client does not exists."));
        }

        @Test
        @DisplayName("Should return 400 when product is already on the wishlist")
        void addProductToWishlistBadRequestProductAlreadyOnWishlist() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            ProductRequest productRequest = new ProductRequest();
            productRequest.setIdProduct(uuid);

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.existsById(uuid)).thenReturn(true);
            when(wishlistService.existsByIdProductAndIdClient(uuid, uuid)).thenReturn(true);

            mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(productRequest))
                            .header("Authorization", token))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Product is already on the wishlist."));
        }

        @Test
        @DisplayName("Should return 400 when product does not exists")
        void addProductToWishlistBadRequestProductDoesNotExists() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            ProductRequest productRequest = new ProductRequest();
            productRequest.setIdProduct(uuid);

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.existsById(uuid)).thenReturn(true);
            when(wishlistService.findProductOnExternalAPI(uuid)).thenThrow(HttpClientErrorException.class);

            mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(productRequest))
                            .header("Authorization", token))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(String.format("Product with idProduct %s does not exists", uuid)));
        }

        @Test
        @DisplayName("Should return 401 when user tries to read another user's wishlist")
        void addProductToWishlistUnauthorized() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            ProductRequest productRequest = new ProductRequest();

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(false);

            mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(productRequest))
                            .header("Authorization", token))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 201 when product is added successfully to the wishlist")
        void addProductToWishlistCreated() throws Exception {
            String token = "BEARER_TOKEN";
            UUID uuid = UUID.randomUUID();
            ResponseEntity<ProductAPIResponse>  response = new ResponseEntity<>(HttpStatusCode.valueOf(200));
            ProductRequest productRequest = new ProductRequest();
            productRequest.setIdProduct(uuid);

            when(authService.isUserAuthorized(uuid.toString(), token)).thenReturn(true);
            when(clientService.existsById(uuid)).thenReturn(true);
            when(wishlistService.findProductOnExternalAPI(uuid)).thenReturn(response);

            mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                            .header("Authorization", token)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(productRequest)))
                    .andExpect(status().isCreated());
        }
    }
}
