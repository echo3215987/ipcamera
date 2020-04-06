package com.compal.ipcamera.error;

public enum IpcameraCodeErrorCode {
    SUCCESS("200", "Success"),
    EXCEPTION_ERROR("99999", "Exception error");

    private final String description;
    private final String code;

    private IpcameraCodeErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    }

