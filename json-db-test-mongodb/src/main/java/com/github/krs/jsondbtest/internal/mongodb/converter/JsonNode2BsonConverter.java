package com.github.krs.jsondbtest.internal.mongodb.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.krs.jsondbtest.internal.container.AbstractListConverter;
import com.github.krs.jsondbtest.internal.container.jackson.ObjectMapperHolder;

import org.bson.Document;

import java.util.Map;

public class JsonNode2BsonConverter extends AbstractListConverter<JsonNode, Document> {

    @SuppressWarnings("unchecked")
    @Override
    public Document convertSingle(final JsonNode json) {
        return new Document(ObjectMapperHolder.get().convertValue(json, Map.class));
    }
}
