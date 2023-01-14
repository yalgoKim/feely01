package com.project.feeely.filter;

import com.project.feeely.dto.auth.AuthCheckRequest;
import com.project.feeely.dto.auth.AuthRequest;
import com.project.feeely.dto.auth.Member;
import com.project.feeely.dto.enums.ErrorCode;
import com.project.feeely.dto.enums.Roles;
import com.project.feeely.service.auth.AuthService;
import com.project.feeely.service.auth.AuthorizationService;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
// OncePerRequestFilter : 어느 서블릿 컨테이너에서나 요청 당 한 번의 실행을 보장하는 것을 목표로 한다.
//                          doFilterInternal메소드와 HttpServletRequest와 HttpServletResponse인자를 제공한다.
@Value("${spring.jwt.header}")
private String SPRING_JWT_HEADER;

    private JwtUtil jwtUtil;

    private AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);  // SC_OK : 상태 200일때 ~
            return;
        }

        URLMatcher saveUrlMatcher = new URLMatcher(List.of("/member/user/signup"));
        if(saveUrlMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        URLMatcher loginUrlMatcher = new URLMatcher(List.of("/auth/user/login"));
        if(loginUrlMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = request.getHeader(SPRING_JWT_HEADER);
        URLMatcher anyUrlMatcher = new URLMatcher(List.of("/auth/user/**"));
        if(anyUrlMatcher.matches(request)){

            if(token == null){;
                error(ErrorCode.UNAUTHORIZED);
                return;
            }

            try {
                if (!jwtUtil.validateToken(token)) {
                    error(ErrorCode.EXPIRED_TOKEN);
                    return;
                }
            } catch (Exception e) {
                error(ErrorCode.INVALID_TOKEN);
                return;
            }

            if (token != null){
                Map<String, ?> resultMap = authorizationService.memberAuthorizationFindById(jwtUtil.getUserIdx(token));
                if(resultMap.isEmpty()){
                    error(ErrorCode.NOT_FOUND_USER);
                    return;
                }
                Member member = Member.memberAuthorizationFindByIdVo(resultMap);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( member, null,  member.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    private void error(ErrorCode code) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();


        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();


        response.sendRedirect(request.getContextPath() + "/error/" + code.getValue());
    }

}
