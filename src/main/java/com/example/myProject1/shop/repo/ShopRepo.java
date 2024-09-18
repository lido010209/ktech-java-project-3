package com.example.myProject1.shop.repo;

import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepo extends JpaRepository<Shop, Long> {
//    @Query(
//            value = "SELECT rank FROM ShopRankView WHERE owner_id = :ownerId",
//            nativeQuery = true
//    )
//    List<> shopsByOwner(@Param("ownerId") Long ownerId);

    List<Shop> findByOwner(UserEntity owner);

    @Query("SELECT s FROM Shop s WHERE s.status.name = 'Opened' AND s.name LIKE %:keyword%")
    List<Shop> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT s FROM Shop s WHERE s.status.name = 'Opened' AND s.category.name = :category")
    List<Shop> findByCategory(@Param("category") String category);

    @Query("SELECT s FROM Shop s WHERE s.status.name = 'Opened' AND s.name LIKE %:keyword% AND s.category.name = :category")
    List<Shop> findByNameAndCategory(@Param("keyword") String keyword, @Param("category") String category);
//    @Query("SELECT s.items FROM Shop s WHERE i.name LIKE %:keyword%")
//    List<Item> findByNameContaining(@Param("keyword") String keyword);

}
