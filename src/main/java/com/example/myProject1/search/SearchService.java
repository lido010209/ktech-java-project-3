package com.example.myProject1.search;

import com.example.myProject1.order.repo.OrderRepo;
import com.example.myProject1.service.WebService;
import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.shop.dto.ShopDto;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.item.repo.ItemRepo;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final ShopRepo shopRepo;
    private final WebService webService;

    public List<ShopDto> searchNoCondition(){
        List<ShopDto> shopDtos = new ArrayList<>();
        UserEntity user = webService.user();
        Set<Shop> shops = new LinkedHashSet<>();
        for (Shop shop: orderRepo.shopsOfBuyer(user)){
            shops.add(shop);
        }
        for (Shop shop: shopRepo.findAll()){
            if (shop.getStatus().getName().equals("Opened") && !user.getShop().equals(shop)){
                shops.add(shop);
            }
        }
        for (Shop shop: shops){
            shopDtos.add(ShopDto.dto(shop));
        }

        return shopDtos;
    }

    public List<ShopDto> searchByName(String keyWord){
        List<ShopDto> shops = new ArrayList<>();
        UserEntity user = webService.user();

        for (Shop shop: shopRepo.findByNameContaining(keyWord)){
            if (!user.getShop().equals(shop))
                shops.add(ShopDto.dto(shop));
        }

        return shops;
    }

    public List<ShopDto> searchByCategory(String category){
        List<ShopDto> shops = new ArrayList<>();
        UserEntity user = webService.user();

        for (Shop shop: shopRepo.findByCategory(category)){
            if (!user.getShop().equals(shop))
                shops.add(ShopDto.dto(shop));
        }
        return shops;
    }

    public List<ShopDto> searchByNameAndCategory(String keyWord, String category){
        List<ShopDto> shops = new ArrayList<>();
        UserEntity user = webService.user();
        for (Shop shop: shopRepo.findByNameAndCategory(keyWord, category)){
            if (!user.getShop().equals(shop))
                shops.add(ShopDto.dto(shop));
        }
        return shops;
    }

    // find items in all shop

    public List<ItemDto> searchItemsByName (String keyword){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user =webService.user();

        for (Item item: itemRepo.findByNameContaining(keyword)){
            if (!user.getShop().getItems().contains(item)){
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }

    public List<ItemDto> searchItemsByPrice (Integer price1, Integer price2){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user =webService.user();
        for (Item item: itemRepo.findByPriceLimit(price1, price2)){
            if (!user.getShop().getItems().contains(item)){
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }
    public List<ItemDto> searchItemsByNameAndPrice (Integer price1, Integer price2, String keyword){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user =webService.user();

        for (Item item: itemRepo.findByNameAndPrice(keyword, price1, price2)){
            if (!user.getShop().getItems().contains(item)){
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }

    // find Items in their shop (Seller search in his/her own shop)
    public List<ItemDto> searchByNameInShop (String keyword){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();

        for (Item item: itemRepo.findByNameContainingInShop(keyword, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByPriceInShop (Integer price1, Integer price2){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();

        for (Item item: itemRepo.findByPriceInShop(price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByNameAndPriceInShop (String keyword, Integer price1, Integer price2){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();

        for (Item item: itemRepo.findByNameAndPriceInShop(keyword, price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    // find Item in another shop (Buyer search)
    public List<ItemDto> searchByNameInShop (String keyword, Long id){
        List<ItemDto> dtos = new ArrayList<>();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop does not exist!!!")
        );

        for (Item item: itemRepo.findByNameContainingInShop(keyword, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByPriceInShop (Integer price1, Integer price2, Long id){
        List<ItemDto> dtos = new ArrayList<>();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop does not exist!!!")
        );

        for (Item item: itemRepo.findByPriceInShop(price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByNameAndPriceInShop (String keyword, Integer price1, Integer price2, Long id){
        List<ItemDto> dtos = new ArrayList<>();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop does not exist!!!")
        );
        for (Item item: itemRepo.findByNameAndPriceInShop(keyword, price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }
}
