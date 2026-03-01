package com.cornedu.api.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private PageInfo page;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).build();
    }

    public static <T> ApiResponse<T> ok(T data, PageInfo page) {
        return ApiResponse.<T>builder().success(true).data(data).page(page).build();
    }

    public static ApiResponse<Void> ok(String message) {
        return ApiResponse.<Void>builder().success(true).message(message).build();
    }

    public static ApiResponse<Void> error(String message) {
        return ApiResponse.<Void>builder().success(false).message(message).build();
    }
}
