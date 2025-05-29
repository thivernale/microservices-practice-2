package org.thivernale.catalogservice.domain;

import java.util.List;

public record PagedResult<T>(
    List<T> data,
    long totalElements,
    long pageNumber,
    int totalPages,
    boolean isFirst,
    boolean isLast,
    boolean hasPrevious,
    boolean hasNext
) {
}
