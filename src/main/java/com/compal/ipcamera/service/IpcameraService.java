package com.compal.ipcamera.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.compal.ipcamera.model.IpcameraReplayResultModel;
import com.compal.ipcamera.model.IpcameraCodeResultModel;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import com.compal.ipcamera.repository.CameraListRepository;
import com.compal.ipcamera.entity.CameraList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class IpcameraService {
    private static final Logger logger = LoggerFactory.getLogger(IpcameraService.class);

    @Autowired
    private CameraListRepository cameraListRepository;

    /* 海康威視API */
    // artemis网关服务器ip端口
    @Value("${hikvision.arttemisconfig.host}")
    private String ARTTEMISCONFIG_HOST;
    // 秘钥appkey
    @Value("${hikvision.arttemisconfig.appkey}")
    private String ARTTEMISCONFIG_APPKEY;
    // 秘钥appSecret
    @Value("${hikvision.arttemisconfig.appsecret}")
    private String ARTTEMISCONFIG_APPSECRET;

    // artemis API Path
    static final String ARTEMIS_PATH = "/artemis";
    //  artemis API content type
    static final String contentType = "application/json";
    static final DateTimeFormatter isoFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public IpcameraReplayResultModel generateCameraPlaybackURLByName(String cameraName, String beginTime, String endTime, String expand) {
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        String indexCode = null;
        try{
            // fetch an individual customer by ID
            CameraList cameraList = cameraListRepository.findByCameraName(cameraName);
            if (cameraList == null){
                ipcameraReplayResultModel.setMessage("camera name was not found");
                return ipcameraReplayResultModel;
            }
            indexCode  = cameraList.getCameraIndexCode();
            String[] replayResponse = GetCameraPlaybackURL(indexCode, beginTime, endTime, expand);
            String replayUrl = replayResponse[0];
            String beginTimeRes = replayResponse[1];
            String endTimeRes = replayResponse[2];
            String size = replayResponse[3];
            String message = replayResponse[4];
            ipcameraReplayResultModel.setReplayUrl(replayUrl);
            ipcameraReplayResultModel.setBeginTime(beginTimeRes);
            ipcameraReplayResultModel.setEndTime(endTimeRes);
            ipcameraReplayResultModel.setSize(size);
            ipcameraReplayResultModel.setMessage(message);
        }catch(Exception e){
            logger.error(e.getMessage());
            ipcameraReplayResultModel.setMessage(e.getMessage());
        }
        return ipcameraReplayResultModel;
    }

    public IpcameraReplayResultModel generateCameraPlaybackURLByCode(String cameraIndexCode, String beginTime, String endTime, String expand) {
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        try{
            String[] replayResponse = GetCameraPlaybackURL(cameraIndexCode, beginTime, endTime, expand);
            String replayUrl = replayResponse[0];
            String beginTimeRes = replayResponse[1];
            String endTimeRes = replayResponse[2];
            String size = replayResponse[3];
            String message = replayResponse[4];
            ipcameraReplayResultModel.setReplayUrl(replayUrl);
            ipcameraReplayResultModel.setBeginTime(beginTimeRes);
            ipcameraReplayResultModel.setEndTime(endTimeRes);
            ipcameraReplayResultModel.setSize(size);
            ipcameraReplayResultModel.setMessage(message);
        }catch(Exception e){
            logger.error(e.getMessage());
            ipcameraReplayResultModel.setMessage(e.getMessage());
        }
        return ipcameraReplayResultModel;
    }

    public IpcameraReplayResultModel generateCameraPreviewURLByName(String cameraName, String expand) {
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        String indexCode = null;
        try{
            // fetch an individual customer by ID
            CameraList cameraList = cameraListRepository.findByCameraName(cameraName);
            if (cameraList == null){
                ipcameraReplayResultModel.setMessage("camera name was not found");
                return ipcameraReplayResultModel;
            }
            indexCode  = cameraList.getCameraIndexCode();
            String[] replayResponse = GetCameraPreviewURL(indexCode, expand);
            String replayUrl = replayResponse[0];
            String message = replayResponse[1];
            ipcameraReplayResultModel.setReplayUrl(replayUrl);
            ipcameraReplayResultModel.setMessage(message);
        }catch(Exception e){
            logger.error(e.getMessage());
            ipcameraReplayResultModel.setMessage(e.getMessage());
        }
        return ipcameraReplayResultModel;
    }

    public IpcameraCodeResultModel generateCameraCode() {
        IpcameraCodeResultModel ipcameraCodeResultModel = new IpcameraCodeResultModel();
        try{
            String message = GetCameraCode();
            ipcameraCodeResultModel.setMessage(message);
        }catch(Exception e){
            logger.error(e.getMessage());
            ipcameraCodeResultModel.setMessage(e.getMessage());
        }
        return ipcameraCodeResultModel;
    }

    public String[] GetCameraPlaybackURL(String cameraIndexCode, String beginTime, String endTime, String expand) {
        String url = null;
        String beginTimeRes = null;
        String endTimeRes = null;
        String size = null;

        //设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
        ArtemisConfig.host = ARTTEMISCONFIG_HOST;
        ArtemisConfig.appKey = ARTTEMISCONFIG_APPKEY;
        ArtemisConfig.appSecret = ARTTEMISCONFIG_APPSECRET;

        final String playbackURLsApi = ARTEMIS_PATH + "/api/video/v1/cameras/playbackURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", playbackURLsApi);//根据现场环境部署确认是http还是https
            }
        };
        logger.info(playbackURLsApi);
        //组装请求参数
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("streamType", 0);
        jsonBody.put("protocol", "rtsp");
        jsonBody.put("transmode", 1);
