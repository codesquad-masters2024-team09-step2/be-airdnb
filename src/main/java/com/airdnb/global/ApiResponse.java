package com.airdnb.global;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private ApiResult result;
    private T data;
    private String errorMessage;

    private ApiResponse(ApiResult result, T data) {
        this.result = result;
        this.data = data;
        this.errorMessage = null;
    }

    private ApiResponse(ApiResult result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiResult.SUCCESS, data);
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>(ApiResult.ERROR, errorMessage);
    }

    private enum ApiResult {
        SUCCESS, ERROR
    }
}
