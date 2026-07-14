import java.util.List;

public final class MiniIndex {
    public record MiniPosting(String documentId, int termFrequency, List<Integer> positions) {
    }

    public void add(String documentId, List<String> tokens) {
        // TODO: replace the document's previous postings and group positions by token.
        throw new UnsupportedOperationException("Implement this exercise");
    }

    public List<MiniPosting> postings(String token) {
        // TODO: return postings for one token.
        throw new UnsupportedOperationException("Implement this exercise");
    }

    public int documentCount() {
        // TODO: return the number of unique document ids.
        throw new UnsupportedOperationException("Implement this exercise");
    }

    public int documentFrequency(String token) {
        // TODO: return the number of documents containing the token.
        throw new UnsupportedOperationException("Implement this exercise");
    }
}
