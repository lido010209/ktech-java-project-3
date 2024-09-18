package com.example.myProject1.token;

import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.service.CustomUDService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class TokenFilterHandler extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;
    private final CustomUDService udService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String stringHeader = request.getHeader("Authorization");
//        log.info(stringHeader);
        if (stringHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        String[] headSplit = stringHeader.split(" ");
        if (!(headSplit.length==2 && headSplit[0].equals("Bearer"))){
            filterChain.doFilter(request, response);
            return;
        }

        String token = headSplit[1];
        if (!tokenUtils.validate(token)){
            filterChain.doFilter(request, response);
            return;
        }
        String username = tokenUtils.parsetClaims(token).getSubject();
        UserDto user = (UserDto) udService.loadUserByUsername(username);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()
                );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}
