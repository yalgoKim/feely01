package com.project.feeely.dto.save;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Alias("memberSignupRequest") // 별명 -> 쉽게 쓰려고
public class MemberSignupRequest {

    @NotNull
    private String name;
    @NotNull
    private String id;
    @NotBlank
    private String password;
}
