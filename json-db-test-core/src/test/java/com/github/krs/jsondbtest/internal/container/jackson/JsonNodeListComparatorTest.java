package com.github.krs.jsondbtest.internal.container.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.krs.jsondbtest.internal.container.ListComparisonResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonNodeListComparatorTest {

    private JsonNodeListComparator comparator;
    private List<JsonNode> expected;
    private List<JsonNode> actual;

    @BeforeEach
    public void setUp() {
        comparator = new JsonNodeListComparator();

        expected = new ArrayList<>();
        actual = new ArrayList<>();
    }

    @Test
    public void testCompareAddsUnexpectedDocumentsToResult() {
        expectDocumentInResult(actual, ListComparisonResult::getUnexpectedDocuments);
    }

    @Test
    public void testCompareAddsMissingDocumentsToResult() {
        expectDocumentInResult(expected, ListComparisonResult::getMissingDocuments);
    }

    @Test
    public void testCompareReturnsNoDifferencesOnlyWhenListsMatch() {
        assertThat(compare().hasDifferences()).isFalse();

        expected.add(node());
        assertThat(compare().hasDifferences()).isTrue();

        actual.add(node());
        assertThat(compare().hasDifferences()).isFalse();

        actual.add(node());
        assertThat(compare().hasDifferences()).isTrue();
    }

    @Test
    public void testCompareAddsUnexpectedDocumentToResultWhenExpectedDocAppearsTwice() {
        expected.add(node());
        actual.add(node());
        expectDocumentInResult(actual, ListComparisonResult::getUnexpectedDocuments);
    }

    @Test
    public void testComparePutsInputSizesInResult() {
        expected.add(node());
        actual.add(node());
        actual.add(node());

        final ListComparisonResult<JsonNode> result = compare();
        assertThat(result.getActualSize()).isEqualTo(2);
        assertThat(result.getExpectedSize()).isEqualTo(1);
    }

    @Test
    public void testCompareWithNullInputs() {
        expectNullPointerException("actual", () -> comparator.compare(expected, null));
        expectNullPointerException("expected", () -> comparator.compare(null, actual));
    }

    private void expectDocumentInResult(final List<JsonNode> input, final Function<ListComparisonResult<JsonNode>, List<JsonNode>> resultList) {
        final JsonNode node = node();
        input.add(node);
        final ListComparisonResult<JsonNode> result = compare();
        assertThat(resultList.apply(result)).containsOnly(node);
    }

    private ListComparisonResult<JsonNode> compare() {
        return comparator.compare(expected, actual);
    }

    private void expectNullPointerException(final String message, final Executable call) {
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, call);
        assertThat(e.getMessage()).isEqualTo(message);
    }

    private JsonNode node() {
        return new TextNode("node");
    }
}
