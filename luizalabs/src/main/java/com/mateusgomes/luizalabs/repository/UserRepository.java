package com.mateusgomes.luizalabs.repository;

import com.mateusgomes.luizalabs.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    Optional<User> findByUserName(@Param("userName") String userName);
}