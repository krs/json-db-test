package com.github.krs.jsondbtest.internal.container.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.krs.jsondbtest.internal.container.DataParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonNodeDataParser implements DataParser<JsonNode> {

    @Override
    public List<JsonNode> parse(final String fileContents) {
        try {
            final JsonNode root = ObjectMapperHolder.get().readTree(fileContents);
            if (root.isArray()) {
                return asJsonNodeList(root);
            } else {
                return Arrays.asList(root);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<JsonNode> asJsonNodeList(final JsonNode root) {
        final List<JsonNode> result = new ArrayList<>(root.size());
        root.elements().forEachRemaining(result::add);
        return result;
    }

}
