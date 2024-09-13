package com.example.team1_be.util.errorException;

import java.io.Serializable;

public class BaseResult implements Serializable {

    public Integer errorCode = 200;
    public String errorMessage = "Success";

    public BaseResult() {
    }

    public BaseResult(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
