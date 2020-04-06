package com.compal.ipcamera.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class CameraList {
//    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private String cameraName;
    private String cameraIndexCode;
    private Timestamp createTime;
    private Timestamp updateTime;

    protected CameraList() {}

    public CameraList(String cameraIndexCode, String cameraName) {
        this.cameraIndexCode = cameraIndexCode;
        this.cameraName = cameraName;
    }

    public CameraList(String cameraIndexCode, String cameraName, Timestamp createTime, Timestamp updateTime) {
        this.cameraIndexCode = cameraIndexCode;
        this.cameraName = cameraName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getCameraIndexCode() {
        return cameraIndexCode;
    }

    public void setCameraIndexCode(String cameraIndexCode) {
        this.cameraIndexCode = cameraIndexCode;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}
