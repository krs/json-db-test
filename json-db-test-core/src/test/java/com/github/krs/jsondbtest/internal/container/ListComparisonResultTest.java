package com.github.krs.jsondbtest.internal.container;

import com.github.krs.jsondbtest.internal.container.ListComparisonResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListComparisonResultTest {
    private ListComparisonResult<String> result;

    @BeforeEach
    public void setUp() {
        result = new ListComparisonResult<>(1, 2);
    }

    @Test
    public void testHasDifferencesReturnsTrueWhenThereAreMissingDocuments() {
        result.addMissingDocument("a");
        assertThat(result.hasDifferences()).isTrue();
    }

    @Test
    public void testHasDifferencesReturnsTrueWhenThereAreUnexpectedDocuments() {
        result.addUnexpectedDocument("a");
        assertThat(result.hasDifferences()).isTrue();
    }
}
