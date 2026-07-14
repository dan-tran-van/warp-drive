import java.util.List;

public final class MiniNgramsTest {
    private MiniNgramsTest() {
    }

    public static void main(String[] args) {
        expectEquals(List.of("he", "el", "ll", "lo"), MiniNgrams.ngrams("hello", 2), "English ngrams");
        expectEquals(List.of("日本", "本語"), MiniNgrams.ngrams("日本語", 2), "CJK code-point ngrams");
        expectEquals(List.of("abc", "bcd"), MiniNgrams.ngrams("abcd", 3), "configurable size");
        expectEquals(List.of(), MiniNgrams.ngrams("hi", 3), "short input");
        expectThrows(() -> MiniNgrams.ngrams("abc", 0), "invalid size");
        System.out.println("MiniNgramsTest passed");
    }

    private static void expectEquals(Object expected, Object actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void expectThrows(Runnable action, String name) {
        try {
            action.run();
            throw new AssertionError(name + ": expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }
}
