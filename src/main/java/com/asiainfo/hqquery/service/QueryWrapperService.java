package com.asiainfo.hqquery.service;

import com.asiainfo.hqquery.entity.ConfigInfo;
import com.asiainfo.hqquery.entity.Response;
import com.asiainfo.hqquery.entity.ResponseHead;
import com.asiainfo.hqquery.entity.XmlResponse;
import com.asiainfo.hqquery.entity.old.DetailWholeObj;
import com.asiainfo.hqquery.entity.old.DetailreplyDisInfo;
import com.asiainfo.hqquery.entity.old.TopWholeObj;
import com.asiainfo.hqquery.entity.old.TopreplyDisInfo;
import com.asiainfo.hqquery.utils.Constants;
import com.asiainfo.hqquery.utils.Variable;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description ${DESCRIPTION}
 */
@Slf4j
@Service
public class QueryWrapperService {


    @Autowired
    private ConfigInfo configInfo;


    public Response queryResponseHandler(String qryStr, JSONPObject qryJson, String phoneNoOri, String address) {
        Response r = new Response();
        String addressName = "";
        if (address.equals(configInfo.getAddress())) {
            addressName = configInfo.getAddressName();
        } else if (address.equals(configInfo.getAddressOldName())) {
            addressName =configInfo.getAddressOldName();
        }

        try {
            ///加载属性列表
            RPCServiceClient client = new RPCServiceClient();
            Options options = client.getOptions();
            //TODO 设置调用WebService的URL
            options.setTimeOutInMilliSeconds(1000000);

            EndpointReference epf = new EndpointReference(address);
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
            JSONObject resultJson = JSONObject.fromObject(result[0]);
            System.out.println(resultJson);
            //根据接口类型判断输出
            if (qryJson.get(Constants.TYPE).equals(Constants.HOUR)) {
                resultJson.discard(Constants.DATA);
            } else if (qryJson.get(Constants.TYPE).equals(Constants.TOP)) {
                resultJson.discard(Constants.EXTDATA);
            } else if (qryJson.get(Constants.TYPE).equals(Constants.DETAIL)) {
                List<JSONObject> detailList = new ArrayList<JSONObject>();
                detailList = (List<JSONObject>) resultJson.get(Constants.DATA);
                for (JSONObject value : detailList) {
                    value.put("phoneNo", phoneNoOri);
                }
                resultJson.put("data", detailList);
            }

            Object result_stat = resultJson.get("result");
            resultJson.discard("resMsg");
            resultJson.discard("result");
            resultJson.discard("sums");
            resultJson.discard("pageInfo");
            if ((result_stat.toString()).equals(Constants.SUCCESS)) {
                r.setHead(new ResponseHead("1", "0", "查询成功,当前查询地址为" + addressName));
                r.setBody(resultJson);
            } else {
                r.setHead(new ResponseHead("1", "-1", "查询失败,当前查询地址为" + addressName));
            }
        } catch (Exception e) {
            r.setHead(new ResponseHead("0", "-1", "HBase服务异常,当前查询地址为" + addressName));
            log.error("HBase服务异常,当前查询地址为" + addressName,e);
        }
        return r;
    }


