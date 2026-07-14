package com.dugian.warp_drive.search.core.scoring;

import com.dugian.warp_drive.search.core.index.InvertedIndex;
import com.dugian.warp_drive.search.core.index.Posting;
import com.dugian.warp_drive.search.core.model.DocumentId;
import com.dugian.warp_drive.search.core.tokenize.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TermFrequencyScorer {
    public Map<DocumentId, Double> score(List<Token> queryTokens, InvertedIndex index) {
        Map<DocumentId, Double> scores = new HashMap<>();
        for (Token queryToken : queryTokens) {
            for (Posting posting : index.postings(queryToken.value())) {
                scores.merge(posting.documentId(), (double) posting.termFrequency(), Double::sum);
            }
        }
        return scores;
    }
}
