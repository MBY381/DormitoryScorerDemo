package com.xjtu.dormitory.common.page;

import java.util.List;

@FunctionalInterface
public interface QueryAction<T> {
    public List<T> execute();
}
