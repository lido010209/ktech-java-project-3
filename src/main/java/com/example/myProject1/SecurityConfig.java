package com.example.myProject1;

import com.example.myProject1.token.TokenUtils;
import com.example.myProject1.token.TokenFilterHandler;
import com.example.myProject1.user.service.CustomUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenUtils tokenUtils;
    private final CustomUDService udService;
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    // front-end
                    auth.requestMatchers("/error", "/lu/**", "/lu/login", "/static/**").permitAll();

                    // backend
                    auth.requestMatchers("/token/issue",
                                    "/token/forgot-password")
                            .permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/").permitAll();
                    auth.requestMatchers("/").hasRole("VIEWER");
                    auth.requestMatchers("/avatar", "/malls").hasRole("VIEWER");
                    auth.requestMatchers("/malls/**", "/search/**").hasRole("USER");
                    auth.requestMatchers("/manage-items/**").hasRole("BUSINESS");
                    auth.requestMatchers("/shops/**").hasRole("USER");
                    auth.requestMatchers("/manage-shops/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(
                        new TokenFilterHandler(
                                tokenUtils,
                                udService
                        ),
                        AuthorizationFilter.class
                )
        ;
        return http.build();
    }
}
