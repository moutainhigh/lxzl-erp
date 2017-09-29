package com.lxzl.erp.common.domain;

/**
 * User : LiuKe
 * Date : 2015/4/29
 * Time : 17:18
 */
public class ServiceResult<String, RESULT> {

    public ServiceResult(){}

    public ServiceResult(String errorCode, RESULT result) {
        this.errorCode = errorCode;
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public RESULT getResult() {
        return result;
    }

    public void setResult(RESULT result) {
        this.result = result;
    }

    private String errorCode;

    private RESULT result;
}
