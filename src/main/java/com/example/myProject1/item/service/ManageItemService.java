package com.example.myProject1.item.service;

import com.example.myProject1.service.WebService;
import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.item.repo.ItemRepo;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class ManageItemService {
    private final ItemRepo itemRepo;
    private final ShopRepo shopRepo;
    private final WebService webService;


    public ManageItemService(ItemRepo itemRepo, ShopRepo shopRepo, WebService webService) {
        this.itemRepo = itemRepo;
        this.shopRepo = shopRepo;
        this.webService = webService;
    }


    public ItemDto create(ItemDto dto){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        Item item = Item.builder()
                .name(dto.getName())
                .stock(dto.getStock())
                .price(dto.getPrice())
                .image("/static/visual/shop/item.png")
                .description(dto.getDescription())
                .shop(shop)
                .build();
        return ItemDto.dto(itemRepo.save(item));

    }

    @Transactional
    public ItemDto update(int itemIdx, ItemDto dto){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStock(dto.getStock());
        item.setDescription(dto.getDescription());
        return ItemDto.dto(itemRepo.save(item));
    }

    @Transactional
    public ItemDto updateImg(int itemIdx, MultipartFile image){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);

        // Create directory to store link of image
        String directory = String.format("item/%s/", item.getId());
        try{
            Files.createDirectories(Path.of(directory));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }
        String fileName= image.getOriginalFilename();
        String[] eleFile= fileName.split("\\.");
        String extension = eleFile[eleFile.length-1];

        String path = directory+ "item."+extension;
        try{
            image.transferTo(Path.of(path));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }

        String url = String.format("/static/%s/item.%s",
                item.getId(), extension);

        item.setImage(url);
        return ItemDto.dto(itemRepo.save(item));
    }

    @Transactional
    public void delete(int itemIdx){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        itemRepo.delete(item);
//        if (itemIdx+1<shop.getItems().size()) {
//            for (int i = itemIdx + 1; i < shop.getItems().size(); i++) {
//                Item item1 = shop.getItems().get(i);
//                File oldFile = new File(item1.getImage());
//                String[] ele = item1.getImage().split(".");
//                String extension = ele[ele.length - 1];
//                File newFIle = new File(String.format("/static/%s/shop%s/item%s.%s",
//                        user.getId(), shopIdx + 1, i, extension));
//                itemRepo.save(item1);
//            }
//        }
        shop.getItems().remove(item);
        shopRepo.save(shop);
    }

    public List<ItemDto> readAll(){
        List<ItemDto> dtos= new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        for (Item item: shop.getItems()){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public ItemDto readOne(int itemIdx){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        return ItemDto.dto(item);
    }

}
