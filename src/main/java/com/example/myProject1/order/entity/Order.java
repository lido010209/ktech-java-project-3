package com.example.myProject1.order.entity;

import com.example.myProject1.entity.BaseEntity;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.status.Status;
import com.example.myProject1.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer_order")
public class Order extends BaseEntity {
    private Integer quantity;
    private Integer total;
    @ManyToOne
    private Status status;
    @ManyToOne
    private UserEntity buyer;
    @ManyToOne
    private Item item;
}
