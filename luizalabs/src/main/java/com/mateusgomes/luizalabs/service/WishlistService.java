package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.model.Product;
import com.mateusgomes.luizalabs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findByClientId(UUID idClient) {
        return productRepository.findByClient(idClient);
    }
}
