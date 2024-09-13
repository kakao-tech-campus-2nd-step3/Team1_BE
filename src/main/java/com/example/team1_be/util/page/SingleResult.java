package com.example.team1_be.util.page;

import com.example.team1_be.util.errorException.BaseResult;

public class SingleResult<T> extends BaseResult {

    private final T resultData;

    public SingleResult(T value) {
        this.resultData = value;
    }

    public T getResultData() {
        return resultData;
    }
}