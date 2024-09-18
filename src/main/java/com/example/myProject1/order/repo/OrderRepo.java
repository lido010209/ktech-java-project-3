package com.example.myProject1.order.repo;

import com.example.myProject1.order.entity.Order;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(UserEntity buyer);

    //Get shops that have the latest transaction
    @Query("SELECT o.item.shop FROM Order o WHERE o.buyer = :buyer AND o.status.name != \"Cancel order\" ORDER BY o.createAt DESC")
    List<Shop> shopsOfBuyer(@Param("buyer") UserEntity buyer);
}
