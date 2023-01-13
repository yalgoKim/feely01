package com.project.feeely.filter;

import com.project.feeely.dto.auth.AuthCheckRequest;
import com.project.feeely.dto.auth.AuthRequest;
import com.project.feeely.dto.enums.Roles;
import com.project.feeely.service.auth.AuthService;
import com.project.feeely.util.auth.JwtUtil;
import com.project.feeely.util.auth.URLMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
// OncePerRequestFilter : 어느 서블릿 컨테이너에서나 요청 당 한 번의 실행을 보장하는 것을 목표로 한다.
//                          doFilterInternal메소드와 HttpServletRequest와 HttpServletResponse인자를 제공한다.

    @Value("Authorization")
    private String SPRING_JWT_HEADER;

    private JwtUtil jwtUtil;
    private AuthService authService;


    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // SC_OK : 상태 200일때 ~
            return;
        }

        final String token = request.getHeader(SPRING_JWT_HEADER);
        URLMatcher userMatcher = new URLMatcher(List.of("/user/**"));   // 관리자

        if (userMatcher.matches(request)) {
            // 토큰이 없는 경우 에러
            if (token == null) {
                throw new IllegalArgumentException("토큰이 없습니다.");
            }

            try {
                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("토큰이 만료되었습니다.");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("서버오류");
            }


            if (token != null) {
                // Role 설정
                List<GrantedAuthority> authorities = new ArrayList<>();

                // 관리자 AuthController 에서 받아서 처리할 때 "user" 를 타이프로 주면 어드민 인거 체크함.
                if ("user".equals(jwtUtil.getType(token))) {
                    // 관리자 정보 조회
                    AuthCheckRequest authCheckRequest = authService.authMemberDetail(jwtUtil.getUserId(token));

                    if (authCheckRequest == null) {
                        throw new IllegalArgumentException("유저가 존재하지 않습니다.");
                    }

                    List<String> authList = authCheckRequest.getAuth();
                    for (String auth : authList) {
                        if (auth.equals("ROLE_USER")) {
                            authorities.add(new SimpleGrantedAuthority(Roles.USER.toString()));
                        } else {
                            throw new IllegalStateException("유효한 권한이 존재하지 않습니다.");
                        }
                    }
                }

                // 세션 저장
                AuthRequest member = new AuthRequest(jwtUtil.getUserId(token) , authorities);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
