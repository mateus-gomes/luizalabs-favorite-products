package com.mateusgomes.luizalabs.repository;

import com.mateusgomes.luizalabs.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT * FROM products WHERE fk_client = ?1", nativeQuery = true)
    List<Product> findByClient(UUID idClient);
}
