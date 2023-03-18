package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.model.Product;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/{idClient}/wishlists")
    public ResponseEntity findWishlistByClient(@PathVariable UUID idClient){
        if(!clientService.existsById(idClient)){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        List<Product> wishlist = wishlistService.findByClientId(idClient);

        if(wishlist.isEmpty()){
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(wishlist);
        }
    }
}
