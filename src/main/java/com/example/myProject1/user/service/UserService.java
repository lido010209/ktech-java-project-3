package com.example.myProject1.user.service;

import com.example.myProject1.shop.entity.Category;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.status.Status;
import com.example.myProject1.status.StatusRepo;
import com.example.myProject1.shop.repo.CategoryRepo;
import com.example.myProject1.item.repo.ItemRepo;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.user.AuthenticationFacade;
import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.entity.Authority;
import com.example.myProject1.user.entity.UserEntity;
import com.example.myProject1.user.repo.AuthorityRepo;
import com.example.myProject1.user.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final PasswordEncoder encoder;
    private final StatusRepo statusRepo;
    private final ShopRepo shopRepo;
    private final CategoryRepo categoryRepo;

    public UserService(UserRepo userRepo, AuthorityRepo authorityRepo, PasswordEncoder encoder, StatusRepo statusRepo, ShopRepo shopRepo, CategoryRepo categoryRepo) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.encoder = encoder;
        this.statusRepo = statusRepo;
        this.shopRepo = shopRepo;
        this.categoryRepo = categoryRepo;
    }

    public UserDto infoLogin(UserDto dto){
        if (userRepo.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username already exists!!!");

        UserEntity newUser = UserEntity.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .profileImg("/static/visual/user.png")
                .build();
        Authority authority1 = authorityRepo.findByAuthority("ROLE_VIEWER").orElseThrow();
        newUser.getAuthorities().add(authority1);
        return UserDto.userDto(userRepo.save(newUser));
    }

    public UserDto create(UserDto dto){
        String userId = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(userId)
                .orElseThrow(()-> new IllegalArgumentException("Account id does not exist!!!"));
        if (userRepo.existsByEmail(dto.getEmail()) && !user.getEmail().equals(dto.getEmail()))
            throw new IllegalArgumentException("Email already exists!!!");
        if (userRepo.existsByPhone(dto.getPhone()) && !user.getPhone().equals(dto.getPhone()))
            throw new IllegalArgumentException("Phone number already exists!!!");
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAge(dto.getAge());
        user.setNickname(dto.getNickname());

        Authority authority1 = authorityRepo.findByAuthority("ROLE_USER").orElseThrow();
        user.getAuthorities().add(authority1);
        user = userRepo.save(user);

        if (user.getShop()==null) {
            Category categoryUser = categoryRepo.findByName("").orElseThrow();
            Status status = statusRepo.findByName("Pending open").orElseThrow();
            Shop shop = Shop.builder()
                    .owner(user)
                    .status(status)
                    .category(categoryUser)
                    .name("")
                    .introduction("")
                    .build();
            shopRepo.save(shop);
        }
        return UserDto.userDto(user);
    }

}
