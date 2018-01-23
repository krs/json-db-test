package com.github.krs.jsondbtest.naming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FolderPerDataStoreNamingStrategyTest {
    private FolderPerDataStoreNamingStrategy strategy;

    @BeforeEach
    public void setUp() {
        strategy = new FolderPerDataStoreNamingStrategy();
    }

    @Test
    public void testToDataContainerName() {
        assertResult("/books", "books");
        assertResult("/books.json", "books");
        assertResult("books.xml", "books");
        assertResult("/books/test1.json", "books");
        assertResult("/books/test/assert1.json", "books");
        assertResult("books/test/assert1.json", "books");
    }

    private void assertResult(final String input, final String expectedOutput) {
        assertThat(strategy.toDataContainerName(input)).isEqualTo(expectedOutput);
    }
}
