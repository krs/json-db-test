package com.github.krs.jsondbtest.naming;

import com.github.krs.jsondbtest.FileNamingStrategy;

import org.apache.commons.lang3.StringUtils;

import static com.github.krs.jsondbtest.internal.FileNamingUtils.SLASH;

/**
 * {@link FileNamingStrategy} where data files for one data container (table, collection) are kept in one folder.
 * <br>If file name is not placed in a folder it will be treated as store name (only without extension).
 * <br><br>Example:
 * <pre>
 * authors/test-case-1/insert.json -> authors
 * authors/test-case-1/assert.json -> authors
 * authors/test-case-2.xml -> authors
 * authors.json -> authors
 * /books/test-case-1.json -> books
 * /books/test-case-2.json -> books
 * </pre>
 */
public class FolderPerDataStoreNamingStrategy extends AbstractFileNamingStrategy {

    @Override
    protected String removeFolderName(String relativeFileName) {
        return StringUtils.substringBefore(relativeFileName, SLASH);
    }
}
