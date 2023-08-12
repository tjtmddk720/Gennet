package Gennet.backend.auth.jwt;

import Gennet.backend.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenizer {
    @Getter
    @Value("${jwt.key}")
    private String secretKey; // JWT 생성 및 검증 시 사용

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes; // Access Token 만료 시간 정보

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;          // Refresh Token 만료 시간 정보

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
        return claims;
    }

    /** token으로 memberId 추출 메서드 **/
    public Long getMemberIdFromToken(String token, String base64EncodedSecretKey){
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        String jwtToken = token.substring("Bearer ".length());

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);

        Claims claims = claimsJws.getBody();

        Long memberId = claims.get("memberId", Long.class);

        return memberId;
    }

    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    public Jws<Claims> verifySignature(String jws){

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(jws);
        } catch (ExpiredJwtException exception) {
            throw new RuntimeException();
        }
    }


    // JWT의 만료 일시를 지정, JWT 생성 시 사용
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    /** Access Token 생성 **/
    public String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("memberId", member.getMemberId()); // 식별자 포함
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = getTokenExpiration(getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        String accessToken = generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }
}
