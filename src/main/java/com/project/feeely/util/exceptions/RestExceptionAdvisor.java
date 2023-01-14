package com.project.feeely.util.exceptions;

import com.project.feeely.dto.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionAdvisor {


    @ExceptionHandler(RestException.class) @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public Response handleRestException(RestException e) {
        return e.getResponse();
    }

    @ExceptionHandler(IllegalArgumentException.class) @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public Response illegalArgumentException(IllegalArgumentException e){
        return new Response.ResponseBuilder<>(e.getMessage(), 403).build();
    }

    @ExceptionHandler(UsernameNotFoundException.class) @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public Response illegalArgumentException(UsernameNotFoundException e){
        return new Response.ResponseBuilder<>(e.getMessage(), 403).build();
    }
}
