package com.github.krs.jsondbtest.naming;

import com.github.krs.jsondbtest.FileNamingStrategy;

import org.apache.commons.lang3.StringUtils;

import static com.github.krs.jsondbtest.internal.FileNamingUtils.*;

public abstract class AbstractFileNamingStrategy implements FileNamingStrategy {

    @Override
    public String toDataContainerName(final String fileName) {
        final String relativeFileName = StringUtils.removeStart(fileName, SLASH);
        if (isInFolder(relativeFileName)) {
            return removeFolderName(relativeFileName);
        }
        return basename(relativeFileName);
    }

    protected abstract String removeFolderName(String relativeFileName);
}
