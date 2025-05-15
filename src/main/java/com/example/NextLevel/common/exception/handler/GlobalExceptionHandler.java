package com.example.NextLevel.common.exception.handler;

import com.example.NextLevel.common.exception.CustomException;
import com.example.NextLevel.common.exception.ErrorCode;
import com.example.NextLevel.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MemberTaskException.class)
//    public ResponseEntity<ApiResponse<Void>> handleMemberTask(MemberTaskException ex) {
//        ApiResponse<Void> body = ApiResponse.error(ex.getMessage());
//        return ResponseEntity
//                .status(ex.getCode())
//                .body(body);
//    }
//
//    @ExceptionHandler(PostTaskException.class)
//    public ResponseEntity<ApiResponse<Void>> handleMemberTask(PostTaskException ex) {
//        ApiResponse<Void> body = ApiResponse.error(ex.getMessage());
//        return ResponseEntity
//                .status(ex.getStatusCode())
//                .body(body);
//    }

    // 비즈니스 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustom(CustomException ex) {
        ErrorCode ec = ex.getErrorCode();
        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiResponse.error(ec));
    }
}
