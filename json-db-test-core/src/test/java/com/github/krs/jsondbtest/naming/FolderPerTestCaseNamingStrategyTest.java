package com.github.krs.jsondbtest.naming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FolderPerTestCaseNamingStrategyTest {
    private FolderPerTestCaseNamingStrategy strategy;

    @BeforeEach
    public void setUp() {
        strategy = new FolderPerTestCaseNamingStrategy();
    }

    @Test
    public void testToDataContainerName() {
        assertResult("/books", "books");
        assertResult("/books.json", "books");
        assertResult("books.xml", "books");
        assertResult("/test1/books.json", "books");
        assertResult("/test1/test2/books.json", "books");
        assertResult("test1/test/books.xml", "books");
    }

    private void assertResult(final String input, final String expectedOutput) {
        assertThat(strategy.toDataContainerName(input)).isEqualTo(expectedOutput);
    }
}
