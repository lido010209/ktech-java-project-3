package com.example.myProject1.user.dto;

import com.example.myProject1.item.entity.Item;
import com.example.myProject1.order.entity.Order;
import com.example.myProject1.user.entity.Authority;
import com.example.myProject1.user.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String nickname;
    private Integer age;
    private Integer payment;
    private String profileImg;
    private Integer numberOrder;
    private Integer numberSell;

    private final Set<String> stringAuthorities = new HashSet<>();

    public static UserDto userDto(UserEntity user){
        UserDto dto = new UserDto();
        dto.id= user.getId();
        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.phone = user.getPhone();
        dto.nickname = user.getNickname();
        dto.age = user.getAge();
        dto.payment = user.getPayment();
        dto.profileImg = user.getProfileImg();
        for (Authority authority: user.getAuthorities()){
            dto.stringAuthorities.add(authority.getAuthority());
        }
        Integer sum =0;
        Integer sumSell=0;
        for (Order order: user.getOrders()){
            if (order.getStatus().getName().equals("Ordered")) sum+=1;
        }
        dto.numberOrder=sum;
        if (user.getShop()!=null && user.getShop().getStatus().getName().equals("Opened")){
            for (Item item: user.getShop().getItems()){
                for (Order order: item.getOrders()){
                    if (order.getStatus().getName().equals("Ordered")) sumSell+=1;
                }
            }
        }
        dto.numberSell = sumSell;
        return dto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stringAuthorities.stream().map(SimpleGrantedAuthority::new).toList();
    }



//    @Override
//    public String getUsername() {
//        return userId;
//    }
}
