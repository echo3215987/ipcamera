package com.compal.ipcamera.error;

public enum IpcameraReplayErrorCode {
    SUCCESS("200", "Success"),
    CAMERA_NAME_IS_EMPTY("10001", "cameraName is empty"),
    CAMERA_INDEX_CODE_IS_EMPTY("10002", "cameraIndexCode is empty"),
    BEGIN_TIME_IS_EMPTY("10003", "beginTime is empty"),
    END_TIME_IS_EMPTY("10004", "endTime is empty"),
    EXCEPTION_ERROR("99999", "Exception error");

    private final String description;
    private final String code;

    private IpcameraReplayErrorCode(String code, String description) {
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

