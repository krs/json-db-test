package com.github.krs.jsondbtest.internal;

import com.github.krs.jsondbtest.internal.container.ListComparisonResult;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class Assertions {

    public static void assertSize(final int expected, final int actual) {
        if (actual != expected) {
            fail(createSizeMessage(expected, actual));
        }
    }

    public static void fail(final String reason) {
        throw new AssertionError(reason);
    }

    public static void assertNoDifferences(final ListComparisonResult<?> comparison) {
        if (comparison.hasDifferences()) {
            final StringBuilder message = new StringBuilder();
            appendDocuments(message, "Database contains unexpected documents:", comparison.getUnexpectedDocuments());
            appendDocuments(message, "Expected documents not found in database:", comparison.getMissingDocuments());
            message.append(createSizeMessage(comparison.getExpectedSize(), comparison.getActualSize()));

            fail(message.toString());
        }
    }

    private static String createSizeMessage(final int expected, final int actual) {
        return "Expected size " + expected + ", actual was " + actual;
    }

    private static void appendDocuments(final StringBuilder message, final String label, final List<?> docs) {
        if (CollectionUtils.isNotEmpty(docs)) {
            message.append(label).append("\n");
            docs.stream().forEach(doc -> appendDocument(message, doc));
            message.append("\n");
        }
    }

    private static <T> void appendDocument(final StringBuilder message, final T doc) {
        message.append(doc.toString()).append("\n"); //TODO use object writer here
    }
}
