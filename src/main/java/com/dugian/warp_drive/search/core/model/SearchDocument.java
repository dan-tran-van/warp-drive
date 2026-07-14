package com.dugian.warp_drive.search.core.model;

import java.util.Objects;

public record SearchDocument(
        DocumentId id,
        String text,
        String authorName,
        String language,
        String writingDirection) {

    public SearchDocument {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(text, "text must not be null");
    }
}
