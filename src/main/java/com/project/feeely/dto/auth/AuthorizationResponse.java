package com.project.feeely.dto.auth;

import com.project.feeely.dto.enums.Roles;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@ToString
@Alias("authorizationResponse")
public class AuthorizationResponse {
    private long idx;
    private Roles role;
    private List<GrantedAuthority> grantedAuth;
    private String token;
    private String name;
    private String password;

    private AuthorizationResponse(Roles role, String token, String name) {
        this.role = role;
        this.token = token;
        setGrantedAuth(role);
        this.name = name;
        this.password = null;
    }

    public static AuthorizationResponse createAuthorizationResponse (Roles role, String token, String name) {
        return new AuthorizationResponse(role, token, name);
    }

    private void setGrantedAuth(Roles role) {
        this.grantedAuth = List.of(new SimpleGrantedAuthority(role.getRole()));
    }
}
