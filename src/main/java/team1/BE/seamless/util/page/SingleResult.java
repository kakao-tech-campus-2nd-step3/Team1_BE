package team1.BE.seamless.util.page;

import team1.BE.seamless.util.errorException.BaseResult;

public class SingleResult<T> extends BaseResult {

    private final T resultData;

    public SingleResult(T value) {
        this.resultData = value;
    }

    public T getResultData() {
        return resultData;
    }
}