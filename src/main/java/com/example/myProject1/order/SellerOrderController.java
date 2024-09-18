package com.example.myProject1.order;

import com.example.myProject1.order.dto.OrderDto;
import com.example.myProject1.order.service.SellerOrderService;
import com.example.myProject1.user.service.AfterLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manage-items/item{itemIdx}/manage-orders")
public class SellerOrderController {
    private final SellerOrderService orderService;
    private final AfterLoginService afterLoginService;
    @PutMapping("order{orderIdx}")
    public OrderDto confirmOrder(
            @PathVariable("itemIdx") int itemIdx,
            @PathVariable("orderIdx") int orderIdx
    ){

        return orderService.confirmOrder(itemIdx, orderIdx);
    }

    @GetMapping("order{orderIdx}")
    public OrderDto oneOrder(
            @PathVariable("itemIdx") int itemIdx,
            @PathVariable("orderIdx") int orderIdx
    ){

        return orderService.oneOrder(itemIdx, orderIdx);
    }


    @GetMapping
    public List<OrderDto> readAllByItem(
            @PathVariable("itemIdx") int itemIdx
    ){
        return orderService.allOrders(itemIdx);
    }


}
