package com.github.krs.jsondbtest.internal.container;

import java.util.List;

public interface ListConverter<FROM, TO> {
    List<TO> convert(List<FROM> source);
}
