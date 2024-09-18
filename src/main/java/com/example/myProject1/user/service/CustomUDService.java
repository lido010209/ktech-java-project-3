package com.example.myProject1.user.service;

import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.entity.UserEntity;
import com.example.myProject1.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomUDService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {
        UserEntity user = userRepo.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Username does not exist!!!")
        );
        return UserDto.userDto(user);
    }

    public UserDto changePassword(String username, String password){
        UserEntity user = userRepo.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Username does not exist!!!")
        );
        user.setPassword(encoder.encode(password));
        return UserDto.userDto(userRepo.save(user));
    }
}
