package team1.be.seamless.util.page;

import team1.be.seamless.util.errorException.BaseResult;

public class SingleResult<T> extends BaseResult {

    private final T resultData;

    public SingleResult(T value) {
        this.resultData = value;
    }

    public T getResultData() {
        return resultData;
    }
}