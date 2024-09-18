package com.example.myProject1.item.dto;

import com.example.myProject1.order.entity.Order;
import com.example.myProject1.shop.dto.ShopDto;
import com.example.myProject1.item.entity.Item;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class ItemDto {
    private Long id;
    private String name;
    private Integer stock;
    private Integer price;
    private String description;
    private String image;
    private ShopDto shop;

    private Integer numberOrder;

    public static ItemDto dto(Item item){

        Integer sum =0;
        for (Order order: item.getOrders()){
            if (order.getStatus().getName().equals("Ordered")) sum+=1;
        }
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .stock(item.getStock())
                .numberOrder(sum)
                .description(item.getDescription())
                .image(item.getImage())
                .shop(ShopDto.dto(item.getShop()))
                .build();

        return itemDto;
    }
}
