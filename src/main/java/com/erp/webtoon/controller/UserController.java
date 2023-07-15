package com.erp.webtoon.controller;

import com.erp.webtoon.dto.token.LogOutRequestDto;
import com.erp.webtoon.dto.token.TokenRequestDto;
import com.erp.webtoon.dto.token.TokenResponseDto;
import com.erp.webtoon.dto.user.LoginRequestDto;
import com.erp.webtoon.dto.user.SlackRequestDto;
import com.erp.webtoon.dto.user.UserRequestDto;
import com.erp.webtoon.service.JwtService;
import com.erp.webtoon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody UserRequestDto userRequestDto){
        userService.addNewCome(userRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto loginDto, HttpServletResponse response) {
        TokenResponseDto tokenDto = userService.login(loginDto);

        Cookie cookie = new Cookie("RefreshToken", String.format(tokenDto.getRefreshToken()));
        cookie.setPath("/");
        cookie.setMaxAge(tokenDto.getRefreshExpire());
        cookie.setHttpOnly(true); // 서버만 쿠키에 접근
        cookie.setSecure(false);
        response.addCookie(cookie);

        return tokenDto;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated TokenRequestDto reissue, Errors errors) {
        return userService.reissue(reissue);
    }

    @PostMapping("/sendPassword")
    public String sendPassword(@RequestParam("userEmail") String userEmail){
        SlackRequestDto dto = userService.createMailAndChangePassword(userEmail);

        return "/users/login";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Validated LogOutRequestDto logout, Errors errors) {
        return userService.logout(logout);
    }
}

