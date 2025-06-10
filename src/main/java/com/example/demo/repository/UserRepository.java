package com.example.demo.repository;

import com.example.demo.model.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByToken(String token);
}
