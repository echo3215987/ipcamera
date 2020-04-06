package com.compal.ipcamera.repository;

import java.util.List;

import com.compal.ipcamera.entity.CameraList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraListRepository extends CrudRepository<CameraList, String> {
    CameraList findByCameraName(String cameraName);
}