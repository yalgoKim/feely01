package com.project.feeely.service.member;

import com.project.feeely.dto.save.MemberSignupRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements MemberService {

    @Override
    public Integer userSave(MemberSignupRequest memberSignupRequest) {
        return null;
    }
}
