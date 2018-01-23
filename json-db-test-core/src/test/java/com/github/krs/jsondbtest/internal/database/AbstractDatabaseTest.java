package com.github.krs.jsondbtest.internal.database;

import com.github.krs.jsondbtest.DataContainer;
import com.github.krs.jsondbtest.FileNamingStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AbstractDatabaseTest {
    private static final String FILE_1 = "a";
    private static final String FILE_2 = "b";
    private static final String FILE_3 = "c";

    private TestDatabase database;

    @Mock
    private FileNamingStrategy naming;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        database = new TestDatabase();
        database.setFileNamingStrategy(naming);

        when(naming.toDataContainerName(anyString())).then((InvocationOnMock i) -> container(i.getArguments()[0].toString()));
    }

    @Test
    public void testInsertCallsContainersBasedOnNamingStrategy() {
        database.insert(FILE_1, FILE_2, FILE_3, FILE_1);
        verifyContainerCalls(DataContainer::insert, 2, 1, 1);
    }

    @Test
    public void testResetCallsContainersBasedOnNamingStrategy() {
        database.reset(FILE_1, FILE_1, FILE_3, FILE_3, FILE_2);
        verifyContainerCalls(DataContainer::reset, 2, 1, 2);
    }

    @Test
    public void testExpectCallsContainersBasedOnNamingStrategy() {
        database.expect(FILE_1, FILE_2, FILE_3, FILE_3);
        verifyContainerCalls(DataContainer::expect, 1, 1, 2);
    }

    @Test
    public void textClearCallsContainersDirectly() {
        database.clear(FILE_1, FILE_2, FILE_2, FILE_3);
        final DataContainer mock1 = database.containers.get(FILE_1);
        final DataContainer mock2 = database.containers.get(FILE_2);
        final DataContainer mock3 = database.containers.get(FILE_3);

        verify(mock1).clear();
        verify(mock2, times(2)).clear();
        verify(mock3).clear();
        verifyNoMoreInteractions(mock1, mock2, mock3);
        assertThat(database.containers).hasSize(3);
    }

    private String container(final String file) {
        return file + "_container";
    }

    private DataContainer verifyMock(final String file, final int times) {
        return verify(getMock(file), times(times));
    }

    private DataContainer getMock(final String file) {
        return database.containers.get(container(file));
    }

    private void verifyContainerCalls(BiConsumer<DataContainer, String> call, int container1, int container2, int container3) {
        call.accept(verifyMock(FILE_1, container1), FILE_1);
        call.accept(verifyMock(FILE_2, container2), FILE_2);
        call.accept(verifyMock(FILE_3, container3), FILE_3);

        verifyNoMoreInteractions(getMock(FILE_1), getMock(FILE_2), getMock(FILE_3));
        assertThat(database.containers).hasSize(3);
    }

    private class TestDatabase extends AbstractDatabase {
        private Map<String, DataContainer> containers = new HashMap<>();

        @Override
        public DataContainer get(String name) {
            if (!containers.containsKey(name)) {
                containers.put(name, mock(DataContainer.class));
            }
            return containers.get(name);
        }

        @Override
        public void clear() {
            fail("Not implemented method called");
        }

        @Override
        public void expectEmpty() {
            fail("Not implemented method called");
        }

        @Override
        public void expectSize(int size) {
            fail("Not implemented method called");
        }

    }
}
