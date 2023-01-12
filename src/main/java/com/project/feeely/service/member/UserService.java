package com.project.feeely.service.member;

import com.project.feeely.dto.save.MemberSignupRequest;
import com.project.feeely.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements MemberService {

    private final MemberMapper memberMapper; // DB 저장해야하니까 필요함
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // 암호화해야하니까 필요하고
    @Override
    public Integer userSave(MemberSignupRequest memberSignupRequest){

        memberMapper.findByIdCount(memberSignupRequest.getId())
                .filter(count -> count == 0).orElseThrow( () -> { // 결과값이 0과 같다면 정상 루트를 타고, 0이 아니라면 IllegalArgumentException 터트림
                    throw new IllegalArgumentException("존재하는 회원입니다.");
                });

        String password = memberSignupRequest.getPassword();
        memberSignupRequest.setPassword(bCryptPasswordEncoder.encode(password));
        return memberMapper.userSave(memberSignupRequest);
    }
}
