package com.dugian.warp_drive.search.core.tokenize;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NgramTokenizerTest {
    @Test
    void tokenizesEnglishWithDefaultSizeTwo() {
        assertEquals(List.of("he", "el", "ll", "lo"),
                new NgramTokenizer().tokenize("Hello").stream().map(Token::value).toList());
    }

    @Test
    void tokenizesVietnameseAndCjkByCodePoint() {
        assertEquals(List.of("ch", "hà", "ào"),
                new NgramTokenizer().tokenize("Chào").stream().map(Token::value).toList());
        assertEquals(List.of("日本", "本語"),
                new NgramTokenizer().tokenize("日本語").stream().map(Token::value).toList());
    }

    @Test
    void supportsMixedScriptsAndConfigurableSize() {
        assertEquals(List.of("a界", "界b"),
                new NgramTokenizer(2).tokenize("a界b").stream().map(Token::value).toList());
        assertEquals(List.of("abc", "bcd"),
                new NgramTokenizer(3).tokenize("abcd").stream().map(Token::value).toList());
    }

    @Test
    void handlesBlankAndShortText() {
        assertEquals(List.of(), new NgramTokenizer().tokenize(" "));
        assertEquals(List.of(), new NgramTokenizer(3).tokenize("hi"));
    }

    @Test
    void rejectsInvalidNgramSize() {
        assertThrows(IllegalArgumentException.class, () -> new NgramTokenizer(0));
    }

    @Test
    void emitsOrdinalPositionsAndRepeatedTokens() {
        List<Token> tokens = new NgramTokenizer().tokenize("aaaa");
        assertEquals(List.of(0, 1, 2), tokens.stream().map(Token::position).toList());
        assertEquals(List.of("aa", "aa", "aa"), tokens.stream().map(Token::value).toList());
    }
}
