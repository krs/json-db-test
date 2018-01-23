package com.github.krs.jsondbtest.internal.container;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListConverter<FROM, TO> implements ListConverter<FROM, TO> {

    protected abstract TO convertSingle(FROM from);

    @Override
    public List<TO> convert(@NonNull final List<FROM> source) {
        final List<TO> result = new ArrayList<>(source.size());
        for (FROM elem: source) {
            result.add(convertSingle(elem));
        }
        return result;
    }

}
