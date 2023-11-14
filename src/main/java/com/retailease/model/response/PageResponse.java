package com.retailease.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageResponse {
    private Integer currentPage;
    private Integer totalPage;
    private Integer size;

}
