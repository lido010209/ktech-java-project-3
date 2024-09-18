package com.example.myProject1.token;

import com.example.myProject1.token.dto.RequestDto;
import com.example.myProject1.token.dto.ResponseDto;
import com.example.myProject1.user.AuthenticationFacade;
import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.service.CustomUDService;
import com.example.myProject1.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("issue")
    public ResponseDto token(
            @RequestBody
            RequestDto dto
    ){
        return tokenService.response(dto);
    }


    // forgot pw
    @PostMapping("forgot-password")
    public ResponseDto tokenForgotPw(
            @RequestBody
            RequestDto dto
    ){
        return tokenService.tokenForgotPw(dto);
    }

    @PostMapping("reset-password")
    public String test(
            @RequestBody
            RequestDto dto
    ){
        tokenService.changePassword(dto.getJwt(), dto.getPassword());
        return "change password successful!!!";
    }


}
