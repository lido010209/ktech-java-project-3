package com.example.myProject1.status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepo extends JpaRepository<Status, Long> {
    boolean existsByName(String name);
    Optional<Status> findByName(String name);
}
