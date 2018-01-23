package com.github.krs.jsondbtest.internal.mongodb.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.krs.jsondbtest.internal.container.AbstractListConverter;
import com.github.krs.jsondbtest.internal.container.jackson.ObjectMapperHolder;

import org.bson.Document;

import java.io.IOException;

public class Bson2JsonNodeConverter extends AbstractListConverter<Document, JsonNode> {

    @Override
    protected JsonNode convertSingle(final Document from) {
        try {
            return ObjectMapperHolder.get().readTree(from.toJson());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
