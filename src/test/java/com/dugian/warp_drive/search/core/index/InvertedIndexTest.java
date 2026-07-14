package com.dugian.warp_drive.search.core.index;

import com.dugian.warp_drive.search.core.model.DocumentId;
import com.dugian.warp_drive.search.core.model.SearchDocument;
import com.dugian.warp_drive.search.core.tokenize.NgramTokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvertedIndexTest {
    @Test
    void storesDocumentsPostingsFrequenciesPositionsAndCounts() {
        InvertedIndex index = new InvertedIndex();
        SearchDocument document = new SearchDocument(new DocumentId("doc-1"), "aaaa", null, "en", null);
        index.add(document, new NgramTokenizer());

        Posting posting = index.postings("aa").get(0);
        assertEquals(document.id(), posting.documentId());
        assertEquals(3, posting.termFrequency());
        assertEquals(java.util.List.of(0, 1, 2), posting.positions());
        assertEquals(1, index.documentCount());
        assertEquals(1, index.tokenCount());
        assertEquals(1, index.documentFrequency("aa"));
    }

    @Test
    void replacingAnIdDoesNotLeaveStalePostings() {
        InvertedIndex index = new InvertedIndex();
        DocumentId id = new DocumentId("doc-1");
        index.add(new SearchDocument(id, "hello", null, null, null), new NgramTokenizer());
        index.add(new SearchDocument(id, "world", null, null, null), new NgramTokenizer());

        assertEquals(0, index.documentFrequency("he"));
        assertEquals(1, index.documentFrequency("wo"));
    }
}
