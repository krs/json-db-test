package com.github.krs.jsondbtest.internal.container;

import java.util.List;

public interface DataParser<T> {
    List<T> parse(String fileContents);
}
