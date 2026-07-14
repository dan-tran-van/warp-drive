package com.dugian.warp_drive.search.core.model;

import java.util.Objects;

public record SearchResult(SearchDocument document, double score) {
    public SearchResult {
        Objects.requireNonNull(document, "document must not be null");
    }
}
