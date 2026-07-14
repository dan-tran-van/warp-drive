# Warp Drive v0 Revised Implementation Plan

## 1. Updated project structure

Keep the existing Maven/Spring Boot layout and package root:

com.dugian.warp_drive
├── WarpDriveApplication.java
└── search
├── core # Pure Java; no Spring imports
│ ├── model
│ │ ├── DocumentId.java
│ │ └── SearchDocument.java
│ ├── text
│ │ └── TextNormalizer.java
│ ├── tokenize
│ │ ├── Token.java
│ │ └── NgramTokenizer.java
│ ├── index
│ │ ├── Posting.java
│ │ └── InvertedIndex.java
│ ├── scoring
│ │ ├── TermFrequencyScorer.java
│ │ └── TfidfScorer.java
│ └── SearchEngine.java
├── api # Spring adapter layer
│ ├── SearchApiService.java
│ ├── SearchConfiguration.java
│ └── dto
│ ├── IndexDocumentRequest.java
│ ├── SearchResultResponse.java
│ ├── SearchResponse.java
│ └── IndexStatsResponse.java
└── controller
└── SearchController.java

Core rules:

- search.core contains all normalization, tokenization, indexing, postings, statistics, scoring, and ranking.
- Core classes use only Java/JDK types.
- search.api and search.controller perform bean wiring, HTTP validation, DTO conversion, and delegation.
- Only SearchDocument.text is indexed in v0. authorName, language, and writingDirection are retained as metadata.
- Lexical JSON and HTML are explicitly outside the indexing path.

## 2. Updated exact file list

Create these production files:

src/main/java/com/dugian/warp_drive/search/core/model/DocumentId.java
src/main/java/com/dugian/warp_drive/search/core/model/SearchDocument.java
src/main/java/com/dugian/warp_drive/search/core/text/TextNormalizer.java
src/main/java/com/dugian/warp_drive/search/core/tokenize/Token.java
src/main/java/com/dugian/warp_drive/search/core/tokenize/NgramTokenizer.java
src/main/java/com/dugian/warp_drive/search/core/index/Posting.java
src/main/java/com/dugian/warp_drive/search/core/index/InvertedIndex.java
src/main/java/com/dugian/warp_drive/search/core/scoring/TermFrequencyScorer.java
src/main/java/com/dugian/warp_drive/search/core/scoring/TfidfScorer.java
src/main/java/com/dugian/warp_drive/search/core/SearchEngine.java
src/main/java/com/dugian/warp_drive/search/api/SearchApiService.java
src/main/java/com/dugian/warp_drive/search/api/SearchConfiguration.java
src/main/java/com/dugian/warp_drive/search/api/dto/IndexDocumentRequest.java
src/main/java/com/dugian/warp_drive/search/api/dto/SearchResultResponse.java
src/main/java/com/dugian/warp_drive/search/api/dto/SearchResponse.java
src/main/java/com/dugian/warp_drive/search/api/dto/IndexStatsResponse.java

Create these tests:

src/test/java/com/dugian/warp_drive/search/core/text/TextNormalizerTest.java
src/test/java/com/dugian/warp_drive/search/core/tokenize/NgramTokenizerTest.java
src/test/java/com/dugian/warp_drive/search/core/index/InvertedIndexTest.java
src/test/java/com/dugian/warp_drive/search/core/scoring/TermFrequencyScorerTest.java
src/test/java/com/dugian/warp_drive/search/core/scoring/TfidfScorerTest.java
src/test/java/com/dugian/warp_drive/search/core/SearchEngineTest.java
src/test/java/com/dugian/warp_drive/search/controller/SearchControllerTest.java

Modify, but do not replace, these existing files:

src/main/java/com/dugian/warp_drive/search/controller/SearchController.java
src/test/java/com/dugian/warp_drive/WarpDriveApplicationTests.java

Do not change pom.xml, application.properties, or compose.yaml in v0 planning unless an implementation issue makes the existing
setup impossible to test.

