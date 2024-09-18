package com.example.myProject1.user.entity;

import com.example.myProject1.entity.BaseEntity;
import com.example.myProject1.order.entity.Order;
import com.example.myProject1.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity extends BaseEntity {
    private String username;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phone;
    private Integer age;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private final Set<Authority> authorities = new HashSet<>();

    private String profileImg;
    private Integer payment;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER)
    private final List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "owner")
    private Shop shop;
}
