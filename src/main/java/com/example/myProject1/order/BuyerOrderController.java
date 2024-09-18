package com.example.myProject1.order;

import com.example.myProject1.order.dto.OrderDto;
import com.example.myProject1.order.service.BuyerOrderService;
import com.example.myProject1.user.service.AfterLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class BuyerOrderController {
    private final BuyerOrderService buyerOrderService;
    private final AfterLoginService afterLoginService;
    @PostMapping("{itemId}")
    public OrderDto create(
            @PathVariable("itemId")
            Long itemId,
            @RequestBody
            OrderDto dto
    ){
        OrderDto orderDto = buyerOrderService.create(itemId, dto);
        return orderDto;
    }


    @GetMapping
    public List<OrderDto> readAll(){
        return buyerOrderService.allOrdersByUser();
    }

    @GetMapping("{orderIdx}")
    public OrderDto oneOrder(
            @PathVariable("orderIdx")
            int orderIdx
    ){
        return buyerOrderService.oneOrderByUser(orderIdx);
    }

    @PutMapping("{orderIdx}")
    public OrderDto cancelOrder(
            @PathVariable("orderIdx")
            int orderIdx
    ){
        return buyerOrderService.cancelOrder(orderIdx);
    }
}