//        jsonBody.put("expand", "streamform=rtp");
        jsonBody.put("beginTime", beginTime);
        jsonBody.put("endTime", endTime);
        if(expand != null)
            jsonBody.put("expand", expand);
        String body = jsonBody.toJSONString();
       //调用接口
        logger.info(body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
        JSONObject resultObject = JSON.parseObject(result);
        String message = resultObject.getString("msg");
        JSONObject data = resultObject.getJSONObject("data");
        if(data != null){
            url = data.getString("url");
            JSONArray jsonArray = data.getJSONArray("list");
            for(int i = 0; i<jsonArray.size(); i++) {
                JSONObject replayObj = jsonArray.getJSONObject(i);
                beginTimeRes = replayObj.getString("beginTime");
                endTimeRes = replayObj.getString("endTime");
                size = replayObj.getString("size");
            }
        }
        return new String[]{url, beginTimeRes, endTimeRes, size, message};
    }

    public String GetCameraCode() {
        //设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
        ArtemisConfig.host = ARTTEMISCONFIG_HOST;
        ArtemisConfig.appKey = ARTTEMISCONFIG_APPKEY;
        ArtemisConfig.appSecret = ARTTEMISCONFIG_APPSECRET;

        final String camerasApi = ARTEMIS_PATH + "/api/resource/v1/cameras";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", camerasApi);
            }
        };
        logger.info(camerasApi);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 1000);

        String body = jsonBody.toJSONString();
        //调用接口
        logger.info(body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
        JSONObject resultObject = JSON.parseObject(result);
        String message = resultObject.getString("msg");
        JSONObject data = resultObject.getJSONObject("data");
        JSONArray jsonArray = new JSONArray();
        if(data != null)
            jsonArray = data.getJSONArray("list");
        for(int i = 0; i<jsonArray.size(); i++){
            JSONObject cameraObj = jsonArray.getJSONObject(i);
            String cameraIndexCode = cameraObj.getString("cameraIndexCode");
            String cameraName = cameraObj.getString("cameraName");
            String createTime = cameraObj.getString("createTime");
            String encodeDevIndexCode = cameraObj.getString("encodeDevIndexCode");
            String regionIndexCode = cameraObj.getString("regionIndexCode");
            String updateTime = cameraObj.getString("updateTime");
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            DateTime createdt = format.parseDateTime(createTime)
                    .withZone(DateTimeZone.forOffsetHours(8));
            DateTime updatedt = format.parseDateTime(updateTime)
                    .withZone(DateTimeZone.forOffsetHours(8));
//                DateTime createdt = new DateTime();
//                DateTime updatedt = createdt.minusMinutes(5);
//                CameraList cameraList = new CameraList("7ccadbece9f64ae0950943ae886fac83", "N01-3F 02L F68-12 78.58 R11.12", new Timestamp(createdt.getMillis()), new Timestamp(updatedt.getMillis()));
            CameraList cameraList = new CameraList(cameraIndexCode, cameraName, new Timestamp(createdt.getMillis()), new Timestamp(updatedt.getMillis()));
            cameraList = cameraListRepository.save(cameraList);
        }
        return message;
    }

    public String[] GetCameraPreviewURL(String cameraIndexCode, String expand) {

        String url = null;

        //设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
        ArtemisConfig.host = ARTTEMISCONFIG_HOST;
        ArtemisConfig.appKey = ARTTEMISCONFIG_APPKEY;
        ArtemisConfig.appSecret = ARTTEMISCONFIG_APPSECRET;

        final String previewURLsApi = ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };
        System.out.println(previewURLsApi);

        logger.info(previewURLsApi);
        //组装请求参数
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("streamType", 0);
        jsonBody.put("protocol", "rtsp");
        jsonBody.put("transmode", 1);
        if(expand != null)
            jsonBody.put("expand", expand);
        String body = jsonBody.toJSONString();
        //调用接口
        logger.info(body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
        JSONObject resultObject = JSON.parseObject(result);
        String message = resultObject.getString("msg");
        JSONObject data = resultObject.getJSONObject("data");
        if(data != null)
            url = data.getString("url");
        return new String[]{url, message};
    }
}


