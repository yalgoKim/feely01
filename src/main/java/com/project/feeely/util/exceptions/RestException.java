package com.project.feeely.util.exceptions;

import com.project.feeely.dto.Response;
import lombok.Getter;

@Getter
public class RestException extends Exception {

    private final Response response;

    public RestException(int code, String message) {
        response = new Response.ResponseBuilder<>(message, code)
                .build();
    }
}
