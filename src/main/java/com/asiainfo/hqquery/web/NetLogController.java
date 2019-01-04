package com.asiainfo.hqquery.web;

import com.asiainfo.hqquery.entity.ConfigInfo;
import com.asiainfo.hqquery.entity.JsonDetail;
import com.asiainfo.hqquery.entity.Response;
import com.asiainfo.hqquery.service.QueryWrapperService;
import com.asiainfo.hqquery.utils.Constants;
import com.asiainfo.hqquery.utils.Variable;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description 上网日志查询对外接口
 */
@Slf4j
@Controller
@RequestMapping("/qry/")
public class NetLogController {

    @Autowired
    private QueryWrapperService queryWrapperService;

    @Autowired
    private ConfigInfo configInfo;

    //JSON格式详单查询
    @RequestMapping(value = "/detail", produces = "application/json")
    @ResponseBody
    public Response detailQueryHandler(HttpServletRequest req, @RequestBody JsonDetail jd) {
        Response response = new Response();
        String phoneNo = "", startTime = "", endTime = "", startIndex = "", offset = "", opId = "", opName = "", srcSystemCode = "", appId = "";
        try {
            phoneNoOri = jd.getPhoneNo();
            phoneNo = phoneNoOri;
            if (Constants.TRUE_STRING.equals(configInfo.getIsSensitive())) {
                phoneNo = doSensitive.transPhoneNo(phoneNoOri);
            }
            startTime = jd.getStartTime();
            endTime = jd.getEndTime();
            startIndex = jd.getStartIndex();
            offset = jd.getOffset();
            opId = jd.getOpId();
            opName = jd.getOpName();
            appId = jd.getAppId();
            srcSystemCode = jd.getSrcSystemCode();
        } catch (Exception e) {
            log.error("参数解析有误", e);
        }
        String qryTime = CustomUtils.getDateStrNow("yyyyMMddHHmmss");
        String qryID = qryTime + phoneNo;
        JSONObject qryJson = new JSONObject();
        qryJson.put("interfaceType", "F12");
        qryJson.put("qryId", qryID);
        qryJson.put("phoneNo", phoneNo);
        qryJson.put("startTime", startTime);
        qryJson.put("endTime", endTime);
        qryJson.put("startIndex", startIndex);
        qryJson.put("offset", offset);
        qryJson.put("opId", opId);
        qryJson.put("opName", opName);
        qryJson.put("srcSystemCode", srcSystemCode);
        qryJson.put("appId", appId);
        //接口类型
        qryJson.put("type", "detail");
        String queryStr = qryJson.toString();
        System.out.println(queryStr);
        response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.address);
        if ("-1".equals(response.getHead().getRetCode()))
            response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.addressOld);
        return response;
    }

    //JSON格式榜单查询
    @RequestMapping(value = "/top", produces = "application/json")
    @ResponseBody
    public Response topQueryHandler(HttpServletRequest req, @RequestBody JsonTop jt) {
        Response response = new Response();
        String phoneNo = "", startTime = "", endTime = "", groupColumnCode = "", topNum = "", opId = "", opName = "", srcSystemCode = "";
        try {
            phoneNoOri = jt.getPhoneNo();
            phoneNo = phoneNoOri;
            if (Variable.isSensitive.equals("true"))
                phoneNo = doSensitive.transPhoneNo(phoneNoOri);
            startTime = jt.getStartTime();
            endTime = jt.getEndTime();
            groupColumnCode = jt.getGroupColumnCode();
            topNum = jt.getTopNum();
            opId = jt.getOpId();
            opName = jt.getOpName();
            srcSystemCode = jt.getSrcSystemCode();
        } catch (Exception e) {
            System.out.println("参数解析有误");
            e.printStackTrace();
        }
        String qryTime = CustomUtils.getDateStrNow("yyyyMMddHHmmss");
        String qryID = qryTime + phoneNo;
        JSONObject qryJson = new JSONObject();
        qryJson.put("interfaceType", "F11");
        qryJson.put("qryId", qryID);
        qryJson.put("phoneNo", phoneNo);
        qryJson.put("startTime", startTime);
        qryJson.put("endTime", endTime);
        qryJson.put("groupColumnCode", groupColumnCode);
        qryJson.put("topNum", topNum);
        qryJson.put("opId", opId);
        qryJson.put("opName", opName);
        qryJson.put("srcSystemCode", srcSystemCode);
        //接口类型
        qryJson.put("type", "top");
        String queryStr = qryJson.toString();
        System.out.println(queryStr);
        response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.address);
        if ("-1".equals(response.getHead().getRetCode()))
            response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.addressOld);
        return response;

    }

    //JSON格式小时流量查询
    @RequestMapping(value = "/hour", produces = "application/json")
    @ResponseBody
    public Response hourQueryHandler(HttpServletRequest req, @RequestBody JsonHour jh) throws ParseException {
        Response response = new Response();
        String phoneNo = "", startTime = "", endTime = "", groupColumnCode = "", topNum = "", opId = "", opName = "", srcSystemCode = "";
        try {
            phoneNoOri = jh.getPhoneNo();
            phoneNo = phoneNoOri;
            if (Variable.isSensitive.equals("true"))
                phoneNo = doSensitive.transPhoneNo(phoneNoOri);
            startTime = jh.getStartTime();
            endTime = jh.getEndTime();
            groupColumnCode = "appId";
            topNum = "10000";
            opId = "00001";
            opName = "kefu";
            srcSystemCode = "kefuhour";
        } catch (Exception e) {
            System.out.println("参数解析有误");
            e.printStackTrace();
        }
        String qryTime = CustomUtils.getDateStrNow("yyyyMMddHHmmss");
        String qryID = qryTime + phoneNo;
        JSONObject qryJson = new JSONObject();
        qryJson.put("interfaceType", "F11");
        qryJson.put("qryId", qryID);
        qryJson.put("phoneNo", phoneNo);
        qryJson.put("startTime", startTime);
        qryJson.put("endTime", endTime);
        qryJson.put("groupColumnCode", groupColumnCode);
        qryJson.put("topNum", topNum);
        qryJson.put("opId", opId);
        qryJson.put("opName", opName);
        qryJson.put("srcSystemCode", srcSystemCode);
        //接口类型
        qryJson.put("type", "hour");
        String nextHour = getNextStartHour(startTime);
        JSONObject resultJson = new JSONObject();

        //判断结束时间与下一小时前后关系
        if (Long.parseLong(endTime) <= Long.parseLong(nextHour)) {
            String queryStr = qryJson.toString();
            System.out.println(queryStr);
            response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.address);
            if ("-1".equals(response.getHead().getRetCode()))
                response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.addressOld);
            //输出该小时的返回值,即流量使用情况
            resultJson.put("hour", startTime.substring(0, 10));
            resultJson.put("result", response.getBody());
            response.setBody(resultJson);
        } else {
            //循环查询
            while (Long.parseLong(endTime) > Long.parseLong(nextHour)) {
                qryJson.put("endTime", nextHour);
                String queryStr = qryJson.toString();
                response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.address);
                if ("-1".equals(response.getHead().getRetCode()))
                    response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.addressOld);
                //输出每小时的返回值,即流量使用情况
                resultJson.put(qryJson.get("startTime").toString().substring(0, 10), response.getBody());
                qryJson.put("startTime", nextHour);
                nextHour = getNextStartHour(nextHour);
            }
            //跳出循环后的最后一次查询
            qryJson.put("endTime", endTime);
            String queryStr = qryJson.toString();
            System.out.println(queryStr);
            response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.address);
            if ("-1".equals(response.getHead().getRetCode()))
                response = queryWrapperService.QuerryResponseHandler(queryStr, qryJson, phoneNoOri, Variable.addressOld);
            resultJson.put(qryJson.get("startTime").toString().substring(0, 10), response.getBody());
            response.setBody(resultJson);
        }
        return response;
    }

    //yyyyMMddHHmmss转换为yyyy-MM-dd HH:mm:ss
    public static String getTimeStamp(String timeInput) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = (Date) sdf2.parse(timeInput);
        return sdf1.format(date);
    }

    //yyyy-MM-dd HH:mm:ss转换为yyyyMMddHHmmss
    public static String getTimeStr(String timeInput) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = (Date) sdf1.parse(timeInput);
        return sdf2.format(date);
    }

    //增加一小时
    public static String addOneHour(String timeInput) throws ParseException {
        Calendar ca = Calendar.getInstance();
        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = sdf2.parse(timeInput);
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, 1);
        return sdf2.format(ca.getTime());
    }

    //取下一小时整点
    public static String getNextStartHour(String timeInput) throws ParseException {
        String startHour = timeInput.substring(0, 10) + "0000";
        return addOneHour(startHour);
    }

    //读取XML格式入参
    public static String getValueByTagName(Document doc, String tagName) {
        if (doc == null) {
            return "";
        }
        NodeList pl = doc.getElementsByTagName(tagName);
        if (pl != null && pl.getLength() > 0) {
            return pl.item(0).getTextContent();
        }
        return "";
    }

    //XML格式榜单查询
    @RequestMapping(value = "/xmltop", produces = "text/xml")
    @ResponseBody
    public xmlresponse newtopQueryHandler(HttpServletRequest req) throws ParseException {
        xmlresponse response = new xmlresponse();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        String phoneNo = "", startTime = "", endTime = "", smsCheck = "";
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse((req.getInputStream()));
            System.out.println(doc);
            phoneNoOri = getValueByTagName(doc, "phoneNumber");
            phoneNo = phoneNoOri;
            if (Variable.isSensitive.equals("true"))
                phoneNo = doSensitive.transPhoneNo(phoneNoOri);
            startTime = getValueByTagName(doc, "startTime");
            System.out.println(startTime);
            startTime = getTimeStr(startTime);
            System.out.println(startTime);
            endTime = getValueByTagName(doc, "endTime");
            endTime = getTimeStr(endTime);
            smsCheck = getValueByTagName(doc, "smsCheck");
            System.out.println(phoneNo + "---debug---榜单查询");
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
            String head = "<retType>" + "0" + "</retType><retcode>" + "-3" + "</retcode><retmsg>" + "参数解析有误" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            String head = "<retType>" + "0" + "</retType><retcode>" + "-4" + "</retcode><retmsg>" + "参数输入有误" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        }
        String queryTime = CustomUtils.getDateStrNow("yyyy-MM-dd%20HH:mm:ss");
        //1.校验短信验证码
        if (smsCheck == null) {
            String head = "<retType>" + "0" + "</retType><retcode>" + "-5" + "</retcode><retmsg>" + "未获取到短信验证码" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        }
        boolean flag = false;
        //根据对端是否使用测试环境修改conf.properties文件,ture代表不校验短信验证码
        if ("true".equals(Variable.isTesting)) {

            flag = true;
        } else {
            flag = queryWrapperService.checkSMSCorrection(phoneNoOri, smsCheck);
        }
        if (!flag) {
            String head = "<retType>" + "0" + "</retType><retcode>" + "-2" + "</retcode><retmsg>" + "短信验证码错误或失效" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        }
        String qryTime = CustomUtils.getDateStrNow("yyyyMMddHHmmss");
        String qryID = qryTime + phoneNo;
        JSONObject qryJson = new JSONObject();
        qryJson.put("interfaceType", "F11");
        qryJson.put("qryId", qryID);
        qryJson.put("phoneNo", phoneNo);
        qryJson.put("startTime", startTime);
        qryJson.put("endTime", endTime);
        qryJson.put("groupColumnCode", "appId");
        qryJson.put("topNum", "10");
        qryJson.put("opId", "00002");
        qryJson.put("opName", "wangting");
        qryJson.put("srcSystemCode", "wangting");
        String queryStr = qryJson.toString();
        System.out.println(queryStr);
        response = queryWrapperService.topQuerryResponseHandlerXml(queryStr, phoneNo);
        return response;
    }

    //XML格式详单查询
    @RequestMapping(value = "/xmldetail", produces = "text/xml")
    @ResponseBody
    public xmlresponse newdetailQueryHandler(HttpServletRequest req) throws ParseException {
        xmlresponse response = new xmlresponse();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        String phoneNo = "", startTime = "", endTime = "", smsCheck = "", pageSize = "", curPage = "";
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse((req.getInputStream()));
            phoneNoOri = getValueByTagName(doc, "phoneNumber");
            phoneNo = phoneNoOri;
            if (Variable.isSensitive.equals("true"))
                phoneNo = doSensitive.transPhoneNo(phoneNoOri);
            startTime = getValueByTagName(doc, "startTime");
            startTime = getTimeStr(startTime);
            endTime = getValueByTagName(doc, "endTime");
            endTime = getTimeStr(endTime);
            smsCheck = getValueByTagName(doc, "smsCheck");
            curPage = getValueByTagName(doc, "curPage");
            pageSize = getValueByTagName(doc, "pageSize");
            curPage = curPage.length() == 0 ? "1" : curPage;
            pageSize = pageSize.length() == 0 ? "30" : pageSize;
            System.out.printf(phoneNo + "---debug---详单查询");
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
            String head = "<retType>" + "0" + "</retType><retcode>" + "-3" + "</retcode><retmsg>" + "参数解析有误" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            String head = "<retType>" + "0" + "</retType><retcode>" + "-4" + "</retcode><retmsg>" + "参数输入有误" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        }
        String queryTime = CustomUtils.getDateStrNow("yyyy-MM-dd%20HH:mm:ss");
        //1.校验短信验证码
        if (smsCheck == null) {
            String head = "<retType>" + "0" + "</retType><retcode>" + "-5" + "</retcode><retmsg>" + "未获取到短信验证码" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        }
        boolean flag = false;
        //根据对端是否使用测试环境修改conf.properties文件,ture代表不校验短信验证码
        if ("true".equals(Variable.isTesting)) {
            flag = true;
        } else {
            flag = queryWrapperService.checkSMSCorrection(phoneNoOri, smsCheck);
        }
        if (!flag) {
            String head = "<retType>" + "0" + "</retType><retcode>" + "-2" + "</retcode><retmsg>" + "短信验证码错误或失效" + "</retmsg>";
            String body = "";
            response.setResponse_head(head);
            response.setResponse_body(body);
            return response;
        }
        String qryTime = CustomUtils.getDateStrNow("yyyyMMddHHmmss");
        String qryID = qryTime + phoneNo;
        JSONObject qryJson = new JSONObject();
        qryJson.put("interfaceType", "F12");
        qryJson.put("qryId", qryID);
        qryJson.put("phoneNo", phoneNo);
        qryJson.put("startTime", startTime);
        qryJson.put("endTime", endTime);
        qryJson.put("startIndex", curPage);
        qryJson.put("offset", pageSize);
        qryJson.put("opId", "00002");
        qryJson.put("opName", "wangting");
        qryJson.put("srcSystemCode", "wangting");
        qryJson.put("appId", "");
        String queryStr = qryJson.toString();
        System.out.println(queryStr);
        response = queryWrapperService.detailQuerryResponseHandlerXml(queryStr, phoneNo);
        return response;
    }
}
