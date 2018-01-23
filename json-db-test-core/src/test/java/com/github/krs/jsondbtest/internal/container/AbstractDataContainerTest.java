package com.github.krs.jsondbtest.internal.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractDataContainerTest {
    private static final String[] TEST_FILES = {"testfile1", "testfile2"};
    private static final String[] TEST_FILE_CONTENTS = {"I'm from testfile1.", "I'm a text in testfile2."};

    private TestDataContainer container;

    private ListComparisonResult<String> result;

    @Mock
    private DataParser<String> parser;

    @Mock
    private ListComparator<String> comparator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        container = new TestDataContainer();
        result = new ListComparisonResult<>(0, 0);

        when(parser.parse(anyString())).thenAnswer((InvocationOnMock invocation) -> Arrays.asList(invocation.getArguments()[0]));
        when(comparator.compare(any(), any())).thenReturn(result);
    }

    @Test
    public void testInsertReadsParsesAndInsertsAllFiles() {
        testInsertion(container::insert);
    }

    @Test
    public void testInsertThrowsExceptionWhenInsertedFileCannotBeRead() {
        testWithIncorrectFile(container::insert);
    }

    @Test
    public void testResetCleansAndInsertsAllFiles() {
        container.data.add("Element to be cleared");
        testInsertion(container::reset);
    }

    @Test
    public void testResetThrowsExceptionWhenInsertedFileCannotBeRead() {
        testWithIncorrectFile(container::reset);
    }

    @Test
    public void testExpectReadsAndComparesAllFiles() {
        container.expect(TEST_FILES);
        verify(comparator).compare(same(container.data), eq(Arrays.asList(TEST_FILE_CONTENTS)));
    }

    @Test
    public void testExpectThrowsErrorWhenComparatorReturnsDifferences() {
        result.addMissingDocument("missing");
        expectAssertError(() -> container.expect(TEST_FILES));
    }

    @Test
    public void testExpectThrowsExceptionWhenExpectedFileCannotBeRead() {
        testWithIncorrectFile(container::expect);
    }

    @Test
    public void testExpectSizeThrowsErrorWhenSizesDontMatch() {
        expectAssertError(() -> container.expectSize(1));
    }

    @Test
    public void testExpectEmptyDoesNothingWhenSizesMatch() {
        container.data.add("single element");
        container.expectSize(1);
    }

    @Test
    public void testExpectEmptyThrowsErrornWhenSizeNotZero() {
        container.data.add("single element");
        expectAssertError(() -> container.expectEmpty());
    }

    @Test
    public void testExpectEmptyDoesNothingWhenSizeIsZero() {
        container.expectEmpty();
    }

    private void testInsertion(final Consumer<String[]> methodToTest) {
        methodToTest.accept(TEST_FILES);

        Stream.of(TEST_FILE_CONTENTS).forEach(s -> verify(parser).parse(s));
        assertThat(container.data).containsExactly(TEST_FILE_CONTENTS);
    }

    private void testWithIncorrectFile(final Consumer<String[]> methodToTest) {
        Assertions.assertThrows(RuntimeException.class, () -> methodToTest.accept(new String[] {"This is invalid filename"}));
    }

    private void expectAssertError(final Executable methodCall) {
        Assertions.assertThrows(AssertionError.class, methodCall);
    }

    private class TestDataContainer extends AbstractDataContainer<String> {
        private List<String> data = new ArrayList<>();

        @Override
        public void clear() {
            data.clear();
        }

        @Override
        protected int getSize() {
            return data.size();
        }

        @Override
        protected void insert(List<String> parsedDocs) {
            data.addAll(parsedDocs);
        }

        @Override
        protected List<String> getData() {
            return data;
        }

        @Override
        protected ListComparator<String> getComparator() {
            return comparator;
        }

        @Override
        protected DataParser<String> getDataParser() {
            return parser;
        }

    }
}
