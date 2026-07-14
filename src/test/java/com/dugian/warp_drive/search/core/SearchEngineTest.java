package com.dugian.warp_drive.search.core;

import com.dugian.warp_drive.search.core.model.DocumentId;
import com.dugian.warp_drive.search.core.model.SearchDocument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchEngineTest {
    @Test
    void searchesMultilingualTextAndRanksRepeatedTermsHigher() {
        SearchEngine engine = new SearchEngine();
        engine.add(document("en", "Hello world"));
        engine.add(document("vi", "Xin chào thế giới"));
        engine.add(document("ja", "日本語の世界"));
        engine.add(document("mixed", "Hello 世界"));

        assertEquals("vi", engine.search("chào", 10).get(0).document().id().value());
        assertEquals("ja", engine.search("日本", 10).get(0).document().id().value());
        assertEquals("en", engine.search("hello", 10).get(0).document().id().value());
        assertEquals("ja", engine.search("世界", 10).get(0).document().id().value());
    }

    @Test
    void appliesDeterministicTieBreakingAndLimit() {
        SearchEngine engine = new SearchEngine();
        engine.add(document("b", "cat"));
        engine.add(document("a", "cat"));

        assertEquals(List.of("a", "b"), engine.search("cat", 2).stream()
                .map(result -> result.document().id().value()).toList());
        assertEquals(1, engine.search("cat", 1).size());
    }

    @Test
    void returnsEmptyForNoMatchAndBlankQuery() {
        SearchEngine engine = new SearchEngine();
        engine.add(document("a", "hello"));

        assertEquals(List.of(), engine.search("missing", 10));
        assertEquals(List.of(), engine.search("", 10));
    }

    private static SearchDocument document(String id, String text) {
        return new SearchDocument(new DocumentId(id), text, null, null, null);
    }
}
