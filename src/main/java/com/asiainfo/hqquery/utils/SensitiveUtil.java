package com.asiainfo.hqquery.utils;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dacp.sdk.HttpHelper;
import lombok.extern.slf4j.Slf4j;

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
public class SensitiveUtil {
    public static String transPhoneNo(String phoneNo) {
        String sensitivePhoneNo = "";
        try {
            Map<String, Object> crypt = new HashMap<>(10);
            crypt.put("tenantId", Variable.user);
            crypt.put("dsId", Variable.dataSource);
            crypt.put("operType", Variable.operType);
            crypt.put("defineId", Variable.defineId);
            List<String> phoneList = new ArrayList<>();
            phoneList.add(phoneNo);
            crypt.put("content", phoneList);
            String result = HttpHelper.post(Variable.user, Variable.key, Variable.restUrl, crypt);
            JSONObject resultJson = JSONObject.parseObject(result);
            List<String> resultList = new ArrayList<>();
            resultList = (List<String>) resultJson.get("toContent");
            sensitivePhoneNo = resultList.get(0);
        } catch (Exception e) {
            log.error("获取加密信息失败: " + e.getMessage(),e);
        }
        return sensitivePhoneNo;
    }


}
