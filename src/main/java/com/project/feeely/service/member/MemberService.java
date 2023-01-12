package com.project.feeely.service.member;

import com.project.feeely.dto.save.MemberSignupRequest;

public interface MemberService {

    public Integer userSave(MemberSignupRequest memberSignupRequest);
}
