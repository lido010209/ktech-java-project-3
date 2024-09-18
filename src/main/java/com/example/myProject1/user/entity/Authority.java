package com.example.myProject1.user.entity;

import com.example.myProject1.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority extends BaseEntity {
    private String authority;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    private final Set<UserEntity> users = new HashSet<>();
}
