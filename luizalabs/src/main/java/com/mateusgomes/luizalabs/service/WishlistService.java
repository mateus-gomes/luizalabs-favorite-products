package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.api.LuizalabsGateway;
import com.mateusgomes.luizalabs.data.domain.Meta;
import com.mateusgomes.luizalabs.data.domain.PageableProductList;
import com.mateusgomes.luizalabs.data.domain.ProductAPIResponse;
import com.mateusgomes.luizalabs.data.model.*;
import com.mateusgomes.luizalabs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LuizalabsGateway luizalabsGateway;

    public PageableProductList findByClientId(UUID idClient, int page) {
        final int PAGE_SIZE = 5;
        Pageable pageable = PageRequest.of(page-1, PAGE_SIZE);
        Page<Product> pageProduct = productRepository.findByClientIdClient(idClient, pageable);

        return new PageableProductList(
                new Meta(page, PAGE_SIZE),
                pageProduct.getContent()
        );
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
