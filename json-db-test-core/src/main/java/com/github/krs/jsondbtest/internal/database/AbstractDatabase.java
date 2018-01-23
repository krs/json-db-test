package com.github.krs.jsondbtest.internal.database;

import java.util.function.BiConsumer;

import com.github.krs.jsondbtest.DataContainer;
import com.github.krs.jsondbtest.Database;
import com.github.krs.jsondbtest.FileNamingStrategy;
import com.github.krs.jsondbtest.naming.FolderPerDataStoreNamingStrategy;

import lombok.NonNull;

public abstract class AbstractDatabase implements Database {
    private FileNamingStrategy namingStrategy = new FolderPerDataStoreNamingStrategy();

    @Override
    public void insert(final String... files) {
        doWithFiles(DataContainer::insert, files);
    }

    @Override
    public void reset(final String... files) {
        doWithFiles(DataContainer::reset, files);
    }

    @Override
    public void expect(final String... files) {
        doWithFiles(DataContainer::expect, files);
    }

    @Override
    public void clear(final String... stores) {
        if (stores != null) {
            for (String store: stores) {
                get(store).clear();
            }
        }
    }

    @Override
    public void setFileNamingStrategy(@NonNull final FileNamingStrategy strategy) {
        this.namingStrategy = strategy;
    }

    private void doWithFiles(final BiConsumer<DataContainer, String> operation, final String... files) {
        if (files != null) {
            for (String file: files) {
                String dataStoreName = namingStrategy.toDataContainerName(file);
                operation.accept(get(dataStoreName), file);
            }
        }
    }

}
