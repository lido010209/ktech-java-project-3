package com.example.myProject1.service;

import com.example.myProject1.order.entity.Order;
import com.example.myProject1.user.AuthenticationFacade;
import com.example.myProject1.user.entity.Authority;
import com.example.myProject1.user.entity.UserEntity;
import com.example.myProject1.user.repo.AuthorityRepo;
import com.example.myProject1.user.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WebService {
    private final AuthorityRepo authorityRepo;
    private final UserRepo userRepo;

    // Update authority for user
    @Transactional
    public UserEntity updateAuthority(String authority, UserEntity user){
        Authority setAuth = authorityRepo.findByAuthority(authority)
                .orElseThrow();
        user.getAuthorities().add(setAuth);
        return userRepo.save(user);
    }

    @Transactional
    public UserEntity removeAuthority(String authority, UserEntity user){
        Authority setAuth = authorityRepo.findByAuthority(authority)
                .orElseThrow();
        user.getAuthorities().remove(setAuth);
        return userRepo.save(user);
    }

    // Decentralization of authority
    @Transactional
    public UserEntity userBusiness(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        Authority authority = authorityRepo.findByAuthority("ROLE_BUSINESS").orElseThrow();
        if (!user.getAuthorities().contains(authority))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You do not have this authority!!!");
        return user;
    }

    @Transactional
    public UserEntity userAdmin(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        Authority authority = authorityRepo.findByAuthority("ROLE_ADMIN").orElseThrow();
        if (!user.getAuthorities().contains(authority))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You do not have this authority!!!");
        return user;
    }

    @Transactional
    public UserEntity user(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        Authority authority = authorityRepo.findByAuthority("ROLE_USER").orElseThrow();
        if (!user.getAuthorities().contains(authority))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You do not have this authority!!!");return user;
    }

    @Transactional
    public UserEntity viewer(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        return user;
    }


    // Set payment for buyer
    @Transactional
    public UserEntity calculatePayment(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        Integer sum =0;
        if (!user.getOrders().isEmpty()) {
            for (Order order : user.getOrders()) {
                if (order.getStatus().getName().equals("Ordered")) {
                    sum += order.getTotal();
                }
            }
        }
        user.setPayment(sum);
        user = userRepo.save(user);
        return user;
    }
}
