package Gennet.backend.auth.controller;

import Gennet.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    //Todo : 1. 클라이언트에서 만료된 access token 보냈을 때 만료됐다고 알려줘야함 -> dofilter에 되어있음
    //       2. 클라이언트에서 access token + refresh token 보내면(재발급 요청) 토큰 검증하고 새로운 access token 발급해줘야함
    //       3. logout 구현
    //          현재 -> 만료된 토큰이 500에러로 나옴
    //                  재발급 안됨
    private final AuthService authService;

    /** Access token 재발급 **/
    @PostMapping("/reissue")
    public ResponseEntity reissue (HttpServletRequest request, HttpServletResponse response) {

        authService.reissue(request, response);
        return ResponseEntity.ok().build();
    }

    /** 로그아웃 **/
    @DeleteMapping("/logout")
    public ResponseEntity logout (HttpServletRequest request) {

        authService.logout(request);
        return ResponseEntity.ok().build();
    }
}
