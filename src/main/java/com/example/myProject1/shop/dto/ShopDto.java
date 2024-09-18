package com.example.myProject1.shop.dto;

import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.user.dto.UserDto;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class ShopDto {
    private Long id;
    private String name;
    private String introduction;
    private UserDto owner;
    private String category;
    private String status;
    private String reason;

    // Item id, item index in shop
    private final Map<Long, Integer> itemIdxs= new HashMap<>();

    public static ShopDto dto (Shop shop){
        ShopDto shopDto = ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .introduction(shop.getIntroduction())
                .owner(UserDto.userDto(shop.getOwner()))
                .category(shop.getCategory().getName())
                .status(shop.getStatus().getName())
                .reason(shop.getStatus().getReason())
                .build();
        for (int i = 0; i < shop.getItems().size(); i++) {
            shopDto.itemIdxs.put(shop.getItems().get(i).getId(), i+1);
        }
        return shopDto;
    }
}
