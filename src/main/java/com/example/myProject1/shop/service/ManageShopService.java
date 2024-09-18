package com.example.myProject1.shop.service;

import com.example.myProject1.service.WebService;
import com.example.myProject1.shop.dto.ShopDto;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.status.Status;
import com.example.myProject1.shop.repo.CategoryRepo;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.status.StatusRepo;
import com.example.myProject1.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManageShopService {
    private final ShopRepo shopRepo;
    private final CategoryRepo categoryRepo;
    private final StatusRepo statusRepo;
    private final WebService webService;

    // For admin
    @Transactional
    public List<ShopDto> readAll(){
//        UserEntity user = webService.userAdmin();
        List<ShopDto> dtos = new ArrayList<>();
        for (Shop shop: shopRepo.findAll()){
            dtos.add(ShopDto.dto(shop));
        }
//        Collections.reverse(dtos);
        return dtos;
    }

    @Transactional
    public ShopDto readOne(Long id){
        Shop shop= shopRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Shop is not found!!!"));
        return ShopDto.dto(shop);
    }

    @Transactional
    public ShopDto updateStatus(Long id, ShopDto dto){
//        UserEntity user = webService.userAdmin();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop is not found!!!")
        );
        if (dto.getStatus().equals("Opened")){
            Status approved = statusRepo.findByName("Opened").orElseThrow();
            shop.setStatus(approved);
            UserEntity user = shop.getOwner();
            webService.updateAuthority("ROLE_BUSINESS", user);
        }
        else if (dto.getStatus().equals("Reject")){
            Status reject = statusRepo.findByName("Reject").orElseThrow();
            shop.setStatus(reject);
            reject.setReason(dto.getReason());
        }
        else if (dto.getStatus().equals("Closed")){
            Status closed = statusRepo.findByName("Closed").orElseThrow();
            shop.setStatus(closed);
            UserEntity user = shop.getOwner();
            webService.removeAuthority("ROLE_BUSINESS", user);
        }
        return ShopDto.dto(shopRepo.save(shop));
    }

    @Transactional
    public void delete(Long id){
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop is not found!!!")
        );
        if (shop.getStatus().getName().equals("Confirm reject")
                || shop.getStatus().getName().equals("Closed")
        ) shopRepo.delete(shop);
    }



}
