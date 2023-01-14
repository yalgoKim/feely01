package com.project.feeely.service.auth;

import com.project.feeely.dto.auth.AuthorizationRequest;
import com.project.feeely.dto.auth.AuthorizationResponse;
import com.project.feeely.dto.enums.Roles;
import com.project.feeely.mapper.AuthorizationMapper;
import com.project.feeely.util.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

    private final AuthorizationMapper authorizationMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtil jwtUtil;


    public AuthorizationResponse authorizationUser(AuthorizationRequest authorizationRequest){
        return authorizationMapper.authorizationUser(authorizationRequest)
                .filter(authorizationResponse ->  // 결과값에서 거르고 싶은거 거른다 if문과 같음
                        bCryptPasswordEncoder.matches(authorizationRequest.getPassword() ,
                                authorizationResponse.getPassword())
                        ).map(authorizationResponse -> {
                            String token = jwtUtil.generateToken(authorizationResponse.getIdx(), authorizationResponse.getRole());
                            return AuthorizationResponse.createAuthorizationResponse(authorizationResponse.getRole(), token, authorizationResponse.getName());
                        }).orElseThrow(
                            () -> {throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
                            }
                );
    }

    public Map<String, ?> memberAuthorizationFindById (Long idx)  {
        return authorizationMapper.memberAuthorizationFindById(idx)
                .orElseThrow(() -> {throw new UsernameNotFoundException("존재하지 않는 회원입니다.");});
    }

}
