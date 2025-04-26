package io.foodapp.server.dtos.Filter;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageFilter {
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String order = "asc";

    public static Pageable toPageAble(PageFilter filter) {
        return PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(
                        Sort.Direction.fromString(filter.getOrder()), // "asc" or "desc"
                        filter.getSortBy()                            // ví dụ: "code", "value", "startDate"
                )
        );
    }
}
