package com.project.feeely.controller.member.authorization;

import com.project.feeely.dto.Response;
import com.project.feeely.dto.auth.AuthRequest;
import com.project.feeely.dto.auth.AuthorizationRequest;
import com.project.feeely.dto.auth.AuthorizationResponse;
import com.project.feeely.service.auth.AuthorizationService;
import com.project.feeely.util.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    // 유저로그인
    @PostMapping("/user/login")
    public Response<AuthorizationResponse> authorizationUser(@RequestBody @Valid AuthorizationRequest authorizationRequest , BindingResult bindingResult) throws RestException{
        if(bindingResult.hasErrors()) {
            throw new RestException(403, "아이디나 비밀번호가 일치하지 않습니다."); // 앞단에서 null이라서 이 상태의 익셉션을 받으면 , 코드번호와 메세지를 받을 수 있음..
        }
        return new Response.ResponseBuilder<AuthorizationResponse>("OK", 200)
                .resultResponse(authorizationService.authorizationUser(authorizationRequest))
                .total(1) // 결과값이 한개, 리스트면 리스트 수
                .build(); // 서비스에 있는 인티저가 response 로 들어감
    }
}
