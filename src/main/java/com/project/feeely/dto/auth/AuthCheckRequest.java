package com.project.feeely.dto.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
public class AuthCheckRequest {

    private String id;
    private List<String> auth;
    private String token;
    private String name;

    public AuthCheckRequest(String id, List<String> auth) {
        this.id = id;
        this.auth = auth;
    }
}
