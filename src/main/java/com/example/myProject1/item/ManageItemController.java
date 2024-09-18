package com.example.myProject1.item;

import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.item.service.ManageItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("manage-items")
public class ManageItemController {
    private final ManageItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestBody
            ItemDto dto
    ){
        return itemService.create(dto);
    }

    @GetMapping
    public List<ItemDto> readAll(){
        return itemService.readAll();
    }

    @GetMapping("item{idx}")
    public ItemDto readOne(
            @PathVariable("idx")
            int itemIdx
    ){
        return itemService.readOne(itemIdx);
    }

    @PutMapping("item{idx}")
    public ItemDto update(
            @PathVariable("idx")
            int itemIdx,
            @RequestBody
            ItemDto dto
    ){
        return itemService.update(itemIdx, dto);
    }

    @PutMapping("item{idx}/image")
    public ItemDto updateImg(
            @PathVariable("idx")
            int itemIdx,
            @RequestParam("image")
            MultipartFile image
    ){
        return itemService.updateImg(itemIdx, image);
    }

    @DeleteMapping("item{idx}")
    public void delete(
            @PathVariable("idx")
            int itemIdx
    ){
        itemService.delete(itemIdx);
    }
}
