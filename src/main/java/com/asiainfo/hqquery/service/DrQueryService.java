package com.asiainfo.hqquery.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.hqquery.entity.*;
import com.asiainfo.hqquery.entity.old.TopWholeObj;
import com.asiainfo.hqquery.entity.old.TopreplyDisInfo;
import com.asiainfo.hqquery.utils.Constants;
import com.asiainfo.hqquery.utils.FastJsonUtils;
import com.asiainfo.hqquery.utils.SMSCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description DrQuery查询服务
 */
@Slf4j
@Service
public class DrQueryService {

    @Autowired
    private ConfigInfo configInfo;

    public Response getJsonResult(QueryJsonInfo jsonInfo, String address) {
        String qryStr = FastJsonUtils.getBeanToJson(jsonInfo);
        Response response = new Response();

        String addressName = "";
        if (address.equals(configInfo.getAddress())) {
            addressName = configInfo.getAddressName();
        } else if (address.equals(configInfo.getAddressOld())) {
            addressName = configInfo.getAddressOldName();
        }

        try {
            RPCServiceClient client = new RPCServiceClient();
            Options options = client.getOptions();
            //TODO 设置调用WebService的URL
            options.setTimeOutInMilliSeconds(1000000);

            EndpointReference epf = new EndpointReference(configInfo.getAddress());
            options.setTo(epf);
            /*
             * 设置将调用的方法，http://ws.apache.org/axis2是方法
             * 默认（没有package）命名空间，如果有包名
             * 就是http://service.hoo.com 包名倒过来即可
             * sayHello就是方法名称了
             */
            //TODO 指定调用的方法和传递参数数据
            QName qname = new QName("http://axis2server.drquery.billing.asiainfo.com", "getAxis2Result");
            Object[] result = client.invokeBlocking(qname, new String[]{qryStr}, new Class[]{String.class});
            JSONObject resultJson = JSONObject.parseObject(result[0].toString());
            if (log.isDebugEnabled()) {
                log.debug(resultJson.toString());
            }
            //根据接口类型判断输出
            if (jsonInfo.getType().equals(Constants.HOUR)) {
                resultJson.remove(Constants.DATA);
            } else if (jsonInfo.getType().equals(Constants.TOP)) {
                resultJson.remove(Constants.EXTDATA);
            } else if (jsonInfo.getType().equals(Constants.DETAIL)) {
                List<JSONObject> detailList = new ArrayList<JSONObject>();
                JSONArray jsonArray = resultJson.getJSONArray(Constants.DATA);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject object = (JSONObject) iterator.next();
                    object.put("phoneNo", jsonInfo.getPhoneNo());
                    detailList.add(object);
                }
                resultJson.put("data", detailList);
            }

            Object result_stat = resultJson.get("result");
            resultJson.remove("resMsg");
            resultJson.remove("result");
            resultJson.remove("sums");
            resultJson.remove("pageInfo");
            if ((result_stat.toString()).equals(Constants.SUCCESS)) {
                response.setHead(new ResponseHead("1", "0", "查询成功,当前查询地址为" + addressName));
                response.setBody(resultJson);
            } else {
                response.setHead(new ResponseHead("1", "-1", "查询失败,当前查询地址为" + addressName));
            }
        } catch (Exception e) {
            response.setHead(new ResponseHead("0", "-1", "Hbase服务异常,当前查询地址为" + addressName));
            log.error("调用DrQuery服务失败,地址:{},参数:{}", configInfo.getAddress(), jsonInfo);
            log.error("调用DrQuery服务失败:" + e.getMessage(), e);
        }
        return response;
    }


    /**
     * 检查短信验证码正确性
     *
     * @param phoneNumber 电话号码
     * @param smsCheck    验证吗
     * @return 验证成功返回true，失败返回false
     */
    public boolean checkSMSCorrection(String phoneNumber, String smsCheck) {
        String str = SMSCheckUtil.startMethod(phoneNumber, smsCheck);
        str = str.substring(str.indexOf("<errorcode>") + "<errorcode>".length(), str.indexOf("</errorcode>"));
        return str.equals("0") ? true : false;
    }

    public XmlResponse getTopXmlResult(QueryJsonInfo jsonInfo, String address) {
        XmlHead head;
        XmlBody body;
        XmlResponse xmlResponse;
        StringBuilder sbBody = new StringBuilder();
        try {
            String qryStr = FastJsonUtils.getBeanToJson(jsonInfo);
            ///加载属性列表
            RPCServiceClient client = new RPCServiceClient();
            Options options = client.getOptions();
            options.setTimeOutInMilliSeconds(1000000);
            EndpointReference epf = new EndpointReference(address);
            options.setTo(epf);
            /*
             * 设置将调用的方法，http://ws.apache.org/axis2是方法
             * 默认（没有package）命名空间，如果有包名
             * 就是http://service.hoo.com 包名倒过来即可
             * sayHello就是方法名称了
             */
            QName qname = new QName("http://axis2server.drquery.billing.asiainfo.com", "getAxis2Result");
            Object[] result = client.invokeBlocking(qname, new String[]{qryStr}, new Class[]{String.class});
            JSONObject resultJson = JSONObject.parseObject(result[0].toString());

            Object result_info = resultJson.get("extData");
            resultJson.put("stats", result_info);
            Object result_data = resultJson.get("data");
            resultJson.put("replyDisInfo", result_data);
            resultJson.remove("extData");
            resultJson.remove("data");
            Object stats = resultJson.get("stats");
            JSONObject statsJson = JSONObject.parseObject(stats.toString());
            String totalFlow = statsJson.getString("totalFlow");

            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(2);
            TopWholeObj two = FastJsonUtils.getJsonToBean(resultJson.toJSONString(), TopWholeObj.class);

            List<TopreplyDisInfo> replyDisInfos = two.getReplyDisInfo();

            for (TopreplyDisInfo replyDisInfo : replyDisInfos) {
                replyDisInfo.setGroupValueName(replyDisInfo.getGroupValueName().split("\\|")[1]);
                sbBody.append(new XmlBody(replyDisInfo.getGroupValueName(), nt.format(Double.valueOf(replyDisInfo.getGroupTotalFlow()) / Double.valueOf(totalFlow))));
            }

            head = new XmlHead("1", "0", "success");
        } catch (Exception e) {
            head = new XmlHead("0", "-1", "HBase查询无返回结果");
            log.error("榜单查询异常, {}", e.getMessage());
        }
        xmlResponse = new XmlResponse(head.toString(), sbBody.toString());
        return xmlResponse;
    }

    public XmlResponse getXmlResult(QueryJsonInfo jsonInfo, String address) {

        return null;
    }

}
