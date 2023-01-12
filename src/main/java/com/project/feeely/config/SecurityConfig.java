package com.project.feeely.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
public class SecurityConfig {
    // 프론트에서 rest로 보낼 때 http요청에 대해 막는 효과가 있어서
    private final String[] CORS_ALLOW_METHOD = {"GET", "POST", "PATCH", "DELETE", "OPTIONS"};

    @Bean // 시큐리티 5부턴 걍 이거 무조건 써야함
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 얘네는 앞단과 뒷단 연결할 때 아이피, 포트갸ㅏ 다르니까 그거 다 설정 열어주는(?) 거
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of(CORS_ALLOW_METHOD));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 쿠키에 얘를 넣고 왔다갔다 하는거라 무상태 처리..?
                .and()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .csrf().disable();
        return http.build();
    }

    @Bean
    // security에 영향을 받지 않는 url을 잡아주겠다 (index 넣어줌 test로)
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/");
    }
}
