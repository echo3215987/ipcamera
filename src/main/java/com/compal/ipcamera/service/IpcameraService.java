package com.compal.ipcamera.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.compal.ipcamera.model.IpcameraReplayResultModel;
import com.compal.ipcamera.model.IpcameraCodeResultModel;
import com.compal.ipcamera.model.ResponseModel;
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
import java.util.UUID;
import com.compal.ipcamera.repository.CameraListRepository;
import com.compal.ipcamera.entity.CameraList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
//import static org.apache.logging.log4j.core.util.NameUtil.md5;

@Service
@Transactional
public class IpcameraService {
    private static final Logger logger = LoggerFactory.getLogger(IpcameraService.class);

    @Autowired
    private CameraListRepository cameraListRepository;

    /* 海康威視API */
    // artemis网关服务器ip端口
//    private static String ARTTEMISCONFIG_HOST = "10.142.81.21:443";
    @Value("${hikvision.arttemisconfig.host}")
    private String ARTTEMISCONFIG_HOST;
    // 秘钥appkey
//    private static String ARTTEMISCONFIG_APPKEY = "25398502";
    @Value("${hikvision.arttemisconfig.appkey}")
    private String ARTTEMISCONFIG_APPKEY;
    // 秘钥appSecret
//    private static String ARTTEMISCONFIG_APPSECRET = "xLU6R5mEblVD2d3Ej47w";
    @Value("${hikvision.arttemisconfig.appsecret}")
    private String ARTTEMISCONFIG_APPSECRET;

    // artemis API Path
    static final String ARTEMIS_PATH = "/artemis";
    //  artemis API content type
    static final String contentType = "application/json";
    static final DateTimeFormatter isoFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public IpcameraReplayResultModel generateCameraPlaybackURLByName(String cameraName, String beginTime, String endTime) {
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();
        String indexCode = null;
        try{
            // fetch an individual customer by ID
            CameraList cameraList = cameraListRepository.findByCameraName(cameraName);
            indexCode  = cameraList.getCameraIndexCode();
        }catch(Exception e){
            logger.error(e.getMessage());
        }

        String replayUrl = GetCameraPlaybackURL(indexCode, beginTime, endTime);
        ipcameraReplayResultModel.setReplayUrl(replayUrl);
        return ipcameraReplayResultModel;
    }

    public IpcameraReplayResultModel generateCameraPlaybackURLByCode(String cameraIndexCode, String beginTime, String endTime) {
        IpcameraReplayResultModel ipcameraReplayResultModel = new IpcameraReplayResultModel();

        String replayUrl = GetCameraPlaybackURL(cameraIndexCode, beginTime, endTime);
        ipcameraReplayResultModel.setReplayUrl(replayUrl);
        return ipcameraReplayResultModel;
    }

    public IpcameraCodeResultModel generateCameraCode() {
        IpcameraCodeResultModel ipcameraCodeResultModel = new IpcameraCodeResultModel();

        String codeStauts = GetCameraCode();
        ipcameraCodeResultModel.setCodeStatus(codeStauts);
        return ipcameraCodeResultModel;
    }

//    public String GetCameraPlaybackURLByName(String cameraName, String beginTime, String endTime){
//        try{
//
//        }catch(Exception e){
//
//        }
//    }

    public String GetCameraPlaybackURL(String cameraIndexCode, String beginTime, String endTime) {
        String url = null;
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
        jsonBody.put("expand", "streamform=rtp");
        jsonBody.put("beginTime", beginTime);
        jsonBody.put("endTime", endTime);
        String body = jsonBody.toJSONString();
       //调用接口
        logger.info(body);
        try{
            String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
            url = JSON.parseObject(result).getJSONObject("data").getString("url");
//            logger.info("cameraIndexCode:" + cameraIndexCode + ", beginTime:" + beginTime + ", endTime:" + endTime);
//            logger.error(JSON.parseObject(result).toJSONString());

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return url;
    }

    public String GetCameraCode() {
        String result = null;
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
        try{
            result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        JSONArray JSONArray = JSON.parseObject(result).getJSONObject("data").getJSONArray("list");

        for(int i = 0; i<JSONArray.size(); i++){
            JSONObject cameraObj = JSONArray.getJSONObject(i);
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
//            System.out.println("Inserted records into the table...");
        }
        return "success";
    }
}


