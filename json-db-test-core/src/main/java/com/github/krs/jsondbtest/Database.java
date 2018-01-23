package com.github.krs.jsondbtest;

public interface Database extends DataContainer {
    DataContainer get(String name);

    void clear(String... containers);

    void setFileNamingStrategy(FileNamingStrategy strategy);
}
