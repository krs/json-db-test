package com.github.krs.jsondbtest;

public interface DataContainer {
    void insert(String... files);

    void reset(String... files);

    void clear();

    void expect(String... files);

    void expectEmpty();

    void expectSize(int size);

    //DataContainer ignoring(String fields);
    //DataContainer replacing(String search, String replace);
}