    //XML版本榜单
    public xmlresponse topQuerryResponseHandlerXml(String qryStr, String phoneNo) {
        String head = "";
        String body = "";
        try {
            InputStream in = QueryWrapperService.class.getClassLoader().getResourceAsStream("conf.properties");
            ///加载属性列表
            RPCServiceClient client = new RPCServiceClient();
            Options options = client.getOptions();
            options.setTimeOutInMilliSeconds(1000000);
            EndpointReference epf = new EndpointReference(Variable.address);
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
            JSONObject resultJson = JSONObject.fromObject(result[0]);
            //JSON格式输出
            System.out.println(resultJson);
            Object result_info = resultJson.get("extData");
            resultJson.put("stats", result_info);
            Object result_data = resultJson.get("data");
            resultJson.put("replyDisInfo", result_data);
            resultJson.discard("extData");
            resultJson.discard("data");
            String jsonstr = resultJson.toString();
            Object stats = resultJson.get("stats");
            JSONObject statsJson = JSONObject.fromObject(stats);
            String totalFlow = statsJson.getString("totalFlow");

            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(2);
            TopWholeObj two = new Gson().fromJson(jsonstr, TopWholeObj.class);

            List<TopreplyDisInfo> replyDisInfos = two.getReplyDisInfo();
            for (TopreplyDisInfo replyDisInfo : replyDisInfos) {
                replyDisInfo.setGroupValueName(replyDisInfo.getGroupValueName().split("\\|")[1]);
                body = body + "<data>" + "<GroupValueName>" + replyDisInfo.getGroupValueName() + "</GroupValueName>"
                        + "<AcceFlow>" + nt.format(Double.valueOf(replyDisInfo.getGroupTotalFlow()) / Double.valueOf(totalFlow)) + "</AcceFlow>"
                        + "</data>";
            }
            head = "<retType>" + "1" + "</retType><retcode>" + "0" + "</retcode><retmsg>" + "success" + "</retmsg>";
            xmlresponse r = new xmlresponse(head, body);
            System.out.println("---榜单查询结果---" + head + body);
            return r;
        } catch (Exception e) {
            head = "<retType>" + "0" + "</retType><retcode>" + "-1" + "</retcode><retmsg>" + "HBase查询无返回结果" + "</retmsg>";
            e.printStackTrace();
            log.warn("榜单查询异常, {}", e.getMessage());
        }
        System.out.println(head + body);
        xmlresponse r = new xmlresponse(head, body);
        return r;

    }

