package com.example.myProject1.user.repo;

import com.example.myProject1.user.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {
    boolean existsByAuthority(String authority);
    Optional<Authority> findByAuthority(String authority);
}
