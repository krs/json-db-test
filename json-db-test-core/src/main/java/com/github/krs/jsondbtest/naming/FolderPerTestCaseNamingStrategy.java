package com.github.krs.jsondbtest.naming;

import com.github.krs.jsondbtest.FileNamingStrategy;

import org.apache.commons.lang3.StringUtils;

import static com.github.krs.jsondbtest.internal.FileNamingUtils.SLASH;
import static com.github.krs.jsondbtest.internal.FileNamingUtils.basename;

/**
 * {@link FileNamingStrategy} where data files for one test case are kept in one folder.
 * <br>If file name is not placed in a folder it will be treated as store name (only without extension).
 * <br>Example:
 * <pre>
 * test-case-1/authors.json
 * test-case-1/books.json
 * test-case-1/assert/authors.json
 * test-case-2/authors.json
 * test-case-2/books.json
 * test-case-2/assert/books.json
 * authors.json
 * </pre>
 */
public class FolderPerTestCaseNamingStrategy extends AbstractFileNamingStrategy {

    @Override
    protected String removeFolderName(String relativeFileName) {
        return basename(StringUtils.substringAfterLast(relativeFileName, SLASH));
    }
}
