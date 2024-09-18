package com.example.myProject1.status;

import com.example.myProject1.entity.BaseEntity;
import com.example.myProject1.shop.entity.Shop;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Status extends BaseEntity {
    private String name;
    private String reason;
    @OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
    private final List<Shop> shops = new ArrayList<>();
}
