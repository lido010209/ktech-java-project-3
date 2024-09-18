package com.example.myProject1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("lu")
public class AdminViewController {

    @GetMapping("manage-shops")
    public String allShop(){
        return "shop/admin/home";
    }
    @GetMapping("manage-shops/{id}")
    public String oneShop(){
        return "shop/admin/read";
    }

}
