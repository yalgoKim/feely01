package com.project.feeely.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorizationMapper {
    List<List<Object>> authMemberDetail(String id);
}
