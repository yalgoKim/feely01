package com.project.feeely.dto.auth;

import com.project.feeely.dto.enums.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class Member implements UserDetails {

    private final long idx; // index

    private final String username;

    private String password;

    private final List<GrantedAuthority> authorities;

    private final boolean enabled;

    public Member(long idx, String username, String password, List<GrantedAuthority> authorities, boolean enabled) {
        this.idx = idx;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    @Builder
    public Member(long idx, String username, List<GrantedAuthority> authorities, boolean enabled) {
        this.idx = idx;
        this.username = username;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public static Member memberAuthorizationFindByIdVo(Map<String, ?> authorizationMemberInfo) {
        return Member.builder()
                .username(authorizationMemberInfo.get("id").toString())
                .idx((Integer) authorizationMemberInfo.get("idx"))
                .enabled((boolean) authorizationMemberInfo.get("enabled")) // 괄호 안은 내 db의 컬럼명과 맞춰줘야 함
                .authorities(
                        List.of(
                                new SimpleGrantedAuthority(
                                        Roles.findByCodeEnum(
                                                authorizationMemberInfo.get("role").toString()).getRole()
                                        )
                                )
                ).build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
