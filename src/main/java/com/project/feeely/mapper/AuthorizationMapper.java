package com.project.feeely.mapper;

import com.project.feeely.dto.auth.AuthorizationRequest;
import com.project.feeely.dto.auth.AuthorizationResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AuthorizationMapper {
    List<List<Object>> authMemberDetail(String id);
    Optional<AuthorizationResponse> authorizationUser(AuthorizationRequest authorizationRequest);

    Optional<Map<String, ?>> memberAuthorizationFindById(Long idx);
}
