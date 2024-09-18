package com.example.myProject1.shop.entity;

import com.example.myProject1.entity.BaseEntity;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.status.Status;
import com.example.myProject1.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop extends BaseEntity {
    private String name;
    private String introduction;
    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    @ManyToOne
    private Category category;
    @ManyToOne
    private Status status;

    @OneToMany(mappedBy = "shop", fetch = FetchType.EAGER)
    private final List<Item> items = new ArrayList<>();

}
