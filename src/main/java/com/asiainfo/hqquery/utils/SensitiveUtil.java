package com.asiainfo.hqquery.utils;

import com.asiainfo.dacp.sdk.HttpHelper;
import com.asiainfo.hqquery.entity.ConfigInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description 加解密工具类
 */
@Slf4j
@Component
@Scope("prototype")
public class SensitiveUtil {

    @Autowired
    private ConfigInfo configInfo;

    public String transPhoneNo(String phoneNo) {
        String sensitivePhoneNo = "";
        try {
            Map<String, Object> crypt = new HashMap<>(10);
            crypt.put("tenantId", configInfo.getUser());
            crypt.put("dsId", configInfo.getDataSource());
            crypt.put("operType", configInfo.getOperType());
            crypt.put("defineId", configInfo.getDefineId());
            List<String> phoneList = new ArrayList<>();
            phoneList.add(phoneNo);
            crypt.put("content", phoneList);
            String result = HttpHelper.post(configInfo.getUser(), configInfo.getKey(), configInfo.getRestUrl(), crypt);
            JSONObject resultJson = JSONObject.fromObject(result);
            List<String> resultList = new ArrayList<>();
            resultList = (List<String>) resultJson.get("toContent");
            sensitivePhoneNo = resultList.get(0);
        } catch (Exception e) {
            log.error("获取加密信息失败: " + e.getMessage(), e);
        }
        return sensitivePhoneNo;
    }


}
