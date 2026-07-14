package com.dugian.warp_drive.search.core.text;

import java.text.Normalizer;
import java.util.Locale;

public final class TextNormalizer {
    public String normalize(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String unicodeNormalized = Normalizer.normalize(text, Normalizer.Form.NFKC);
        return unicodeNormalized.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
    }
}
