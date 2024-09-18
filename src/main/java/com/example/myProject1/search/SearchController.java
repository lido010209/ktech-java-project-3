package com.example.myProject1.search;

import com.example.myProject1.item.dto.ItemDto;
import com.example.myProject1.item.entity.Item;
import com.example.myProject1.shop.dto.ShopDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("shops")
    public List<ShopDto> searchItems(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category) {

        if (keyword != null && category != null) {
            return searchService.searchByNameAndCategory(keyword, category);
        } else if (keyword != null) {
            return searchService.searchByName(keyword);
        }
        else if (category != null) {
            return searchService.searchByCategory(category);
        }
        return searchService.searchNoCondition();
    }

    @GetMapping("items")
    public List<ItemDto> searchItems(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "price1", required = false) Integer price1,
            @RequestParam(value = "price2", required = false) Integer price2
    ) {
        if (keyword != null && price1!=null && price2!=null)
            return searchService.searchItemsByNameAndPrice(price1, price2, keyword);
        else if (keyword != null)
            return searchService.searchItemsByName(keyword);
        return searchService.searchItemsByPrice(price1, price2);

    }

    // Sellers search in their own shop
    @GetMapping("shops/items")
    public List<ItemDto> searchItemsInShop(
//            @PathVariable("id") Long id,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "price1", required = false) Integer price1,
            @RequestParam(value = "price2", required = false) Integer price2
    ) {
        if (keyword != null && price1!=null && price2!=null)
            return searchService.searchByNameAndPriceInShop(keyword, price1, price2);
        else if (keyword != null)
            return searchService.searchByNameInShop(keyword);
        return searchService.searchByPriceInShop(price1, price2);
    }


    // Buyer search in one shop
    @GetMapping("shops/{id}/items")
    public List<ItemDto> searchItemsInShop(
            @PathVariable("id") Long id,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "price1", required = false) Integer price1,
            @RequestParam(value = "price2", required = false) Integer price2
    ) {
        if (keyword != null && price1!=null && price2!=null)
            return searchService.searchByNameAndPriceInShop(keyword, price1, price2, id);
        else if (keyword != null)
            return searchService.searchByNameInShop(keyword, id);
        return searchService.searchByPriceInShop(price1, price2, id);
    }

}
