package com.example.myProject1.user;

import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.service.AfterLoginService;
import com.example.myProject1.user.service.CustomUDService;
import com.example.myProject1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final UserService userService;
    private final AfterLoginService afterLoginService;
    private final CustomUDService udService;

    @PostMapping
    public ResponseEntity<UserDto> infoLogin(
            @RequestBody
            UserDto dto
    ){
        UserDto user = userService.infoLogin(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @GetMapping
    public UserDto read(){
        String username = new AuthenticationFacade().authentication().getName();
        return (UserDto) udService.loadUserByUsername(username);
    }

    @PutMapping
    public ResponseEntity<UserDto> signIn(
            @RequestBody
            UserDto dto
    ){
        UserDto user = userService.create(dto);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @PutMapping("avatar")
    public ResponseEntity<UserDto> updateImg(
            @RequestParam
            MultipartFile image
    ){
        UserDto user = afterLoginService.updateImg(image);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @PutMapping("change-password")
    public ResponseEntity<UserDto> changePassword(
            @RequestParam("oldPassword")
            String oldPassword,
            @RequestParam("newPassword")
            String newPassword
    ){
        UserDto user = afterLoginService.changePassword(oldPassword, newPassword);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }
}
