package com.compal.ipcamera.model;

public class ResponseModel {
    private String message;
    private String code;
    private Object result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
