package com.mateusgomes.luizalabs.repository;

import com.mateusgomes.luizalabs.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByClientIdClient(UUID idClient, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE id_product = ?1 AND fk_client = ?2", nativeQuery = true)
    Optional<Product> existsByIdProductAndIdClient(UUID idProduct, UUID idClient);
}
