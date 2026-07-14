# Ngram tokenizer exercise

Implement `MiniNgrams.ngrams` in `MiniNgrams.java`.

Requirements:

- Reject `size < 1` with `IllegalArgumentException`.
- Return an empty list when the input has fewer code points than `size`.
- Use Unicode code points, not `substring`.
- `"hello"`, size `2` → `he`, `el`, `ll`, `lo`.
- `"日本語"`, size `2` → `日本`, `本語`.
- Do not normalize or lowercase in this exercise.

Try the implementation before looking at the production tokenizer.

Run the explicit criteria test with:

```bash
javac MiniNgrams.java MiniNgramsTest.java && java MiniNgramsTest
```
