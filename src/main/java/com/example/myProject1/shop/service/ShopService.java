package com.example.myProject1.shop.service;

import com.example.myProject1.service.WebService;
import com.example.myProject1.shop.dto.ShopDto;
import com.example.myProject1.shop.entity.Category;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.status.Status;
import com.example.myProject1.shop.repo.CategoryRepo;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.status.StatusRepo;
import com.example.myProject1.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
@Slf4j
public class ShopService {
    private final ShopRepo shopRepo;
    private final CategoryRepo categoryRepo;
    private final StatusRepo statusRepo;
    private final WebService webService;

    public ShopService(ShopRepo shopRepo, CategoryRepo categoryRepo, StatusRepo statusRepo, WebService webService) {
        this.shopRepo = shopRepo;
        this.categoryRepo = categoryRepo;
        this.statusRepo = statusRepo;
        this.webService = webService;
    }


//    @Transactional
//    public ShopDto create(ShopDto dto){
//        UserEntity user = webService.user();
//        if (user.getShop()!=null)
//            throw new IllegalArgumentException("You already registered for opening shop");
//        Category category = categoryRepo.findByName(dto.getCategory())
//                .orElseThrow(()-> new IllegalArgumentException("This category is not available!!!"));
//        Status status = statusRepo.findByName("Pending open").orElseThrow();
//        Shop shop = Shop.builder()
//                .name(dto.getName())
//                .introduction(dto.getIntroduction())
//                .owner(user)
//                .category(category)
//                .status(status)
//                .build();
//        return ShopDto.dto(shopRepo.save(shop));
//    }

    @Transactional
    public ShopDto update(ShopDto dto){
        UserEntity user = webService.user();
        Shop shop = user.getShop();
        Category category = categoryRepo.findByName(dto.getCategory())
                .orElseThrow(()-> new IllegalArgumentException("This category is not available!!!"));
        Status status = statusRepo.findByName("Pending open").orElseThrow();
        shop.setName(dto.getName());
        shop.setIntroduction(dto.getIntroduction());
        shop.setCategory(category);
        shop.setStatus(status);
        return ShopDto.dto(shopRepo.save(shop));
    }

    @Transactional
    public ShopDto readOne(){
        UserEntity user = webService.user();
        if (user.getShop()==null)
            throw new IllegalArgumentException("You have not registered for creating shop yet!!");
        return ShopDto.dto(user.getShop());
    }
    @Transactional
    public ShopDto updateStatus(ShopDto dto){
        UserEntity user = webService.user();
        Shop shop = user.getShop();

        if (dto.getStatus().equals("Confirm reject")){
            Status status = statusRepo.findByName(dto.getStatus()).orElseThrow();
            shop.setStatus(status);
        }
        else if (dto.getStatus().equals("Request close")){
            Status status = statusRepo.findByName(dto.getStatus()).orElseThrow();
            status.setReason(dto.getReason());
            statusRepo.save(status);
            shop.setStatus(status);
        }
        return ShopDto.dto(shopRepo.save(shop));
    }

//    public ShopDto anotherShops(Long id){
//        Shop shop= shopRepo.findById(id)
//                .orElseThrow(()-> new IllegalArgumentException("Shop is not found"));
//        return ShopDto.dto(shop);
//    }
}
