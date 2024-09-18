package com.example.myProject1.order.dto;

import com.example.myProject1.item.entity.Item;
import com.example.myProject1.order.entity.Order;
import com.example.myProject1.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class OrderDto {
    private Long id;
    private Integer quantity;
    private Integer total;
    private Long itemId;
    private String status;
    private UserDto buyer;
    private String itemName;
    private Integer itemPrice;
    private String itemDescription;
    private String itemImage;

    private final Map<Long, Integer> orderIdxs = new HashMap<>();
    private final Map<Long, Integer> orderBuyerIdxs = new HashMap<>();

    public static OrderDto dto(Order order){
        OrderDto orderDto = OrderDto.builder()
                .id(order.getId())
                .quantity(order.getQuantity())
                .total(order.getTotal())
                .itemId(order.getItem().getId())
                .itemName(order.getItem().getName())
                .itemPrice(order.getItem().getPrice())
                .itemDescription(order.getItem().getDescription())
                .itemImage(order.getItem().getImage())
                .status(order.getStatus().getName())
                .buyer(UserDto.userDto(order.getBuyer()))
                .build();
        for (int i = 0; i < order.getItem().getOrders().size(); i++) {
            orderDto.orderIdxs.put(order.getItem().getOrders().get(i).getId(), i+1);
        }
        for (int i = 0; i < order.getBuyer().getOrders().size(); i++) {
            orderDto.orderBuyerIdxs.put(order.getBuyer().getOrders().get(i).getId(), i+1);
        }
        return orderDto;
    }
}
