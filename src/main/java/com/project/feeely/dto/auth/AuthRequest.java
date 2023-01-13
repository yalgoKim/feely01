package com.project.feeely.dto.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class AuthRequest {

    private String id;
    private List<GrantedAuthority> authorities;
}