    //XML版本详单
    public XmlResponse detailQuerryResponseHandlerXml(String qryStr, String phoneNo) {
        String head = "";
        String body = "";
        try {
            ///加载属性列表
            RPCServiceClient client = new RPCServiceClient();
            Options options = client.getOptions();
            //TODO 设置调用WebService的URL
            options.setTimeOutInMilliSeconds(1000000);
            EndpointReference epf = new EndpointReference(Variable.address);
            options.setTo(epf);

            QName qname = new QName("http://axis2server.drquery.billing.asiainfo.com", "getAxis2Result");
            Object[] result = client.invokeBlocking(qname, new String[]{qryStr}, new Class[]{String.class});
            JSONObject resultJson = JSONObject.fromObject(result[0]);

            if(log.isDebugEnabled()){
                log.debug(resultJson);
            }

            /*
             * 设置将调用的方法，http://ws.apache.org/axis2是方法
             * 默认（没有package）命名空间，如果有包名
             * 就是http://service.hoo.com 包名倒过来即可
             * sayHello就是方法名称了
             */
            //TODO 指定调用的方法和传递参数数据

            Object result_info = resultJson.get("extData");
            resultJson.put("stats", result_info);
            Object result_data = resultJson.get("data");
            resultJson.put("replyDisInfo", result_data);
            resultJson.discard("extData");
            resultJson.discard("data");
            String jsonstr = resultJson.toString();
            Object result_sum = resultJson.getJSONObject("stats");
            JSONObject result_sum_Json = JSONObject.fromObject(result_sum);
            String totalRecord = result_sum_Json.getString("totalCount");
            DetailWholeObj dwo = new Gson().fromJson(jsonstr, DetailWholeObj.class);

            List<DetailreplyDisInfo> ReplyDisInfos = dwo.getReplyDisInfo();
            for (DetailreplyDisInfo replyDisInfo : ReplyDisInfos) {
                //处理详单数据内容
                //终端类型类似 OPPO|OPPO X9007转换为OPPO X9007
                replyDisInfo.setTermTypeName(replyDisInfo.getTermTypeName().split("\\|")[1]);
                String tempstr = new String(replyDisInfo.getAccessModeName());
                String tempTermType =  getTermType(replyDisInfo);
                String tempApp = getAppName(replyDisInfo);
                String totalURL = "空";
                //时间格式转换
                String startTime = getTimeStamp(ReplyDisInfo.getStartTime());
                //网络类型如果不是2G/3G/4G或者数据异常，显示为2/3/4G
                if (!tempstr.equals("2G") && !tempstr.equals("3G") && !tempstr.equals("4G")) {
                    ReplyDisInfo.setAccessModeName("2/3/4G");
                }
                //app特殊协议转译
                                /*if("DNS".equals(aaData.getAppName())){
                                        tempApp="域名解析服务";
                                }
                                if("IP_TCP".equals(aaData.getAppName())||"IP_IPv6-ICMP".equals(aaData.getAppName())||"SymcD".equals(aaData.getAppName())||"SIP".equals(aaData.getAppName())||"IP_ESP".equals(aaData.getAppName())||"HTTPS".equals(aaData.getAppName())||"HTTP".equals(aaData.getAppName())){
                                        tempApp="安全加密网页";
                                }*/
                getCggSnIp(replyDisInfo);

                //URL如果没有，则显示“空”;7月23日改为域名
                if (null == ReplyDisInfo.getFullDomain() || "".equals(ReplyDisInfo.getFullDomain()) || "null".equals(ReplyDisInfo.getFullDomain()) || "-99".equals(ReplyDisInfo.getFullDomain())) {
                    totalURL = ReplyDisInfo.getC_GGSN_IP();
                } else {
                    totalURL = ReplyDisInfo.getFullDomain();
                }

                                /*else if(null==aaData.getDetailURL() || "".equals(aaData.getDetailURL())|| "-99".equals(aaData.getDetailURL())){
					totalURL=aaData.getFullDomain();
                                    }
                                else{
                                    totalURL=aaData.getFullDomain()+aaData.getDetailURL();
                                }*/

                body = body + "<data>" + "<startTime>" + startTime + "</startTime>"
                        + "<accessModeName>" + ReplyDisInfo.getAccessModeName() + "</accessModeName>"
                        + "<termTypeName>" + tempTermType + "</termTypeName>"
                        + "<appName>" + tempApp + "</appName>"
                        + "<upFlow>" + ReplyDisInfo.getUpFlow() + "</upFlow>"
                        + "<downFlow>" + ReplyDisInfo.getDownFlow() + "</downFlow>"
                        + "<detailURL>" + totalURL + "</detailURL>"
                        + "</data>";
            }
            body = "<totalNum>" + totalRecord + "</totalNum>" + body;
            head = "<retType>" + "1" + "</retType><retcode>" + "0" + "</retcode><retmsg>" + "success" + "</retmsg>";

            xmlresponse r = new xmlresponse(head, body);
            System.out.println("---详单查询结果---" + head + body);
            return r;
        } catch (Exception e) {
            head = "<retType>" + "0" + "</retType><retcode>" + "-1" + "</retcode><retmsg>" + "HBase查询无返回结果" + "</retmsg>";
            e.printStackTrace();
            log.warn("详单查询异常, {}", e.getMessage());
        }
        System.out.println(head + body);
        XmlResponse r = new XmlResponse(head, body);
        return r;
    }

    private void getCggSnIp(DetailreplyDisInfo replyDisInfo) {
        //url,域名,ip
        if (null == replyDisInfo.getC_GGSN_IP() || "null".equals(replyDisInfo.getC_GGSN_IP()) || "".equals(replyDisInfo.getC_GGSN_IP()) || "-99".equals(replyDisInfo.getC_GGSN_IP())) {
            replyDisInfo.setC_GGSN_IP("空");
        }
    }

    /**
     * app类型异常判断
     * @param replyDisInfo
     * @return
     */
    private String getAppName(DetailreplyDisInfo replyDisInfo) {
        String appType = replyDisInfo.getAppName();
        if (null == replyDisInfo.getAppName() || "".equals(replyDisInfo.getAppName()) || "-99".equals(replyDisInfo.getAppName())) {
            appType = "其它";
        }
        return appType;
    }

    /**
     *  终端类型异常判断
     * @param ReplyDisInfo
     * @return
     */
    private String getTermType(DetailreplyDisInfo ReplyDisInfo) {
        String termType = ReplyDisInfo.getTermTypeName();
        if (null == ReplyDisInfo.getTermTypeName() || "".equals(ReplyDisInfo.getTermTypeName()) || "\\N".equals(ReplyDisInfo.getTermTypeName()) || "-99".equals(ReplyDisInfo.getTermTypeName())) {
            termType = "不详";
        }
        return termType;
    }
}
