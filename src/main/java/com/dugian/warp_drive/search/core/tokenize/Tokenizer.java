package com.dugian.warp_drive.search.core.tokenize;

import java.util.List;

public interface Tokenizer {
    List<Token> tokenize(String text);
}
