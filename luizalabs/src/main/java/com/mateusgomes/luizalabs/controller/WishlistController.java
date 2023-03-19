package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.model.PageableProductList;
import com.mateusgomes.luizalabs.model.Product;
import com.mateusgomes.luizalabs.model.ProductAPIResponse;
import com.mateusgomes.luizalabs.model.ProductRequest;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ClientService clientService;

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();

        return ResponseEntity.status(400).body(String.format("No %s was provided", name));
    }

    @GetMapping("/{idClient}/wishlists")
    public ResponseEntity findWishlistByClient(
            @PathVariable UUID idClient,
            @RequestParam(value="page", required=true) int page
    ){
        if(!clientService.existsById(idClient)){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        PageableProductList pageableProductList = wishlistService.findByClientId(idClient, page);

        if(pageableProductList.getProducts().isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(pageableProductList);
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

        try{
            ResponseEntity<ProductAPIResponse> responseProductAPI = wishlistService.findProductOnExternalAPI(idProduct);
            Product product = wishlistService.addProductToWishlist(responseProductAPI.getBody(), idClient);

            return ResponseEntity.status(201).body(product);
        } catch (HttpClientErrorException e){
            return ResponseEntity.status(400).body(String.format("Product with idProduct %s does not exists", idProduct));
        }
    }
}
