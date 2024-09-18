package com.example.myProject1.item.service;

import com.example.myProject1.service.WebService;
import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.item.repo.ItemRepo;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepo itemRepo;
    private final ShopRepo shopRepo;
    private final WebService webService;

    public List<ItemDto> readAll(){
        UserEntity viewer = webService.viewer();
        List<ItemDto> dtos= new ArrayList<>();
        if (viewer.getShop()==null){
            for (Item item: itemRepo.findAll()){
                dtos.add(ItemDto.dto(item));
            }
            return dtos;
        }
        for (Item item: itemRepo.findAll()){
            if (!viewer.getShop().getItems().contains(item)) {
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }

    public List<ItemDto> readAllInAnotherShop(Long id){
        Shop shop= shopRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Shop is not found"));
        List<ItemDto> dtos= new ArrayList<>();
        for (Item item: shop.getItems()){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public ItemDto readOne(Long id){
        Item item = itemRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Item does not exist!!!"));
        return ItemDto.dto(item);
    }

}
