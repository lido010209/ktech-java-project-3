package com.example.myProject1.token;

import com.example.myProject1.token.dto.RequestDto;
import com.example.myProject1.token.dto.ResponseDto;
import com.example.myProject1.user.AuthenticationFacade;
import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.service.CustomUDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenUtils tokenUtils;
    private final CustomUDService udService;
    private final PasswordEncoder encoder;

    public ResponseDto response(RequestDto dto){
        UserDto user = (UserDto) udService.loadUserByUsername(dto.getUsername());
        if (!encoder.matches(dto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Wrong password!!!");
        ResponseDto responseDto = ResponseDto.builder().token(tokenUtils.generateToken(user)).build();
        return responseDto;
    }

    // forgot password
    public ResponseDto tokenForgotPw(RequestDto dto){
        UserDto user = (UserDto) udService.loadUserByUsername(dto.getUsername());
        ResponseDto responseDto = ResponseDto.builder().token(tokenUtils.generateToken(user)).build();
        return responseDto;
    }

    public void changePassword(String token, String password){
        String username =tokenUtils.parsetClaims(token).getSubject();
        udService.changePassword(username, password);
    }

    // This token is extracted after success handler (success authentication)
//    public ResponseDto getToken(){
//        String username = new AuthenticationFacade().authentication().getName();
//        UserDto user = (UserDto) udService.loadUserByUsername(username);
//        ResponseDto responseDto = ResponseDto.builder().token(tokenUtils.generateToken(user)).build();
//        return responseDto;
//    }


}
