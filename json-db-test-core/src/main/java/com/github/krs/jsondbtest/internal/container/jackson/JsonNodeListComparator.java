package com.github.krs.jsondbtest.internal.container.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.krs.jsondbtest.internal.container.ListComparator;
import com.github.krs.jsondbtest.internal.container.ListComparisonResult;

import lombok.NonNull;

import java.util.List;

public class JsonNodeListComparator implements ListComparator<JsonNode> {

    @Override
    public ListComparisonResult<JsonNode> compare(@NonNull final List<JsonNode> expected, @NonNull final List<JsonNode> actual) {
        final ListComparisonResult<JsonNode> result = new ListComparisonResult<>(expected.size(), actual.size());

        final List<JsonNode> expectedNotFound = expected; // FIXME this modifies input
        for (JsonNode node: actual) {
            if (expectedNotFound.contains(node)) {
                expectedNotFound.remove(node);
            } else {
                result.addUnexpectedDocument(node);
            }
        }
        expectedNotFound.forEach(result::addMissingDocument);
        return result;
    }

}
