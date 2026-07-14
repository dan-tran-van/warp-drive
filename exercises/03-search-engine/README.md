# Search engine exercise

Implement `MiniSearchEngine` in `MiniSearchEngine.java`.

Requirements:

- Add documents represented by token lists.
- For every query token, add matching document term frequency to its score.
- Exclude documents with no matching query tokens.
- Sort by score descending, then document id ascending.
- Return at most `limit` results.
- Return an empty list for `limit <= 0` or no matches.

Example: `doc-a = [ab, ab, cd]`, `doc-b = [ab]`, query `[ab]` ranks `doc-a` before `doc-b`.

Run the explicit criteria test with:

```bash
javac MiniSearchEngine.java MiniSearchEngineTest.java && java MiniSearchEngineTest
```
