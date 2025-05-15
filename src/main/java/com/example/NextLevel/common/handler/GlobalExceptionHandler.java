package com.example.NextLevel.common.handler;

import com.example.NextLevel.common.response.ApiResponse;
import com.example.NextLevel.domain.member.exception.MemberTaskException;
import com.example.NextLevel.domain.post.exception.PostTaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberTask(MemberTaskException ex) {
        ApiResponse<Void> body = ApiResponse.error(ex.getMessage());
        return ResponseEntity
                .status(ex.getCode())
                .body(body);
    }

    @ExceptionHandler(PostTaskException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberTask(PostTaskException ex) {
        ApiResponse<Void> body = ApiResponse.error(ex.getMessage());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(body);
    }
}
