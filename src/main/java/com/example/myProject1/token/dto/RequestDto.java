package com.example.myProject1.token.dto;

import lombok.Data;

@Data
public class RequestDto {
    private String username;
    private String password;
    private String jwt;
}
