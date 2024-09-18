package com.example.myProject1.user.service;

import com.example.myProject1.order.entity.Order;
import com.example.myProject1.user.AuthenticationFacade;
import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.entity.Authority;
import com.example.myProject1.user.entity.UserEntity;
import com.example.myProject1.user.repo.AuthorityRepo;
import com.example.myProject1.user.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class AfterLoginService {
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final AuthorityRepo authorityRepo;

    public UserDto updateAccount(UserDto dto){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        if (userRepo.existsByEmail(dto.getEmail()) && !user.getEmail().equals(dto.getEmail()))
            throw new IllegalArgumentException("Email already exists!!!");
        if (userRepo.existsByPhone(dto.getPhone()) && !user.getPhone().equals(dto.getPhone()))
            throw new IllegalArgumentException("Phone number already exists!!!");
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setName(dto.getName());
        return UserDto.userDto(userRepo.save(user));
    }

    public UserDto changePassword(String oldPassword, String newPassword){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        if (!encoder.matches(oldPassword, user.getPassword()))
            throw new IllegalArgumentException("Password is wrong!!!");
        user.setPassword(encoder.encode(newPassword));
        return UserDto.userDto(userRepo.save(user));
    }

    public UserDto updateImg(MultipartFile image){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));

        String directory = "profile/"+user.getId()+"/";
        try{
            Files.createDirectories(Path.of(directory));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }
        String fileName= image.getOriginalFilename();
        String[] eleFile= fileName.split("\\.");
        String extension = eleFile[eleFile.length-1];

        String path = directory+ "profile."+extension;
        try{
            image.transferTo(Path.of(path));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }

        String url = "/static/"+user.getId()+"/profile."+extension;
        user.setProfileImg(url);
        return UserDto.userDto(userRepo.save(user));
    }

//    public UserDto updateAuthority(String authority){
//        Authority setAuth = authorityRepo.findByAuthority(authority)
//                .orElseThrow();
//        String username = new AuthenticationFacade().authentication().getName();
//        UserEntity user = userRepo.findByUsername(username)
//                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
//
//        user.getAuthorities().add(setAuth);
//        return UserDto.userDto(userRepo.save(user));
//    }
}
