package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.api.LuizalabsGateway;
import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.model.Product;
import com.mateusgomes.luizalabs.model.ProductAPIResponse;
import com.mateusgomes.luizalabs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LuizalabsGateway luizalabsGateway;

    public List<Product> findByClientId(UUID idClient) {
        return productRepository.findByClient(idClient);
    }

    public boolean existsByIdProductAndIdClient(UUID idProduct, UUID idClient) {
        return productRepository.existsByIdProductAndIdClient(idProduct, idClient).isPresent();
    }

    public ResponseEntity<ProductAPIResponse> findProductOnExternalAPI(UUID idProduct) {
        return luizalabsGateway.getProductById(idProduct);
    }

    public Product addProductToWishlist(ProductAPIResponse productAPIResponse, UUID idClient) {
        Product product = buildProduct(productAPIResponse, idClient);
        productRepository.save(product);
        return product;
    }

    private Product buildProduct(ProductAPIResponse productAPIResponse, UUID idClient){
        return new Product(
                productAPIResponse.getId(),
                productAPIResponse.getTitle(),
                productAPIResponse.getImage(),
                productAPIResponse.getPrice(),
                productAPIResponse.getReviewScore(),
                new Client(idClient)
        );
    }
}
