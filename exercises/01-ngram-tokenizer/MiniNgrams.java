import java.util.List;
import java.util.stream.IntStream;

public final class MiniNgrams {
    private MiniNgrams() {
    }

    public static List<String> ngrams(String input, int size) {
        // TODO: validate size, iterate over Unicode code points, and build windows.
        if (size < 1) {
            throw new IllegalArgumentException("Size must be at least 1");
        }
        IntStream codePointStream = input.codePoints();

        codePointStream.forEach(codePoint -> System.out.println("code point " + Character.toString(codePoint)));
        return List.of("", "");

        // throw new UnsupportedOperationException("Implement this exercise");
    }
}
