package com.mateusgomes.luizalabs.repository;

import com.mateusgomes.luizalabs.data.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username =:username WHERE u.idUser =:idUser")
    void updateById(@Param("username") String username, @Param("idUser") UUID idUser);
}
