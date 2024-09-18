package com.example.myProject1.user.service;

import com.example.myProject1.user.dto.AuthorityDto;
import com.example.myProject1.user.entity.Authority;
import com.example.myProject1.user.repo.AuthorityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    private final AuthorityRepo authorityRepo;
    private static final String[] authorities =
            {"ROLE_USER", "ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_VIEWER"};

    public AuthorityService(AuthorityRepo authorityRepo) {
        this.authorityRepo = authorityRepo;
        for (String authority: authorities){
            if (!authorityRepo.existsByAuthority(authority)){
                Authority newAuthority= authorityRepo.save(
                        Authority.builder().authority(authority).build()
                );
                AuthorityDto.dto(authorityRepo.save(newAuthority));
            }
        }
    }


}
