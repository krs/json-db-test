package com.github.krs.jsondbtest.internal.mongodb.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Bson2JsonNodeConverterTest {
    private static final String STRING_FIELD = "string";
    private static final String STRING_VALUE = "string value";

    private static final String INT_FIELD = "int";
    private static final Integer INT_VALUE = -12;

    private static final String SHORT_FIELD = "short";
    private static final Integer SHORT_VALUE = 42;

    private static final String LONG_FIELD = "looong";
    private static final long LONG_VALUE = Long.MAX_VALUE;

    private static final String BOOLEAN_FIELD = "bool";
    private static final boolean BOOLEAN_VALUE = true;

    private static final String DOUBLE_FIELD = "double";
    private static final double DOUBLE_VALUE = Double.MIN_VALUE;

    private Bson2JsonNodeConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new Bson2JsonNodeConverter();
    }

    @Test
    public void testConvertNullThrowsException() {
        Assertions.assertThrows(NullPointerException.class, () -> converter.convert(null));
    }

    @Test
    public void testConvertEmptyListReturnsEmptyList() {
        assertThat(converter.convert(new ArrayList<>())).isEmpty();
    }

    @Test
    public void testConvertReturnsConvertedValues() {
        final List<Document> docs = Arrays.asList(createBsonDocument(), createBsonDocument());
        final List<JsonNode> result = converter.convert(docs);

        assertThat(result).hasSize(docs.size());
        assertJsonNodeHasSameDataAsInput(result.get(0));
        assertJsonNodeHasSameDataAsInput(result.get(1));
    }

    private void assertJsonNodeHasSameDataAsInput(final JsonNode jsonNode) {
        System.out.print(jsonNode.toString());
        assertThat(jsonNode.get(STRING_FIELD).asText()).isEqualTo(STRING_VALUE);
        assertThat(jsonNode.get(INT_FIELD).asInt()).isEqualTo(INT_VALUE);
        assertThat(jsonNode.get(BOOLEAN_FIELD).asBoolean()).isEqualTo(BOOLEAN_VALUE);
        assertThat(jsonNode.get(LONG_FIELD).asLong()).isEqualTo(LONG_VALUE);
        assertThat(jsonNode.get(DOUBLE_FIELD).asLong()).isEqualTo(DOUBLE_VALUE);
        assertThat(jsonNode.get(SHORT_FIELD).asLong()).isEqualTo(SHORT_VALUE);
    }

    private Document createBsonDocument() {
        // TODO check BsonTypeClassMap to see how various bson objects are converted

        final BasicDBObject result = createSimpleBsonObject();
        result.put("date", new Date(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()));
        result.put("id", ObjectId.get());
        result.put("object", createSimpleBsonObject());

        final BasicDBList listOfStrings = new BasicDBList();
        listOfStrings.add("string1");
        listOfStrings.add("string1");
        result.put("listOfStrings", listOfStrings);

        final BasicDBList listOfObjects = new BasicDBList();
        listOfObjects.add(createSimpleBsonObject());
        listOfObjects.add(createSimpleBsonObject());
        result.put("listOfObjects", listOfObjects);

        return new Document(result);
    }

    private BasicDBObject createSimpleBsonObject() {
        final BasicDBObject result = new BasicDBObject();
        result.put(STRING_FIELD, STRING_VALUE);
        result.put(BOOLEAN_FIELD, BOOLEAN_VALUE);
        result.put(INT_FIELD, INT_VALUE);
        result.put(LONG_FIELD, LONG_VALUE);
        result.put(DOUBLE_FIELD, DOUBLE_VALUE);
        result.put(SHORT_FIELD, SHORT_VALUE);
        return result;
    }

}
