package com.project.feeely.mapper;

import com.project.feeely.dto.save.MemberSignupRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMapper {

    Optional<Integer> findByIdCount(String id);
    int userSave(MemberSignupRequest memberSignupRequest);
}
