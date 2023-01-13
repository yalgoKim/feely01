package com.project.feeely.util.auth;

import com.project.feeely.dto.auth.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {
    // JwtUtil : JWT를 생성하고 검증하는 역할을 수행함
    @Value("eWFsZ29faXNfZnJlZQ==")
    private String SPRING_JWT_SECRET;

//    서명은 비밀키를 포함하여 암호화되어있음.
//    토큰을 인코딩하거나 유효성을 검증할 때 사용하는 고유한 암호화 코드
//    헤더, 페이로드의 값을 Base64로 인코딩하고 이 값을 비밀 키를 이용해 헤더에서 정의한 알고리즘으로 해싱
//    그리고 해싱된 값을 다시 Base64로 인코딩하여 생성합니다. 그리고 우리는 yml에 이걸 미리 지정해뒀음~

    // 유저정보 가져오기
    public AuthRequest getMember() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            return null;
        }
        return (AuthRequest) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    // 우리가 흔히 하는 아이디/패스워드 사용자 정보를 넣고 실제 가입된 사용자인지 체크한후 인증에 성공하면
    // 사용자의 principal과 credential 정보를 Authentication에 담는다.
    // 그리고 Spring Security에서 방금 담은 Authentication을  SecurityContext에 보관
    // 이 SecurityContext를 SecurityContextHolder에 담아 보관
    // getAuthentication() 메서드는 GrantedAuthority 를 상속 받은 권한 객체 Collection을 Return 한다.

    //이렇게, 순차적으로 사용자의 정보를 담아놓고, 필요한 곳에서 다시 현재 로그인한 사용자의 정보를 얻기 위해서는
    // 위와 같이 접근하면 됨...


    // JWT 토큰 생성
    public String generateToken(String id, String type) {
        return Jwts.builder()
                .claim("id", id) // claim :
                .claim("TYPE", type)
                .setIssuedAt(new Date()) // 토큰 발행 시간 정보
                .setExpiration(getExpiration()) // 만료시간 세팅
                .signWith(getKey()) // secret값 세팅
                .compact();
    }

    // jwt의 payload
    //    토큰에 담을 클레임(Claim) 정보를 포함하고 있습니다.
    //    클레임은 json 형태로 key-value 한 쌍으로 이루어져 있습니다.
    //    클레임의 정보는 등록된 (registered) 클레임, 공개(public) 클레임, 비공개(private) 클레임으로 세 종류가 있습니다.

    // 토큰의 만료시간 가져오는 메서드
    private Date getExpiration() {
        return Date.from(LocalDateTime.now().with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }

    // 사용할 jwt secret값 세팅
    private Key getKey() {
        return Keys.hmacShaKeyFor(SPRING_JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰의 유효성 및 만료일자 확인
    public boolean validateToken(String token) {
        try {
            //  토큰 만료
            final Date expiration = getClaims(token).getExpiration();
            return !expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 토큰에서 회원 아이디 추출
    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

    // 토큰에서 회원 타입(?) 추출
    public String getType(String token) {
        Claims claims = getClaims(token);
        return claims.get("TYPE", String.class);
    }

    // 뭔가.. 토큰의 몸통을 가져오는 작업일까..
    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

}
