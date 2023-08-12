package Gennet.backend.auth.config;

import Gennet.backend.auth.filter.JwtAuthenticationFilter;
import Gennet.backend.auth.filter.JwtVerificationFilter;
import Gennet.backend.auth.handler.MemberAccessDeniedHandler;
import Gennet.backend.auth.handler.MemberAuthenticationEntryPoint;
import Gennet.backend.auth.handler.MemberAuthenticationFailureHandler;
import Gennet.backend.auth.handler.MemberAuthenticationSuccessHandler;
import Gennet.backend.auth.jwt.JwtTokenizer;
import Gennet.backend.auth.repository.AuthRepository;
import Gennet.backend.auth.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final AuthRepository authRepository;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin() // h2에서 사용하기 위함(X-Frame-Options->해당 페이지와 동일한 orgin에 해당하는 frame만 표시)
                .and()
                .csrf().disable()        // 로컬에서 진행하므로 CSRF 공격 비활성화
                .cors(withDefaults())    // corsConfigurationSource라는 이름으로 등록된 Bean을 이용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션을 생성하지 않도록 설정
                .and()
                .formLogin().disable()   // JSON포맷으로 username과 password 전송하는 방식 사용
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())   // Spring Security의 Configuration을 개발자가 직접 정의할 수 있음
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.PATCH, "/members/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/members/**").hasAnyRole("USER")
                        .anyRequest().permitAll()                // HTTP request 요청에 대한 접근 권한 설정(URL 별로 권한 적용할 수 있음)
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // CorsConfigurationSource Bean 생성을 통해 구체적인 CORS 정책을 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));   // 모든 출처(Origin)에 대해 스크립트 기반의 HTTP 통신을 허용하도록 설정
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));  // 파라미터로 지정한 HTTP Method에 대한 HTTP 통신을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();   // CorsConfigurationSource 인터페이스의 구현 클래스의 객체 생성
        source.registerCorsConfiguration("/**", configuration); //모든 URL에 앞에서 구성한 CORS 정책 적용
        return source;
    }
    // JwtAuthenticationFilter를 등록하는 역할
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {  // Configuration을 커스터마이징
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);  // AuthenticationManager의 객체를 얻음

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer,authRepository);  //  AuthenticationManager와 JwtTokenizer를 DI
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());  //
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());  //

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);  // JwtVerificationFilter의 인스턴스를 생성하면서 JwtVerificationFilter에서 사용되는 객체들을 생성자로 DI

            builder
                    .addFilter(jwtAuthenticationFilter)  // Spring Security Filter Chain에 추가
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);   // JwtVerificationFilter는 JwtAuthenticationFilter에서 로그인 인증에 성공한 후
                                                                                             // 발급받은 JWT가 클라이언트의 request header(Authorization 헤더)에 포함되어 있을 경우에만 동작

        }
    }
}