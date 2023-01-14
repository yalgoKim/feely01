package com.project.feeely.dto.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class AuthRequest {

    private String id;
    private List<GrantedAuthority> authorities;

}
