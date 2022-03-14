
package com.example.myokhttp;

public class JsonRootBean {

    private Data data;
    private int errorCode;
    private String errorMsg;
    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "JsonRootBean{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}