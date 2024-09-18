package com.example.myProject1.item.repo;

import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.name LIKE %:keyword%")
    List<Item> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT i FROM Item i WHERE i.shop =:shop AND i.name LIKE %:keyword%")
    List<Item> findByNameContainingInShop(@Param("keyword") String keyword,
                                          @Param("shop") Shop shop);

    @Query("SELECT i FROM Item i WHERE i.name LIKE %:keyword% AND i.price BETWEEN :price1 AND :price2")
    List<Item> findByNameAndPrice(@Param("keyword") String keyword,
                                  @Param("price1") Integer price1,
                                  @Param("price2") Integer price2);

    @Query("SELECT i FROM Item i WHERE i.price BETWEEN :price1 AND :price2")
    List<Item> findByPriceLimit(@Param("price1") Integer price1, @Param("price2") Integer price2);

    @Query("SELECT i FROM Item i WHERE i.shop =:shop AND i.price BETWEEN :price1 AND :price2")
    List<Item> findByPriceInShop(@Param("price1") Integer price1,
                                 @Param("price2") Integer price2,
                                 @Param("shop") Shop shop);

    @Query("SELECT i FROM Item i WHERE i.shop =:shop AND i.name LIKE %:keyword% AND i.price BETWEEN :price1 AND :price2")
    List<Item> findByNameAndPriceInShop(@Param("keyword") String keyword,
                                @Param("price1") Integer price1,
                                 @Param("price2") Integer price2,
                                 @Param("shop") Shop shop);
}
