package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.model.Product;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("GET /clients/{idClient}/wishlists - " +
            "Should return 200 when the client exists but there are no products in the wishlist"
    )
    void findWishlistByClientOk() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(clientService.existsById(uuid)).thenReturn(true);

        when(wishlistService.findByClientId(uuid)).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/clients/{idClient}/wishlists", uuid)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /clients/{idClient}/wishlists - " +
            "Should return 204 when the client exists but there are no products in the wishlist"
    )
    void findWishlistByClientNoContent() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(clientService.existsById(uuid)).thenReturn(true);

        mockMvc.perform(get("/clients/{idClient}/wishlists", uuid)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /clients/{idClient}/wishlists - Should return 400 when the client does not exists")
    void findWishlistByClientBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/clients/{idClient}/wishlists", uuid)).andExpect(status().isBadRequest());
    }
}
