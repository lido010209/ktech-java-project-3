package com.example.myProject1.order.service;

import com.example.myProject1.status.Status;
import com.example.myProject1.status.StatusRepo;
import com.example.myProject1.order.dto.OrderDto;
import com.example.myProject1.order.entity.Order;
import com.example.myProject1.order.repo.OrderRepo;
import com.example.myProject1.service.WebService;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.item.repo.ItemRepo;
import com.example.myProject1.user.entity.UserEntity;
import com.example.myProject1.user.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyerOrderService {
    private final OrderRepo orderRepo;
    private final WebService webService;
    private final ItemRepo itemRepo;
    private final StatusRepo statusRepo;
    private final UserRepo userRepo;
    @Transactional
    public OrderDto create(Long itemId, OrderDto dto){
        UserEntity buyer = webService.user();
        Item item = itemRepo.findById(itemId)
                .orElseThrow(()-> new IllegalArgumentException("This item is not available!!!"));

        if (item.getStock()<dto.getQuantity())
            throw new IllegalArgumentException("Sorry, the quantity of item is not enough!!!");

        // Calculate the total bill
        Integer total = item.getPrice()* dto.getQuantity();

        // Set status
        Status status = statusRepo.findByName("Ordered")
                .orElseThrow(()-> new IllegalArgumentException("Invalid status"));

        Order order = Order.builder()
                .quantity(dto.getQuantity())
                .total(total)
                .buyer(buyer)
                .item(item)
                .status(status)
                .build();

        order = orderRepo.save(order);
        buyer.getOrders().add(order);
        webService.calculatePayment();
        return OrderDto.dto(order);
    }

    // payment, update status


    @Transactional
    public List<OrderDto> allOrdersByUser(){
        List<OrderDto> dtos = new ArrayList<>();
        UserEntity buyer = webService.user();
        for (Order order: buyer.getOrders()){
            dtos.add(OrderDto.dto(order));
        }
//        log.info(dtos.toString());
        return dtos;
    }

    @Transactional
    public OrderDto oneOrderByUser(int orderIdx){
        UserEntity buyer = webService.user();
        if (orderIdx>= buyer.getOrders().size())
            throw new IllegalArgumentException("This order does not exist!!!");

        Order order = buyer.getOrders().get(orderIdx);
//        log.info(dtos.toString());
        return OrderDto.dto(order);
    }

    @Transactional
    public OrderDto cancelOrder (int orderIdx){
        UserEntity buyer = webService.user();
        if (orderIdx>= buyer.getOrders().size())
            throw new IllegalArgumentException("This order does not exist!!!");

        Order order = buyer.getOrders().get(orderIdx);
        // status cancel
        Status status = statusRepo.findByName("Cancel order").orElseThrow();
        order.setStatus(status);
        order = orderRepo.save(order);
        buyer.getOrders().remove(order);
        webService.calculatePayment();
        return OrderDto.dto(orderRepo.save(order));
    }

}
