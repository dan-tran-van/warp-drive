package com.dugian.warp_drive.search.core.scoring;

import com.dugian.warp_drive.search.core.index.InvertedIndex;
import com.dugian.warp_drive.search.core.model.DocumentId;
import com.dugian.warp_drive.search.core.model.SearchDocument;
import com.dugian.warp_drive.search.core.tokenize.NgramTokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TermFrequencyScorerTest {
    @Test
    void repeatedTermGetsHigherScore() {
        InvertedIndex index = new InvertedIndex();
        DocumentId repeated = new DocumentId("repeated");
        DocumentId once = new DocumentId("once");
        NgramTokenizer tokenizer = new NgramTokenizer();
        index.add(new SearchDocument(repeated, "aaaa", null, null, null), tokenizer);
        index.add(new SearchDocument(once, "aa", null, null, null), tokenizer);

        var scores = new TermFrequencyScorer().score(tokenizer.tokenize("aa"), index);
        assertTrue(scores.get(repeated) > scores.get(once));
    }
}
