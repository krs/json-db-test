package com.github.krs.jsondbtest.mongodb;

import com.github.krs.jsondbtest.DataContainer;
import com.github.krs.jsondbtest.internal.Assertions;
import com.github.krs.jsondbtest.internal.database.AbstractDatabase;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MongoDb extends AbstractDatabase {
    private final MongoClient mongoClient;
    private final String database;

    public MongoDb(final MongoClient mongoClient, final String database) {
        this.mongoClient = mongoClient;
        this.database = database;
    }

    @Override
    public DataContainer get(final String name) {
        return new MongoDataContainer(getDatabase().getCollection(name));
    }

    @Override
    public void clear() {
        doWithCollections(DataContainer::clear);
    }

    @Override
    public void expectEmpty() {
        doWithCollections(DataContainer::expectEmpty);
    }

    @Override
    public void expectSize(final int size) {
        Assertions.assertSize(size, getCollectionNames().size());
    }

    private MongoDatabase getDatabase() {
        return mongoClient.getDatabase(database);
    }

    private Set<String> getCollectionNames() {
        final Set<String> names = new HashSet<>();
        getDatabase().listCollectionNames().into(names);
        return names;
    }

    private void doWithCollections(Consumer<DataContainer> consumer) {
        getCollectionNames().stream().map(this::get).peek(consumer);
    }
}
