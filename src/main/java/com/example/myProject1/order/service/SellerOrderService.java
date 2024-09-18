package com.example.myProject1.order.service;

import com.example.myProject1.status.Status;
import com.example.myProject1.status.StatusRepo;
import com.example.myProject1.order.dto.OrderDto;
import com.example.myProject1.order.entity.Order;
import com.example.myProject1.order.repo.OrderRepo;
import com.example.myProject1.service.WebService;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.entity.Shop;
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
public class SellerOrderService {
    private final OrderRepo orderRepo;
    private final WebService webService;
    private final ItemRepo itemRepo;
    private final StatusRepo statusRepo;
    private final UserRepo userRepo;

    @Transactional
    public OrderDto confirmOrder(int itemIdx, int orderIdx){
        UserEntity seller = webService.userBusiness();
        Shop shop = seller.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        if (orderIdx>= item.getOrders().size())
            throw new IllegalArgumentException("Order is not found!!!");
        Order order = item.getOrders().get(orderIdx);
        UserEntity buyer = order.getBuyer();
        if (order.getStatus().getName().equals("Cancel order")) {
            orderRepo.delete(order);
            buyer.getOrders().remove(order);
            webService.calculatePayment();
            return OrderDto.dto(order);
        }

        buyer.setPayment(buyer.getPayment() - order.getTotal());
        userRepo.save(buyer);

        item.setStock(item.getStock()-order.getQuantity());
        itemRepo.save(item);

        // Status
        Status status = statusRepo.findByName("Paid").orElseThrow();

        order.setStatus(status);
        order= orderRepo.save(order);
        buyer.getOrders().remove(order);
        webService.calculatePayment();
        return OrderDto.dto(order);
    }





    @Transactional
    public List<OrderDto> allOrders(int itemIdx){
        List<OrderDto> dtos = new ArrayList<>();
        UserEntity seller = webService.userBusiness();
        Shop shop = seller.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);

        for (Order order: item.getOrders()){
            dtos.add(OrderDto.dto(order));
        }
//        log.info(dtos.toString());
        return dtos;
    }

    @Transactional
    public OrderDto oneOrder(int itemIdx, int orderIdx){
        UserEntity seller = webService.userBusiness();
        Shop shop = seller.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        if (orderIdx>= item.getOrders().size())
            throw new IllegalArgumentException("Order is not found!!!");
        Order order = item.getOrders().get(orderIdx);

//        log.info(dtos.toString());
        return OrderDto.dto(order);
    }
}
