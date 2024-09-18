package com.example.myProject1.user.dto;

import com.example.myProject1.user.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityDto {
    private Long id;
    private String authority;

    public static AuthorityDto dto(Authority authority){
        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.id = authority.getId();
        authorityDto.authority = authority.getAuthority();
        return authorityDto;
    }
}
