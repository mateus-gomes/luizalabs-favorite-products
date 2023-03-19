package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.model.Product;
import com.mateusgomes.luizalabs.model.ProductAPIResponse;
import com.mateusgomes.luizalabs.model.ProductRequest;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        }

        return ResponseEntity.status(200).body(wishlist);
    }

    @PostMapping("/{idClient}/wishlists")
    public ResponseEntity addProductToWishlist(@PathVariable UUID idClient, @RequestBody ProductRequest productRequest){
        UUID idProduct = productRequest.getIdProduct();

        if(!clientService.existsById(idClient)){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        if(wishlistService.existsByIdProductAndIdClient(idProduct, idClient)){
            return ResponseEntity.status(400).body("Product is already on the wishlist.");
        }

        ResponseEntity<ProductAPIResponse> responseProductAPI = wishlistService.findProductOnExternalAPI(idProduct);

        boolean isValidProduct = responseProductAPI.getStatusCode().is2xxSuccessful();

        if(!isValidProduct){
            return ResponseEntity.status(400).body(String.format("Product with idProduct %s does not exists", idProduct));
        }

        Product product = wishlistService.addProductToWishlist(responseProductAPI.getBody(), idClient);

        return ResponseEntity.status(201).body(product);
    }
}
