package com.example.myProject1.shop;

import com.example.myProject1.shop.dto.ShopDto;
import com.example.myProject1.shop.service.ManageShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("manage-shops")
public class ManageShopController {
    private final ManageShopService manageShopService;
    @GetMapping
    public List<ShopDto> readAll(){
        return manageShopService.readAll();
    }
    @GetMapping("base")
    public List<ShopDto> readAllForBase(){
        return manageShopService.readAll();
    }

    @GetMapping("{id}")
    public ShopDto readOne(@PathVariable("id") Long id){
        return manageShopService.readOne(id);
    }

    // Confirm request creating shop
    @PutMapping("{id}")
    public ShopDto updateStatus(
            @PathVariable("id") Long id,
            @RequestBody ShopDto dto
    ){
        return manageShopService.updateStatus(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable("id") Long id
    ){
        manageShopService.delete(id);
    }

}
