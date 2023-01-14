package com.project.feeely.dto.auth;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@Alias("authorizationRequest")
public class AuthorizationRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String password;
}
