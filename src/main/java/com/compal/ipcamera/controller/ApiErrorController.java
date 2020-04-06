package com.compal.ipcamera.controller;

import com.compal.ipcamera.model.ResponseModel;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping("/error")
    public ResponseEntity<ResponseModel> handleError(HttpServletRequest request) {
        HttpStatus status = this.getStatus(request);
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ResponseModel responseModel = new ResponseModel();
        HttpStatus statusCode2 = HttpStatus.OK;

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                System.out.println(404);
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                System.out.println(500);
            }
        }
        return new ResponseEntity<ResponseModel>(responseModel, statusCode2);
    }

    /**
     * 获取状态码
     * @param request
     * @return
     */
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode.intValue());
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}







