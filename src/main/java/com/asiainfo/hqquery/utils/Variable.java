package com.asiainfo.hqquery.utils;

import java.util.Properties;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description ${DESCRIPTION}
 */
public class Variable {
    public static String isSensitive = "false";
    public static String address = "";
    public static String addressOld = "";
    public static String addressName = "";
    public static String addressOldName = "";
    public static String isTesting = "true";
    public static String user = "";
    public static String restUrl = "";
    public static String key = "";
    public static String dataSource = "";
    public static String operType = "";
    public static String defineId = "";

    static {
        try {
            Properties prop = new Properties();
            prop.load(Variable.class.getClassLoader().getResourceAsStream("conf.properties"));
            address = prop.getProperty("address");
            addressOld = prop.getProperty("addressOld");
            addressName = prop.getProperty("addressName");
            addressOldName = prop.getProperty("addressOldName");
            isSensitive = prop.getProperty("sensitive");
            isTesting = prop.getProperty("isTesting");
            user = prop.getProperty("user");
            restUrl = prop.getProperty("restUrl");
            key = prop.getProperty("key");
            dataSource = prop.getProperty("dataSource");
            operType = prop.getProperty("operType");
            defineId = prop.getProperty("defineId");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
