package com.cognia.backend.api;

import java.util.List;
import org.springframework.http.HttpStatus;

public final class Pagination {

    private Pagination() {
    }

    public static <T> ApiListResponse<T> of(List<T> items, int page, int pageSize) {
        if (page < 1 || pageSize < 1 || pageSize > 100) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "VALIDATION_ERROR",
                    "page must be at least 1 and pageSize must be between 1 and 100.");
        }

        long offset = (long) (page - 1) * pageSize;
        int from = (int) Math.min(offset, items.size());
        int to = Math.min(from + pageSize, items.size());
        return ApiListResponse.of(items.subList(from, to), page, pageSize, items.size());
    }
}
