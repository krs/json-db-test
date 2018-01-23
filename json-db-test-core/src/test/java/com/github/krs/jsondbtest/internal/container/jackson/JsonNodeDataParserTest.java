package com.github.krs.jsondbtest.internal.container.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.krs.jsondbtest.internal.container.jackson.JsonNodeDataParser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonNodeDataParserTest {
    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private static final String SINGLE_NODE_JSON = "{\"" + FIELD + "\":\"" + VALUE + "\"}";
    private static final String MULTI_NODE_JSON = "[{\"" + FIELD + "\":\"" + VALUE + "\"},{\"" + FIELD + "\":\"" + VALUE + "2\"}]";

    private JsonNodeDataParser parser;

    @BeforeEach
    public void setUp() {
        parser = new JsonNodeDataParser();
    }

    @Test
    public void testParseWithSingleNodeDocumentReturnsOneElementList() {
        assertResults(SINGLE_NODE_JSON, VALUE);
    }

    @Test
    public void testParseWithArrayDocumentReturnsList() {
        assertResults(MULTI_NODE_JSON, VALUE, VALUE + "2");
    }

    @Test
    public void testParseIncorrectInputThrowsException() {
        Assertions.assertThrows(RuntimeException.class, () -> parser.parse("{I'm not a valid JSON"));
    }

    private void assertResults(final String jsonString, final String... values) {
        final List<JsonNode> result = parser.parse(jsonString);
        assertThat(result).hasSize(values.length);
        for (int i = 0; i < values.length; ++i) {
            assertThat(result.get(i).get(FIELD).asText()).isEqualTo(values[i]);
        }
    }
}
