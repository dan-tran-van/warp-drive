package com.dugian.warp_drive.search.core.tokenize;

import java.util.Objects;

public record Token(String value, int position) {
    public Token {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("value must not be empty");
        }
        if (position < 0) {
            throw new IllegalArgumentException("position must not be negative");
        }
    }
}
