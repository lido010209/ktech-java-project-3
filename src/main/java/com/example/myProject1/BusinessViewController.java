package com.example.myProject1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lu")
public class BusinessViewController {
    @GetMapping("manage-items")
    public String allItem(){
        return "item/home";
    }
    @GetMapping("manage-items/create")
    public String createItem(){
        return "item/create";
    }
    @GetMapping("manage-items/{idx}")
    public String oneItem(){
        return "item/update";
    }

    @GetMapping("manage-items/{idx}/manage-orders")
    public String allOrder(){
        return "order/user-seller/home";
    }
    @GetMapping("manage-items/{itemIdx}/manage-orders/{orderIdx}")
    public String oneOrder(){
        return "order/user-seller/read";
    }
}
