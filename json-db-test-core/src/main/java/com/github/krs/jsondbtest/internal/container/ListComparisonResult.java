package com.github.krs.jsondbtest.internal.container;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ListComparisonResult<T> {
    private final int expectedSize;
    private final int actualSize;
    private List<T> unexpectedDocuments = new LinkedList<>();
    private List<T> missingDocuments = new LinkedList<>();

    public boolean hasDifferences() {
        return CollectionUtils.isNotEmpty(unexpectedDocuments) || CollectionUtils.isNotEmpty(missingDocuments);
    }

    public void addUnexpectedDocument(final T document) {
        unexpectedDocuments.add(document);
    }

    public void addMissingDocument(final T document) {
        missingDocuments.add(document);
    }

}
