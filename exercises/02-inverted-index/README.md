# Inverted index exercise

Implement `MiniIndex` in `MiniIndex.java`.

Requirements:

- Index each document's token list in memory.
- For `['ab', 'bc', 'ab']`, the `ab` posting has frequency `2` and positions `[0, 2]`.
- `documentCount()` counts unique document ids.
- `documentFrequency(token)` counts documents containing the token.
- Adding an existing document id replaces its old postings.
- Return immutable posting and position lists.
- Do not add deletion or synchronization yet.

Run the explicit criteria test with:

```bash
javac MiniIndex.java MiniIndexTest.java && java MiniIndexTest
```
