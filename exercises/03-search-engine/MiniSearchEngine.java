import java.util.List;

public final class MiniSearchEngine {
    public record Result(String documentId, double score) {
    }

    public void add(String documentId, List<String> tokens) {
        // TODO: store the document's tokens.
        throw new UnsupportedOperationException("Implement this exercise");
    }

    public List<Result> search(List<String> queryTokens, int limit) {
        // TODO: score, sort deterministically, and apply the limit.
        throw new UnsupportedOperationException("Implement this exercise");
    }
}