## 3. Updated build order

1. Define immutable core models:
   - DocumentId
   - SearchDocument
   - Token
   - Posting

2. Implement TextNormalizer:
   - Unicode normalization
   - Locale.ROOT lowercasing for Latin text
   - trim and whitespace collapsing
   - preserve Vietnamese diacritics and CJK characters

3. Implement configurable NgramTokenizer:
   - default ngram size 2
   - operate on Unicode code points, never raw char slicing
   - emit normalized token values and ordinal positions
   - reject invalid ngram sizes

4. Implement InvertedIndex:
   - token → postings
   - document id, term frequency, token positions
   - document frequency and document/token counts
   - first support add/index

5. Implement TermFrequencyScorer.
6. Implement SearchEngine and verify the first working in-memory search loop.
7. Add TfidfScorer, deterministic ordering, and limit handling.
8. Add replaceById only after add/search is stable; add removeById afterward.
9. Add Spring configuration, adapter service, DTOs, and:
   - POST /api/documents
   - GET /api/search
   - GET /api/index/stats

10. Add controller tests and run the full Maven test suite without introducing persistence code.

Scoring defaults:

- Term-frequency scoring is the first scorer.
- TF-IDF uses a simple smoothed IDF and deterministic ranking.
- Sort by score descending, then DocumentId ascending.
- No BM25, phrase/proximity logic, field weighting, or semantic scoring.

## 4. Spring/JPA/MySQL dependencies remaining unused in v0

Keep the current dependencies and configuration unchanged:

- JPA and Hibernate
- Flyway
- MySQL JDBC driver
- Docker Compose integration
- Lombok
- OpenAPI/Springdoc
- Spring validation

They remain build/runtime infrastructure but are not used by the search core. The core must not import any of them or any
Spring package.

Spring Web MVC remains used for the adapter endpoints. Existing datasource properties and compose.yaml may still cause Spring
context startup to initialize database infrastructure; this is existing project behavior, not part of the v0 search design.

## 5. Risks and decisions

- Ngrams are character/code-point ngrams with default size 2, matching the requested MySQL-style default.
- Token positions are ordinal positions in the emitted ngram sequence. Offsets are optional and should be omitted unless they
  can be added without complicating Unicode handling.

- Unicode normalization should preserve Vietnamese diacritics; do not strip accents.
- CJK text is preserved and tokenized without language-specific segmentation.
- DocumentId should be a small immutable value object around a string.
- SearchDocument stores optional metadata but indexes only text.
- Duplicate ids use replace semantics once replacement is implemented.
- Missing deletes should not be allowed to block the initial search loop; deletion is deferred until after add/search works.
- The singleton in-memory engine has process-local state and no persistence or restart recovery.
- Existing context tests may remain coupled to MySQL because current Spring configuration is intentionally retained.
- API request/response JSON shape should use DTOs and never expose Posting, InvertedIndex, or other core internals directly.

## 6. First implementation milestone only

### Milestone 1: Pure Java text-to-ranked-results loop

Implement only:

TextNormalizer.java
Token.java
NgramTokenizer.java
Posting.java
InvertedIndex.java
TermFrequencyScorer.java
SearchEngine.java

Success criteria:

- English, Vietnamese, Japanese/CJK, and mixed-language text can be normalized and tokenized.
- Default ngram size is 2; invalid sizes fail clearly.
- Repeated ngrams produce increasing term frequency and multiple positions.
- Documents can be added to an in-memory inverted index.
- Queries return matching documents ranked by term-frequency score.
- No-result queries return an empty result.
- Ties are resolved by ascending document id.
- Core tests cover blank text, short text, repeated tokens, diacritics, CJK, mixed scripts, and limit behavior.
- No Spring, database, controller, DTO, persistence, or API work is included in this milestone.

To continue this session, run codex resume 019f5baf-fcce-7943-a830-502f9a87e258
