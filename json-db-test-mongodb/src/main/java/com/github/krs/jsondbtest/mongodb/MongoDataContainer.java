package com.github.krs.jsondbtest.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.krs.jsondbtest.internal.container.AbstractDataContainer;
import com.github.krs.jsondbtest.internal.container.DataParser;
import com.github.krs.jsondbtest.internal.container.ListComparator;
import com.github.krs.jsondbtest.internal.container.ListConverter;
import com.github.krs.jsondbtest.internal.container.jackson.JsonNodeDataParser;
import com.github.krs.jsondbtest.internal.container.jackson.JsonNodeListComparator;
import com.github.krs.jsondbtest.internal.mongodb.converter.Bson2JsonNodeConverter;
import com.github.krs.jsondbtest.internal.mongodb.converter.JsonNode2BsonConverter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import lombok.AccessLevel;
import lombok.Setter;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Setter(AccessLevel.PACKAGE)
public class MongoDataContainer extends AbstractDataContainer<JsonNode> {
    private final MongoCollection<Document> collection;

    private ListComparator<JsonNode> comparator;
    private DataParser<JsonNode> parser;
    private ListConverter<Document, JsonNode> bson2JsonNodeConverter;
    private ListConverter<JsonNode, Document> jsonNode2BsonConverter;

    public MongoDataContainer(final MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public void clear() {
        collection.deleteMany(new BasicDBObject());
    }

    @Override
    protected int getSize() {
        return (int) collection.count();
    }

    @Override
    protected void insert(final List<JsonNode> jsonDocs) {
        collection.insertMany(getJsonNode2BsonConverter().convert(jsonDocs));
    }

    @Override
    protected List<JsonNode> getData() {
        final List<Document> mongoDocs = new ArrayList<>(getSize());
        collection.find().into(mongoDocs);
        return getBson2JsonNodeConverter().convert(mongoDocs);
    }

    @Override
    protected ListComparator<JsonNode> getComparator() {
        initialize(comparator, this::setComparator, JsonNodeListComparator::new);
        return comparator;
    }

    @Override
    protected DataParser<JsonNode> getDataParser() {
        initialize(parser, this::setParser, JsonNodeDataParser::new);
        return parser;
    }

    private ListConverter<JsonNode, Document> getJsonNode2BsonConverter() {
        initialize(jsonNode2BsonConverter, this::setJsonNode2BsonConverter, JsonNode2BsonConverter::new);
        return jsonNode2BsonConverter;
    }

    private ListConverter<Document, JsonNode> getBson2JsonNodeConverter() {
        initialize(bson2JsonNodeConverter, this::setBson2JsonNodeConverter, Bson2JsonNodeConverter::new);
        return bson2JsonNodeConverter;
    }

    private <T> void initialize(final T value, Consumer<T> setter, Supplier<T> producer) {
        if (value == null) {
            setter.accept(producer.get());
        }
    }

}
