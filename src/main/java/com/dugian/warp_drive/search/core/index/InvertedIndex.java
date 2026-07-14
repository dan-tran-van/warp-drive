package com.dugian.warp_drive.search.core.index;

import com.dugian.warp_drive.search.core.model.DocumentId;
import com.dugian.warp_drive.search.core.model.SearchDocument;
import com.dugian.warp_drive.search.core.tokenize.Token;
import com.dugian.warp_drive.search.core.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class InvertedIndex {
    private final Map<DocumentId, SearchDocument> documents = new LinkedHashMap<>();
    private final Map<String, Map<DocumentId, Posting>> postingsByToken = new HashMap<>();

    public synchronized void add(SearchDocument document, List<Token> tokens) {
        Objects.requireNonNull(document, "document must not be null");
        Objects.requireNonNull(tokens, "tokens must not be null");
        remove(document.id());
        documents.put(document.id(), document);

        Map<String, List<Integer>> positionsByToken = new LinkedHashMap<>();
        for (Token token : tokens) {
            positionsByToken.computeIfAbsent(token.value(), ignored -> new ArrayList<>()).add(token.position());
        }
        for (Map.Entry<String, List<Integer>> entry : positionsByToken.entrySet()) {
            List<Integer> positions = List.copyOf(entry.getValue());
            postingsByToken.computeIfAbsent(entry.getKey(), ignored -> new HashMap<>())
                    .put(document.id(), new Posting(document.id(), positions.size(), positions));
        }
    }

    public void add(SearchDocument document, Tokenizer tokenizer) {
        Objects.requireNonNull(tokenizer, "tokenizer must not be null");
        add(document, tokenizer.tokenize(document.text()));
    }

    public void index(SearchDocument document, Tokenizer tokenizer) {
        add(document, tokenizer);
    }

    public synchronized void remove(DocumentId documentId) {
        if (documents.remove(documentId) == null) {
            return;
        }
        postingsByToken.values().removeIf(postings -> {
            postings.remove(documentId);
            return postings.isEmpty();
        });
    }

    public synchronized int documentCount() {
        return documents.size();
    }

    public synchronized int tokenCount() {
        return postingsByToken.size();
    }

    public synchronized int documentFrequency(String token) {
        Map<DocumentId, Posting> postings = postingsByToken.get(token);
        return postings == null ? 0 : postings.size();
    }

    public synchronized List<Posting> postings(String token) {
        Map<DocumentId, Posting> postings = postingsByToken.get(token);
        return postings == null ? List.of() : List.copyOf(postings.values());
    }

    public synchronized SearchDocument document(DocumentId documentId) {
        return documents.get(documentId);
    }
}
