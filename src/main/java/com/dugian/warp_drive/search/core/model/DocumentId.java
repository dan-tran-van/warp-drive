package com.dugian.warp_drive.search.core.model;

import java.util.Objects;

public record DocumentId(String value) implements Comparable<DocumentId> {
    public DocumentId {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    @Override
    public int compareTo(DocumentId other) {
        return value.compareTo(other.value);
    }
}
