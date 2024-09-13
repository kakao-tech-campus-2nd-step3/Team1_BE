package com.example.team1_be.util.page;

import com.example.team1_be.util.errorException.BaseResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListResult<T> extends BaseResult {

    private final List<T> resultData = new ArrayList<>();

    public ListResult(Collection<T> initList) {
        if (initList != null) {
            resultData.addAll(initList);
        }
    }

    public void addAll(Collection<T> others) {
        resultData.addAll(others);
    }

    public void add(T element) {
        resultData.add(element);
    }

    public List<T> getResultData() {
        return resultData;
    }

    @Override
    public String toString() {
        return "ListResult{" +
            "resultData=" + resultData +
            '}';
    }
}