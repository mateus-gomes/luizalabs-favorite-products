package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.data.domain.PageableProductList;
import com.mateusgomes.luizalabs.data.model.Product;
import com.mateusgomes.luizalabs.data.domain.ProductAPIResponse;
import com.mateusgomes.luizalabs.data.domain.ProductRequest;
import com.mateusgomes.luizalabs.service.AuthService;
import com.mateusgomes.luizalabs.service.ClientService;
import com.mateusgomes.luizalabs.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
@Tag(name = "Wishlist", description = "Endpoints for managing client's wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthService authService;

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();

        return ResponseEntity.status(400).body(String.format("No %s was provided", name));
    }

    @GetMapping("/{idClient}/wishlists")
    @Operation(summary = "Find wishlist from client", description = "Find wishlist from client separated by pages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist was found",
                    content = { @Content(
                            schema = @Schema(implementation = PageableProductList.class),
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "No page was provided",
                    content = { @Content (schema = @Schema(defaultValue = "No page was provided"))}
            ),
            @ApiResponse(responseCode = "400", description = "Client does not exists",
                    content = { @Content (schema = @Schema(defaultValue = "Client does not exists."))}
            ),
            @ApiResponse(responseCode = "401", description = "User tries to read information from other user",
                    content = { @Content (
                            schema = @Schema(defaultValue = "You are not authorized to complete this action.")
                    )}
            ),
            @ApiResponse(responseCode = "404", description = "No clients were found",
                    content = { @Content }
            ),
    })
    public ResponseEntity findWishlistByClient(
            @PathVariable UUID idClient,
            @RequestParam(value="page", required=true) int page,
            @RequestHeader (name="Authorization") String token
    ){
        if(!authService.isUserAuthorized(idClient.toString(), token)){
            return ResponseEntity.status(401).body("You are not authorized to complete this action.");
        }

        if(!clientService.existsById(idClient)){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        PageableProductList pageableProductList = wishlistService.findByClientId(idClient, page);

        if(pageableProductList.getProducts().isEmpty()){
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(pageableProductList);
    }

    @Operation(
            summary = "Add a item to client's wishlist",
            description = "Create a new record of a product related to the client in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item was successfully added",
                    content = { @Content(
                            schema = @Schema(implementation = Product.class),
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "Client does not exists",
                    content = { @Content (schema = @Schema(defaultValue = "Client does not exists."))}
            ),
            @ApiResponse(responseCode = "400", description = "Product is already on the wishlist",
                    content = { @Content (schema = @Schema(defaultValue = "Product is already on the wishlist."))}
            ),
            @ApiResponse(responseCode = "400", description = "Product does not exists",
                    content = { @Content (schema = @Schema(
                            defaultValue = "Product with idProduct {idProduct} does not exists."
                    ))}
            ),
            @ApiResponse(responseCode = "401", description = "User tries to read information from other user",
                    content = { @Content (
                            schema = @Schema(defaultValue = "You are not authorized to complete this action.")
                    )}
            ),
    })
    @PostMapping("/{idClient}/wishlists")
    public ResponseEntity addProductToWishlist(
            @PathVariable UUID idClient,
            @RequestBody ProductRequest productRequest,
            @RequestHeader (name="Authorization") String token
    ){
        if(!authService.isUserAuthorized(idClient.toString(), token)){
            return ResponseEntity.status(401).body("You are not authorized to complete this action.");
        }
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
