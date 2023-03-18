package com.mateusgomes.luizalabs.repository;

import com.mateusgomes.luizalabs.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByClientEmail(String clientEmail);

    Optional<Client> findByClientEmail(String clientEmail);
}
