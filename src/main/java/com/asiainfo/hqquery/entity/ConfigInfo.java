package com.asiainfo.hqquery.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom")
public class ConfigInfo {
    private Boolean isSensitive = false;
    private String address = "";
    private String addressOld = "";
    private String addressName = "";
    private String addressOldName = "";
    private String isTesting = "true";
    private String user = "";
    private String restUrl = "";
    private String key = "";
    private String dataSource = "";
    private String operType = "";
    private String defineId = "";
}
