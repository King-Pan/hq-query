package com.asiainfo.hqquery.entity;

import lombok.Data;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description 前台服务参数实体
 */
@Data
public class JsonDetail {
    /**
     * 电话号码
     */
    private String phoneNo;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 开始索引
     */
    private String startIndex;
    /**
     * 偏移量 分页数
     */
    private String offset;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 操作员ID
     */
    private String opId;
    /**
     * 操作员名称
     */
    private String opName;
    /**
     * 调用系统
     */
    private String srcSystemCode;

}
