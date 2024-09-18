package com.example.myProject1.shop;

import com.example.myProject1.shop.dto.ShopDto;
import com.example.myProject1.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("shops")
public class ShopController {
    private final ShopService shopService;
//    @PostMapping
//    public ShopDto create(
//            @RequestBody
//            ShopDto shop
//    ){
//        return shopService.create(shop);
//    }

    @GetMapping
    public ShopDto readOne(
    ){
        return shopService.readOne();
    }

//    @GetMapping("{id}")
//    public ShopDto readAnother(@PathVariable("id") Long id){
//        return shopService.anotherShops(id);
//    }

    // update shop
    @PutMapping
    public ShopDto update(
            @RequestBody
            ShopDto dto
    ){
        return shopService.update(dto);
    }
    // Update status
    @PutMapping("status")
    public ShopDto updateStatus(
            @RequestBody
            ShopDto dto
    ){
        return shopService.updateStatus(dto);
    }

//    @DeleteMapping("{shopIdx}")
//    public void delete(
//            @PathVariable("shopIdx")
//            int shopIdx
//    ){
//        shopService.delete(shopIdx);
//    }
}
