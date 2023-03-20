package com.mateusgomes.luizalabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusgomes.luizalabs.model.*;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WishlistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /clients - Should return 400 when no page is send in the request")
    void findAllClientsBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/clients/{idClient}/wishlists", uuid))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No page was provided"));
    }

    @Test
    @DisplayName("GET /clients/{idClient}/wishlists - " +
            "Should return 200 when the client exists but there are no products on the wishlist"
    )
    void findWishlistByClientOk() throws Exception {
        int pageNumber = 0;
        UUID uuid = UUID.randomUUID();

        when(clientService.existsById(uuid)).thenReturn(true);

        when(wishlistService.findByClientId(uuid, pageNumber)).thenReturn(
                new PageableProductList(new Meta(pageNumber, 10), List.of(new Product()))
        );

        mockMvc.perform(get(String.format(
                "/clients/{idClient}/wishlists?page=%d", pageNumber), uuid)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /clients/{idClient}/wishlists - " +
            "Should return 404 when the client exists but there are no products on the wishlist"
    )
    void findWishlistByClientNoContent() throws Exception {
        int pageNumber = 0;
        UUID uuid = UUID.randomUUID();

        when(clientService.existsById(uuid)).thenReturn(true);

        when(wishlistService.findByClientId(uuid, pageNumber)).thenReturn(
                new PageableProductList(new Meta(pageNumber, 10), new ArrayList<>())
        );

        mockMvc.perform(get("/clients/{idClient}/wishlists?page=0", uuid)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /clients/{idClient}/wishlists - Should return 400 when the client does not exists")
    void findWishlistByClientBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/clients/{idClient}/wishlists", uuid)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /clients/{idClient}/wishlists - Should return 400 when client does not exists")
    void addProductToWishlistBadRequestClientDoesNotExists() throws Exception {
        UUID uuid = UUID.randomUUID();
        ProductRequest productRequest = new ProductRequest();

        mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client does not exists."));
    }

    @Test
    @DisplayName("POST /clients/{idClient}/wishlists - Should return 400 when product is already on the wishlist")
    void addProductToWishlistBadRequestProductAlreadyOnWishlist() throws Exception {
        UUID uuid = UUID.randomUUID();
        ProductRequest productRequest = new ProductRequest();
        productRequest.setIdProduct(uuid);

        when(clientService.existsById(uuid)).thenReturn(true);

        when(wishlistService.existsByIdProductAndIdClient(uuid, uuid)).thenReturn(true);

        mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product is already on the wishlist."));
    }

    @Test
    @DisplayName("POST /clients/{idClient}/wishlists - Should return 400 when product does not exists")
    void addProductToWishlistBadRequestProductDoesNotExists() throws Exception {
        UUID uuid = UUID.randomUUID();
        ProductRequest productRequest = new ProductRequest();
        productRequest.setIdProduct(uuid);

        when(clientService.existsById(uuid)).thenReturn(true);

        when(wishlistService.findProductOnExternalAPI(uuid)).thenThrow(HttpClientErrorException.class);

        mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format("Product with idProduct %s does not exists", uuid)));
    }

    @Test
    @DisplayName("POST /clients/{idClient}/wishlists - " +
            "Should return 201 when product is added successfully to the wishlist"
    )
    void addProductToWishlistCreated() throws Exception {
        UUID uuid = UUID.randomUUID();
        ResponseEntity<ProductAPIResponse>  response = new ResponseEntity<>(HttpStatusCode.valueOf(200));
        ProductRequest productRequest = new ProductRequest();
        productRequest.setIdProduct(uuid);

        when(clientService.existsById(uuid)).thenReturn(true);

        when(wishlistService.findProductOnExternalAPI(uuid)).thenReturn(response);

        mockMvc.perform(post("/clients/{idClient}/wishlists", uuid)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }
}
