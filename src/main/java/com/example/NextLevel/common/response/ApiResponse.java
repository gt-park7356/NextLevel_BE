package com.example.NextLevel.common.response;

import com.example.NextLevel.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code    = code;
        this.message = message;
        this.data    = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "OK", "성공", data);
    }

    public static <T> ApiResponse<T> error(ErrorCode ec) {
        return new ApiResponse<>(false, ec.getCode(), ec.getMessage(), null);
    }

}
