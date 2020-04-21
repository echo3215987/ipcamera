package com.compal.ipcamera.controller;

import com.compal.ipcamera.model.IpcameraCodeResultModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.compal.ipcamera.model.ResponseModel;
import com.compal.ipcamera.model.RequestIpcameraReplayModel;
import com.compal.ipcamera.model.IpcameraReplayResultModel;
import com.compal.ipcamera.service.IpcameraService;
import com.compal.ipcamera.error.IpcameraReplayErrorCode;
import com.compal.ipcamera.error.IpcameraCodeErrorCode;

@RestController
@RequestMapping(value = "/v1/api")
public class HikvisionController {
    private static Logger logger = LogManager.getLogger(HikvisionController.class);
    @Autowired
    private IpcameraService ipcameraService;

    @RequestMapping(value = "/ipcamera/replayurl/name", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> getIpcameraReplayURLByName(@RequestBody @Validated RequestIpcameraReplayModel requestIpcameraReplayModel) {
        ResponseModel responseModel = new ResponseModel();
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        HttpStatus statusCode = HttpStatus.OK;
        try {
            if (requestIpcameraReplayModel.getCameraName() == null && requestIpcameraReplayModel.getCameraName().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.CAMERA_NAME_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.CAMERA_NAME_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getBeginTime() == null && requestIpcameraReplayModel.getBeginTime().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.BEGIN_TIME_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.BEGIN_TIME_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getEndTime() == null && requestIpcameraReplayModel.getEndTime().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.END_TIME_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.END_TIME_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getExpand() == null && requestIpcameraReplayModel.getExpand().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.EXPAND_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.EXPAND_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            ipcameraReplayResultModel = ipcameraService.generateCameraPlaybackURLByName(requestIpcameraReplayModel.getCameraName(), requestIpcameraReplayModel.getBeginTime(), requestIpcameraReplayModel.getEndTime(), requestIpcameraReplayModel.getExpand());
            responseModel.setResult(ipcameraReplayResultModel);
            responseModel.setCode(IpcameraReplayErrorCode.SUCCESS.getCode());
            logger.info("Success: replayUrl: " + ipcameraReplayResultModel.getReplayUrl() );

        } catch (Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
            responseModel.setMessage(e.getMessage());
            responseModel.setCode(IpcameraReplayErrorCode.EXCEPTION_ERROR.getCode());
            logger.error("Error: replayUrl: " + ipcameraReplayResultModel.getReplayUrl());
        }
        return new ResponseEntity<ResponseModel>(responseModel, statusCode);
    }

    @RequestMapping(value = "/ipcamera/replayurl/code", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> getIpcameraReplayURLByCode(@RequestBody @Validated RequestIpcameraReplayModel requestIpcameraReplayModel) {
        ResponseModel responseModel = new ResponseModel();
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        HttpStatus statusCode = HttpStatus.OK;
        try {

            if (requestIpcameraReplayModel.getCameraIndexCode() == null && requestIpcameraReplayModel.getCameraIndexCode().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.CAMERA_INDEX_CODE_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.CAMERA_INDEX_CODE_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getBeginTime() == null && requestIpcameraReplayModel.getBeginTime().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.BEGIN_TIME_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.BEGIN_TIME_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getEndTime() == null && requestIpcameraReplayModel.getEndTime().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.END_TIME_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.END_TIME_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getExpand() == null && requestIpcameraReplayModel.getExpand().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.EXPAND_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.EXPAND_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            ipcameraReplayResultModel = ipcameraService.generateCameraPlaybackURLByCode(requestIpcameraReplayModel.getCameraIndexCode(), requestIpcameraReplayModel.getBeginTime(), requestIpcameraReplayModel.getEndTime(), requestIpcameraReplayModel.getExpand());
            responseModel.setResult(ipcameraReplayResultModel);
            responseModel.setCode(IpcameraReplayErrorCode.SUCCESS.getCode());
            logger.info("Success: replayUrl: " + ipcameraReplayResultModel.getReplayUrl() );

        } catch (Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
            responseModel.setMessage(e.getMessage());
            responseModel.setCode(IpcameraReplayErrorCode.EXCEPTION_ERROR.getCode());
            logger.error("Error: replayUrl: " + ipcameraReplayResultModel.getReplayUrl());
        }
        return new ResponseEntity<ResponseModel>(responseModel, statusCode);
    }

    @RequestMapping(value = "/ipcamera/previewurl/name", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> getIpcameraPreviewURLByName(@RequestBody @Validated RequestIpcameraReplayModel requestIpcameraReplayModel) {
        ResponseModel responseModel = new ResponseModel();
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        HttpStatus statusCode = HttpStatus.OK;
        try {
            if (requestIpcameraReplayModel.getCameraName() == null && requestIpcameraReplayModel.getCameraName().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.CAMERA_NAME_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.CAMERA_NAME_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            if (requestIpcameraReplayModel.getExpand() == null && requestIpcameraReplayModel.getExpand().equals("")) {
                statusCode = HttpStatus.NO_CONTENT;
                responseModel.setCode(IpcameraReplayErrorCode.EXPAND_IS_EMPTY.getCode());
                responseModel.setMessage(IpcameraReplayErrorCode.EXPAND_IS_EMPTY.getDescription());
                return new ResponseEntity<ResponseModel>(responseModel, statusCode);
            }

            ipcameraReplayResultModel = ipcameraService.generateCameraPreviewURLByName(requestIpcameraReplayModel.getCameraName(), requestIpcameraReplayModel.getExpand());
            responseModel.setResult(ipcameraReplayResultModel);
            responseModel.setCode(IpcameraReplayErrorCode.SUCCESS.getCode());
            logger.info("Success: replayUrl: " + ipcameraReplayResultModel.getReplayUrl() );

        } catch (Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
            responseModel.setMessage(e.getMessage());
            responseModel.setCode(IpcameraReplayErrorCode.EXCEPTION_ERROR.getCode());
            logger.error("Error: replayUrl: " + ipcameraReplayResultModel.getReplayUrl());
        }
        return new ResponseEntity<ResponseModel>(responseModel, statusCode);
    }

    @RequestMapping(value = "/ipcamera/code", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> getIpcameraCode() {
        ResponseModel responseModel = new ResponseModel();
        IpcameraCodeResultModel ipcameraCodeResultModel = new IpcameraCodeResultModel();
        HttpStatus statusCode = HttpStatus.OK;

        try {
            ipcameraCodeResultModel = ipcameraService.generateCameraCode();
            responseModel.setResult(ipcameraCodeResultModel);
            responseModel.setCode(IpcameraCodeErrorCode.SUCCESS.getCode());
            logger.info("Success: message: " + ipcameraCodeResultModel.getMessage() );

        } catch (Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
            responseModel.setMessage(e.getMessage());
            responseModel.setCode(IpcameraCodeErrorCode.EXCEPTION_ERROR.getCode());
            logger.error("Error: message: " + ipcameraCodeResultModel.getMessage());
        }
        return new ResponseEntity<ResponseModel>(responseModel, statusCode);
    }
}
