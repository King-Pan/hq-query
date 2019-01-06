package com.asiainfo.hqquery.web;

import club.javalearn.date.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.hqquery.entity.ConfigInfo;
import com.asiainfo.hqquery.entity.QueryJsonInfo;
import com.asiainfo.hqquery.entity.Response;
import com.asiainfo.hqquery.service.DrQueryService;
import com.asiainfo.hqquery.utils.FastJsonUtils;
import com.asiainfo.hqquery.utils.SensitiveUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description 上网日志对外http服务
 */
@Slf4j
@RestController
@RequestMapping("/qry")
public class InternetLogController {

    @Autowired
    private SensitiveUtil sensitiveUtil;

    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private DrQueryService drQueryService;


    /**
     * 获取json格式的上网日志榜单
     *
     * @param jsonInfo 查询参数
     * @return 返回榜单
     */
    @RequestMapping(value = "/top", produces = "application/json")
    public Object getTop(@RequestBody QueryJsonInfo jsonInfo) {
        //打印查询参数
        log(jsonInfo, "JSON榜单查询");
        // 设置qryId,加密phoneNo
        setQueryParam(jsonInfo);
        //接口类型
        jsonInfo.setType("top");
        jsonInfo.setInterfaceType("F11");

        String jsonString = FastJsonUtils.getBeanToJson(jsonInfo);
        log.info("JSON榜单查询DrQuery参数: {}", jsonString);
        return "";
    }

    /**
     * 获取json格式的上网日志详单
     *
     * @param jsonInfo 查询参数
     * @return 返回详单
     */
    @RequestMapping(value = "/detail", produces = "application/json")
    public Object getDetail(@RequestBody QueryJsonInfo jsonInfo) {
        //打印查询参数
        log(jsonInfo, "JSON详单查询");
        // 设置qryId,加密phoneNo
        setQueryParam(jsonInfo);
        //接口类型
        jsonInfo.setType("detail");
        jsonInfo.setInterfaceType("F12");
        String jsonString = FastJsonUtils.getBeanToJson(jsonInfo);
        log.info("JSON详单查询DrQuery参数: {}", jsonString);
        return "";
    }

    /**
     * 获取json格式的上网日志小时流量查询
     *
     * @param jsonInfo 查询参数
     * @return 小时上网日志
     */
    @RequestMapping(value = "/hour", produces = "application/json")
    public Object getHour(@RequestBody QueryJsonInfo jsonInfo) {
        Response response = new Response();
        JSONObject resultJson = new JSONObject();
        //打印查询参数
        log(jsonInfo, "JSON小时流量查询");
        // 设置qryId,加密phoneNo
        setQueryParam(jsonInfo);
        //设置类型
        jsonInfo.setInterfaceType("F11");
        jsonInfo.setType("hour");
        DateTime startTime = new DateTime(jsonInfo.getStartTime());
        DateTime nextHour = startTime.plusHours(1);
        DateTime endTime = new DateTime(jsonInfo.getEndTime());
        //结束时间小于等于下一个小时时间
        if (endTime.getMillis() <= nextHour.getMillis()) {
            jsonInfo.setEndTime(nextHour.toString(DateUtil.DEFAULT_TIME_FORMAT));
            response = drQueryService.getJsonResult(jsonInfo, configInfo.getAddress());
            if ("-1".equals(response.getHead().getRetCode())) {
                //切换到备用地址上
                response = drQueryService.getJsonResult(jsonInfo, configInfo.getAddressOld());
            }

            //输出该小时的返回值,即流量使用情况
            resultJson.put("hour", jsonInfo.getStartTime().substring(0, 10));
            resultJson.put("result", response.getBody());
            response.setBody(resultJson);
        } else {

        }
        return response;
    }


    private void log(QueryJsonInfo jsonInfo, String method) {
        log.info("查询方法: {},查询参数: {}", method, jsonInfo);
        log.info("查询手机号码:{},开始时间:{},结束时间:{}", jsonInfo.getPhoneNo(), jsonInfo.getStartTime(), jsonInfo.getEndTime());
    }

    private boolean validation(QueryJsonInfo jsonInfo) {
        if (StringUtils.isBlank(jsonInfo.getPhoneNo())) {
            log.error("query param phoneNo is null");
            return false;
        }
        if (StringUtils.isBlank(jsonInfo.getStartTime())) {
            log.error("query param startTime is null");
            return false;
        }
        if (StringUtils.isBlank(jsonInfo.getEndTime())) {
            log.error("query param endTime is null");
            return false;
        }
        DateTime startTime = new DateTime(jsonInfo.getStartTime());
        DateTime endTime = new DateTime(jsonInfo.getEndTime());
        int days = Days.daysBetween(startTime, endTime).getDays();
        if (days > 7 && days < 30) {
            log.warn("the startTime and endTime arg longer than 7 days less than 30 days");
        }
        if (days > 30) {
            log.error("the startTime and endTime arg longer than 30 days");
        }
        return true;
    }

    private void setQueryParam(QueryJsonInfo jsonInfo) {
        String phoneNo = sensitiveUtil.transPhoneNo(jsonInfo.getPhoneNo());
        log.info("加密前手机号码:{},加密后手机号码:{}", jsonInfo.getPhoneNo(), phoneNo);
        String qryId = DateUtil.getCurrentTime() + phoneNo;
        jsonInfo.setQryId(qryId);
        jsonInfo.setPhoneNo(phoneNo);
    }
}
