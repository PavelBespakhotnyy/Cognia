package com.cognia.backend.api;

import java.util.List;

public record ApiListResponse<T>(List<T> data, PageMeta meta) {

    public static <T> ApiListResponse<T> of(List<T> data, int page, int pageSize, long total) {
        return new ApiListResponse<>(data, new PageMeta(page, pageSize, total));
    }
}
