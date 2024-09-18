package com.example.myProject1.token;

import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.service.CustomUDService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenUtils {
    private final Key keySecret;
    private final JwtParser jwtParser;

    public TokenUtils(
            @Value("${jwt.secret}")
            String code
    ) {
        this.keySecret = Keys.hmacShaKeyFor(code.getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.keySecret).build();
    }

    public String generateToken(UserDto dto){
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                .setSubject(dto.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(60*60*24)));
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(keySecret)
                .compact();
    }

    public boolean validate(String token){
        try{
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            if (claims.getExpiration().before(new Date())){
                return false;
            }
            return true;
        } catch (Exception e){
            e.getMessage();
        }
        return false;
    }

    public Claims parsetClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }

}
