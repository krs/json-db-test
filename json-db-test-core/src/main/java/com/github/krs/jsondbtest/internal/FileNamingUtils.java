package com.github.krs.jsondbtest.internal;

import org.apache.commons.lang3.StringUtils;

public class FileNamingUtils {
    public static final String SLASH = "/";
    private static final String DOT = ".";

    public static boolean isInFolder(final String fileName) {
        return StringUtils.contains(fileName, SLASH);
    }

    public static String basename(final String fileName) {
        return StringUtils.substringBeforeLast(fileName, DOT);
    }

    public static String toResource(final String fileName) {
        if (StringUtils.startsWith(fileName, SLASH)) {
            return fileName;
        } else {
            return SLASH + fileName;
        }
    }
}
