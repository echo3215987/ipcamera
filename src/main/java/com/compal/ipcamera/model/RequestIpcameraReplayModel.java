package com.compal.ipcamera.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RequestIpcameraReplayModel {
    private String cameraName;
    private String beginTime;
    private String endTime;
    private String cameraIndexCode;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCameraIndexCode() {
        return cameraIndexCode;
    }

    public void setCameraIndexCode(String cameraIndexCode) {
        this.cameraIndexCode = cameraIndexCode;
    }
}
