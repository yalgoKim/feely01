package com.project.feeely.service.auth;

import com.project.feeely.dto.auth.AuthCheckRequest;
import com.project.feeely.mapper.AuthorizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthorizationMapper authorizationMapper;

    public AuthCheckRequest authMemberDetail(String id) {
        List<List<Object>> resultMap = authorizationMapper.authMemberDetail(id);

        if(resultMap.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        String resultId = (String) resultMap.get(1).get(0);
        List<String> authList = (List<String>) (Object) resultMap.get(0);
        if (resultMap.get(0).get(0) != null) {
            return new AuthCheckRequest(resultId, authList);
        } else {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
