package com.github.krs.jsondbtest.internal;

import com.github.krs.jsondbtest.internal.Assertions;
import com.github.krs.jsondbtest.internal.container.ListComparisonResult;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertionsTest {
    @Test
    public void testAssertSizeThrowsErrorWhenSizesDontMatch() {
        expectAssertionError(() -> Assertions.assertSize(1, 2), "Expected size 1, actual was 2");
    }

    @Test
    public void testAssertSizeDoesNothingWhenSizesMatch() {
        Assertions.assertSize(2, 2);
    }

    @Test
    public void testFailThrowsError() {
        final String message = RandomStringUtils.random(10);
        expectAssertionError(() -> Assertions.fail(message), message);
    }

    @Test
    public void testAssertNoDifferencesDoesNothingWhenNoDifferences() {
        Assertions.assertNoDifferences(new ListComparisonResult<>(1, 1));
    }

    @Test
    public void testAssertNoDifferencesThrowsErrorWhenThereAreDifferences() {
        final ListComparisonResult<String> comparison = new ListComparisonResult<>(1, 2);
        comparison.addMissingDocument("missing1");
        comparison.addMissingDocument("missing2");
        comparison.addUnexpectedDocument("unexpected1");
        comparison.addUnexpectedDocument("unexpected2");
        expectAssertionError(() -> Assertions.assertNoDifferences(comparison), "Database contains unexpected documents:\n"
                + "unexpected1\n"
                + "unexpected2\n"
                + "\nExpected documents not found in database:\n"
                + "missing1\n"
                + "missing2\n"
                + "\nExpected size 1, actual was 2");
    }

    private void expectAssertionError(Executable executable, String errorMessage) {
        final Throwable error = org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class, executable);
        assertThat(error.getMessage()).isEqualTo(errorMessage);
    }
}
