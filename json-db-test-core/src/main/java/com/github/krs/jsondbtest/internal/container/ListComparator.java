package com.github.krs.jsondbtest.internal.container;

import java.util.List;

public interface ListComparator<T> {
    ListComparisonResult<T> compare(final List<T> expected, final List<T> actual);
}
