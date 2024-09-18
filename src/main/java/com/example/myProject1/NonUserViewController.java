package com.example.myProject1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("lu")
public class NonUserViewController {
    @GetMapping("home")
    public String home(){
        return "home";
    }

    @GetMapping
    public String createAccount(){
        return "user/create";
    }

    @GetMapping("login")
    public String login(){
        return "user/login";
    }

    @GetMapping("edit")
    public String register(){
        return "user/register";
    }
}
