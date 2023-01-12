package com.project.feeely.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@ToString
public class Response <T> implements Serializable {

    private static final long serialVersionUID = 3876786389768673L;

    private final String message; // -> OK, FAIL, ERROR -> ERROR MESSAGE

    private final HttpStatus status;

    private final int total; // result가 1개면 1, 실패 했을 경우 0으로 보내면 페이징 처리할 때 if문 가를 수 있대

    private final T response; // 내가 보낼 값을 response로 주는 것

    public static class ResponseBuilder<T> {

        private final String message;  // 결과 메시지
        private final int code;
        private int total;          // 총 응답 데이터 수
        private T response; // 응답 데이터

        private HttpStatus status;

        private void setStatus(int code){
            try {
                status = HttpStatus.valueOf(code);
            } catch (IllegalArgumentException ignored) { // 매개변수가 잘못들어온걸 알려주는거임
                status = HttpStatus.MULTI_STATUS;
            };
        }

        public ResponseBuilder(String message, int code) {
            this.message = message;
            this.code = code;
            setStatus(code);
        }

        public ResponseBuilder<T> resultResponse(T value) {
            response = value;
            return this;
        }

        public ResponseBuilder<T> total(int value) {
            total = value;
            return this; // T 때문에 this로 받음..
        }
        public Response<T> build() {
            return new Response<>(this);
        }
    }


    private Response(ResponseBuilder<T> responseBuilder){
        message = responseBuilder.message;
        status = responseBuilder.status;
        total = responseBuilder.total;
        response = responseBuilder.response;
    }
}
