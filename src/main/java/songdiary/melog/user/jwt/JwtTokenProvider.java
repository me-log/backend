package songdiary.melog.user.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import songdiary.melog.user.dto.TokenDto;

import java.security.AlgorithmConstraints;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public TokenDto generateToken(Authentication authentication){
        System.out.println("genereate token"+authentication);
        //get Authentication
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        //create Access Token
        Date accessTokenExpire = new Date(now+1000*30);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //create Refresh Token
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now+1000*30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //get information from token to decode jwt token
    public Authentication getAuthentication(String accessToken){
        //decode Jwt token
        Claims claims = parseClaims(accessToken); //claims : section of payload
        if(claims.get("auth")==null) throw new RuntimeException("token don't have authentication");

        //GrantedAuthority : interface to represent permissions granted to the user
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)//SimpleGrantedAuthority : implementation of GrantedAuthority, create object representing authority of string(ROLE_USER or ROLE_ADMIN)
                .toList();

        //make UserDetails object and return Authentication
        //UserDetails : interface, User(class to implement UserDetails)
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch(ExpiredJwtException e){
            return e.getClaims();
        }
    }
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch(SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token", e);
        }catch(ExpiredJwtException e){
            log.info("Expired JWT Token", e);
        }catch(UnsupportedJwtException e){
            log.info("Unsupported JWT Token", e);
        }catch(IllegalArgumentException e){
            log.info("JWT claims string is empty", e);
        }
        return false;
    }
    public Long getMemberIdFromToken(String token){
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 이후의 문자열을 추출
        }
        Claims claims = parseClaims(token); // 토큰에서 클레임 추출
        // 클레임에서 사용자 ID를 추출하여 반환
        // 사용자 ID가 클레임에 저장되는 방식에 따라 적절히 수정해야 함
        return Long.parseLong(claims.getSubject());
    }
}
