import java.util.List;

public final class MiniSearchEngineTest {
    private MiniSearchEngineTest() {
    }

    public static void main(String[] args) {
        MiniSearchEngine engine = new MiniSearchEngine();
        engine.add("doc-b", List.of("ab"));
        engine.add("doc-a", List.of("ab", "ab", "cd"));
        engine.add("doc-c", List.of("xy"));

        expectEquals(List.of("doc-a", "doc-b"), ids(engine.search(List.of("ab"), 10)),
                "term-frequency ranking");
        expectEquals(List.of("doc-a"), ids(engine.search(List.of("ab"), 1)), "limit handling");
        expectEquals(List.of(), engine.search(List.of("missing"), 10), "no-match query");
        expectEquals(List.of(), engine.search(List.of("ab"), 0), "non-positive limit");

        MiniSearchEngine ties = new MiniSearchEngine();
        ties.add("doc-b", List.of("xy"));
        ties.add("doc-a", List.of("xy"));
        expectEquals(List.of("doc-a", "doc-b"), ids(ties.search(List.of("xy"), 10)),
                "deterministic id tie-breaker");
        System.out.println("MiniSearchEngineTest passed");
    }

    private static List<String> ids(List<MiniSearchEngine.Result> results) {
        return results.stream().map(MiniSearchEngine.Result::documentId).toList();
    }

    private static void expectEquals(Object expected, Object actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
