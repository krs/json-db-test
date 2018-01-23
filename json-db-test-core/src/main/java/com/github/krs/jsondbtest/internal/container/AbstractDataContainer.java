package com.github.krs.jsondbtest.internal.container;

import com.github.krs.jsondbtest.DataContainer;
import com.github.krs.jsondbtest.internal.Assertions;
import com.github.krs.jsondbtest.internal.FileNamingUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractDataContainer<T> implements DataContainer {

    protected abstract int getSize();

    protected abstract void insert(final List<T> parsedDocs);

    protected abstract List<T> getData();

    protected abstract ListComparator<T> getComparator();

    protected abstract DataParser<T> getDataParser();

    @Override
    public void insert(final String... files) {
        doWithFiles(files, (String file) -> insert(readFile(file)));
    }

    @Override
    public void expect(final String... files) {
        final List<T> expected = new LinkedList<>();
        doWithFiles(files, (String file) -> expected.addAll(readFile(file)));
        final List<T> actual = getData();
        Assertions.assertNoDifferences(getComparator().compare(actual, expected));
    }

    @Override
    public void reset(final String... files) {
        clear();
        insert(files);
    }

    @Override
    public void expectEmpty() {
        expectSize(0);
    }

    @Override
    public void expectSize(final int size) {
        Assertions.assertSize(size, getSize());
    }

    private void doWithFiles(final String[] files, final Consumer<String> action) {
        if (files != null) {
            for (String file: files) {
                action.accept(file);
            }
        }
    }

    private List<T> readFile(final String file) {
        return getDataParser().parse(readContents(file));
    }

    private String readContents(final String file) {
        try {
            return IOUtils.toString(getClass().getResourceAsStream(FileNamingUtils.toResource(file)), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
