package com.example.NextLevel.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String statusMessage;
    private final T data;

    public ApiResponse(
            final String statusMessage,
            final T data
    ) {
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(final T data) {
        return new ApiResponse<>(
                "성공",
                data
        );
    }

    // ← 에러 응답용 메서드 추가
    public static <T> ApiResponse<T> error(String statusMessage) {
        return new ApiResponse<>(statusMessage, null);
    }

}
