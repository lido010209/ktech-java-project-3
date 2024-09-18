package com.example.myProject1.user;

import com.example.myProject1.token.dto.ResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public Authentication authentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
