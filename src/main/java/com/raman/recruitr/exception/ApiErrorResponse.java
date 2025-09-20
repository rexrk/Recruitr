package com.raman.recruitr.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ApiErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private final String status;
    private final String message;

    public static ApiErrorResponse of(org.springframework.http.HttpStatus httpStatus, String message) {
        return new ApiErrorResponse(httpStatus.name(), message);
    }
}
