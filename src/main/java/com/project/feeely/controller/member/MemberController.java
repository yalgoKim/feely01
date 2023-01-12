package com.project.feeely.controller.member;

import com.project.feeely.dto.Response;
import com.project.feeely.dto.save.MemberSignupRequest;
import com.project.feeely.service.member.MemberService;
import com.project.feeely.util.exceptions.RestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(@Qualifier("userService") MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/user/save")
    public Response<Integer> userSave(@RequestBody @Valid MemberSignupRequest memberSignupRequest , BindingResult bindingResult ) throws RestException {
        // @Valid : dto 상 @NotNull 읽어서 값이 null이면 팅겨내줌
        // BindingResult : name = 1 , id = 2, password = null인데 @Notnull이라 에러가 나타나는데, BindingResult로 한번 바인딩 해줄 수 있음
        if(bindingResult.hasErrors()) {
            throw new RestException(403, "알맞은 값을 입력해 주세요."); // 앞단에서 null이라서 이 상태의 익셉션을 받으면 , 코드번호와 메세지를 받을 수 있음..
        }
        return new Response.ResponseBuilder<Integer>("OK", 200)
                .resultResponse(memberService.userSave(memberSignupRequest))
                .total(1) // 결과값이 한개, 리스트면 리스트 수
                .build(); // 서비스에 있는 인티저가 response 로 들어감
    }
}
