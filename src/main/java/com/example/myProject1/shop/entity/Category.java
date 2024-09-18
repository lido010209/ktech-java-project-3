package com.example.myProject1.shop.entity;

import com.example.myProject1.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    private String name;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private final List<Shop> shops = new ArrayList<>();

}
