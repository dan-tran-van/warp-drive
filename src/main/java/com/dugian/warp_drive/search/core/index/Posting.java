package com.dugian.warp_drive.search.core.index;

import com.dugian.warp_drive.search.core.model.DocumentId;

import java.util.List;
import java.util.Objects;

public record Posting(DocumentId documentId, int termFrequency, List<Integer> positions) {
    public Posting {
        Objects.requireNonNull(documentId, "documentId must not be null");
        Objects.requireNonNull(positions, "positions must not be null");
        if (termFrequency < 1) {
            throw new IllegalArgumentException("termFrequency must be positive");
        }
        if (positions.size() != termFrequency) {
            throw new IllegalArgumentException("positions size must equal termFrequency");
        }
        positions = List.copyOf(positions);
    }
}
