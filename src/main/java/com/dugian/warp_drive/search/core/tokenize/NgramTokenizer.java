package com.dugian.warp_drive.search.core.tokenize;

import com.dugian.warp_drive.search.core.text.TextNormalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class NgramTokenizer implements Tokenizer {
    public static final int DEFAULT_NGRAM_SIZE = 2;

    private final int ngramSize;
    private final TextNormalizer normalizer;

    public NgramTokenizer() {
        this(DEFAULT_NGRAM_SIZE, new TextNormalizer());
    }

    public NgramTokenizer(int ngramSize) {
        this(ngramSize, new TextNormalizer());
    }

    public NgramTokenizer(int ngramSize, TextNormalizer normalizer) {
        if (ngramSize < 1) {
            throw new IllegalArgumentException("ngramSize must be at least 1");
        }
        this.ngramSize = ngramSize;
        this.normalizer = Objects.requireNonNull(normalizer, "normalizer must not be null");
    }

    public int ngramSize() {
        return ngramSize;
    }

    @Override
    public List<Token> tokenize(String text) {
        String normalized = normalizer.normalize(text);
        int[] codePoints = normalized.codePoints().toArray();
        if (codePoints.length < ngramSize) {
            return List.of();
        }

        List<Token> tokens = new ArrayList<>(codePoints.length - ngramSize + 1);
        for (int start = 0; start <= codePoints.length - ngramSize; start++) {
            String value = new String(codePoints, start, ngramSize);
            tokens.add(new Token(value, start));
        }
        return List.copyOf(tokens);
    }
}
