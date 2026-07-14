import java.util.List;

public final class MiniIndexTest {
    private MiniIndexTest() {
    }

    public static void main(String[] args) {
        MiniIndex index = new MiniIndex();
        index.add("doc-1", List.of("ab", "bc", "ab"));
        index.add("doc-2", List.of("ab"));

        expectEquals(2, index.documentCount(), "unique document count");
        expectEquals(2, index.documentFrequency("ab"), "document frequency");
        expectEquals(List.of(0, 2), index.postings("ab").get(0).positions(), "positions");
        expectEquals(2, index.postings("ab").get(0).termFrequency(), "term frequency");

        index.add("doc-1", List.of("xy"));
        expectEquals(0, index.documentFrequency("bc"), "replacement removes stale postings");
        expectEquals(1, index.documentFrequency("xy"), "replacement adds new postings");

        expectThrows(() -> index.postings("ab").add(null), "immutable postings result");
        System.out.println("MiniIndexTest passed");
    }

    private static void expectEquals(Object expected, Object actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void expectThrows(Runnable action, String name) {
        try {
            action.run();
            throw new AssertionError(name + ": expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // Success.
        }
    }
}
