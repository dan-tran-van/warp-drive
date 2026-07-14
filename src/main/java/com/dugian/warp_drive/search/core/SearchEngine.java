package com.dugian.warp_drive.search.core;

import com.dugian.warp_drive.search.core.index.InvertedIndex;
import com.dugian.warp_drive.search.core.model.SearchDocument;
import com.dugian.warp_drive.search.core.model.SearchResult;
import com.dugian.warp_drive.search.core.scoring.TermFrequencyScorer;
import com.dugian.warp_drive.search.core.tokenize.NgramTokenizer;
import com.dugian.warp_drive.search.core.tokenize.Token;
import com.dugian.warp_drive.search.core.tokenize.Tokenizer;

import java.util.Comparator;
import java.util.List;

public final class SearchEngine {
    private final InvertedIndex index;
    private final Tokenizer tokenizer;
    private final TermFrequencyScorer scorer;

    public SearchEngine() {
        this(new NgramTokenizer(), new InvertedIndex(), new TermFrequencyScorer());
    }

    public SearchEngine(Tokenizer tokenizer, InvertedIndex index, TermFrequencyScorer scorer) {
        this.tokenizer = tokenizer;
        this.index = index;
        this.scorer = scorer;
    }

    public void add(SearchDocument document) {
        index.add(document, tokenizer);
    }

    public List<SearchResult> search(String query, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        List<Token> queryTokens = tokenizer.tokenize(query);
        return scorer.score(queryTokens, index).entrySet().stream()
                .map(entry -> new SearchResult(index.document(entry.getKey()), entry.getValue()))
                .sorted(Comparator.comparingDouble(SearchResult::score).reversed()
                        .thenComparing(result -> result.document().id()))
                .limit(limit)
                .toList();
    }

    public int documentCount() {
        return index.documentCount();
    }

    public int tokenCount() {
        return index.tokenCount();
    }
}
