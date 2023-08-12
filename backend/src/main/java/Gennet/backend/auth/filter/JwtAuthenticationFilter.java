package Gennet.backend.auth.filter;

import Gennet.backend.auth.dto.LoginDto;
import Gennet.backend.auth.entity.RefreshToken;
import Gennet.backend.auth.jwt.JwtTokenizer;
import Gennet.backend.auth.repository.AuthRepository;
import Gennet.backend.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

    // 로그인 인증 요청을 처리
@RequiredArgsConstructor
 public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final AuthRepository authRepository;

        // 인증 시도
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();    // 클라이언트에서 보낸 email, password를 dto로 역직렬화하기 위한 인스턴스 생성
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class); // ServletInputStream -> LoginDto(역직렬화)

        //  Username과 Password 정보를 포함한 UsernamePasswordAuthenticationToken을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            return authenticationManager.authenticate(authenticationToken);  // UsernamePasswordAuthenticationToken을 AuthenticationManager에게 전달, 인증 처리 위임
        }
        // 클라이언트의 인증 정보를 이용해 인증에 성공했을 때
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        Member member = (Member) authResult.getPrincipal();  // authResult.getPrincipal()로 Member 엔티티 클래스의 객체를 얻음
        // AuthenticationManager 내부에서 인증에 성공 -> 인증된 Authentication 객체가 생성되면서 principal 필드에 Member 객체가 할당
        String accessToken = delegateAccessToken(member);   // delegateAccessToken 메서드를 이용해 Access Token 생성
        String refreshToken = delegateRefreshToken(member); // Refresh Token 생성

        // refreshtoken db에 저장
        RefreshToken saveRefreshToken = RefreshToken.builder()
                .memberId(member.getMemberId())
                .refreshToken(refreshToken)
                .build();

        authRepository.save(saveRefreshToken);

        response.setHeader("Authorization", "Bearer " + accessToken);  // response header(Authorization)에 Access Token 추가
                                                                                      // 요청을 보낼 때마다 request header에 추가해서 클라이언트 측의 자격 증명
        response.setHeader("Refresh", refreshToken);                   // Access Token이 만료 -> Access Token을 클라이언트에게 추가적으로 제공
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

        // Access Token 생성하는 구체적인 로직
    public String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

        // Refresh Token 생성하는 구체적인 로직
    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}
