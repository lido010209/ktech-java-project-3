package com.example.myProject1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("lu")
public class UserViewController {
    @GetMapping("profile")
    public String profile(){
        return "user/profile";
    }

    @GetMapping("shop")
    public String createShop(){
        return "shop/user-shop/manage-shop";
    }

//    @GetMapping("shop/edit")
//    public String shop(){
//        return "shop/update";
//    }

    @GetMapping("malls")
    public String mall(){
        return "item/mall/home.html";
    }
    @GetMapping("malls/{itemId}")
    public String orderCreate(){
        return "order/user-buyer/create";
    }
    @GetMapping("order")
    public String allOrder(){
        return "order/user-buyer/allOrder";
    }
    @GetMapping("order/{orderIdx}")
    public String cancelOrder(){
        return "order/user-buyer/cancelOrder";
    }


    @GetMapping("search/shops")
    public String searchShops(){
        return "search/shop";
    }

    @GetMapping("search/shops/{id}/items")
    public String searchOneShop(){
        return "search/item";
    }

    @GetMapping("search/items")
    public String searchItems(){
        return "search/item";
    }

}
