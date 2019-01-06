package com.asiainfo.hqquery.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description 查询drquery参数信息
 */
@Data
public class QueryJsonInfo {
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
    /**
     * 接口类型
     */
    private String interfaceType;
    /**
     * 查询id = 当前时间 + 加密后的手机号码
     */
    private String qryId;

    /**
     * 接口类型
     */
    private String type;
}
