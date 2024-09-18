package com.example.myProject1.item;

import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("malls")
public class ItemController {
    private final ItemService itemService;
    @GetMapping
    public List<ItemDto> readAll(){
        return itemService.readAll();
    }
    @GetMapping("{id}")
    public ItemDto readOne(@PathVariable("id") Long id){
        return itemService.readOne(id);
    }

    @GetMapping("shop/{id}")
    public List<ItemDto> readAllInAnotherShop(@PathVariable("id") Long id){
        return itemService.readAllInAnotherShop(id);
    }
}
