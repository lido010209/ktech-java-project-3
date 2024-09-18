package com.example.myProject1.item.entity;

import com.example.myProject1.entity.BaseEntity;
import com.example.myProject1.order.entity.Order;
import com.example.myProject1.shop.entity.Shop;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
public class Item extends BaseEntity {
    private String name;
    private Integer stock;
    private Integer price;
    private String description;
    private String image;
    @ManyToOne
    private Shop shop;
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private final List<Order> orders = new ArrayList<>();

}
