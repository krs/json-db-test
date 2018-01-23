package com.github.krs.jsondbtest;

@FunctionalInterface
public interface FileNamingStrategy {
    String toDataContainerName(String fileName);
}
