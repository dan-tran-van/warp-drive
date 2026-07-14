package com.dugian.warp_drive.search.core.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextNormalizerTest {
    private final TextNormalizer normalizer = new TextNormalizer();

    @Test
    void normalizesCaseWhitespaceAndUnicode() {
        assertEquals("café xin chào 世界", normalizer.normalize("  CAFÉ   XIN   CHÀO  世界 "));
    }

    @Test
    void blankTextBecomesEmpty() {
        assertEquals("", normalizer.normalize(" \t\n "));
    }
}
