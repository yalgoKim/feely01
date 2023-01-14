package com.project.feeely.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public interface CodeEnum {

    @JsonValue // json 형태로 바꿔주는것
    String getCode();
}
