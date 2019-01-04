package com.asiainfo.hqquery.entity.old;

public class DetailreplyDisInfo {
    private String startTime;//起始时间
    private String accessModeName;//网络类型
    private String termTypeName;//终端类型
    private String appName;//业务名称
    private String fullDomain;
    private String detailURL;//url
    private String upFlow;//上行流量
    private String downFlow;//下行流量
    private String C_GGSN_IP;//IP

    public String getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(String upFlow) {
        this.upFlow = upFlow;
    }

    public String getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(String downFlow) {
        this.downFlow = downFlow;
    }

    public String getC_GGSN_IP() {
        return C_GGSN_IP;
    }

    public void setC_GGSN_IP(String C_GGSN_IP) {
        this.C_GGSN_IP = C_GGSN_IP;
    }

    //访问域名
    public String getFullDomain() {
        return fullDomain;
    }

    public void setFullDomain(String fullDomain) {
        this.fullDomain = fullDomain;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAccessModeName() {
        return accessModeName;
    }

    public void setAccessModeName(String accessModeName) {
        this.accessModeName = accessModeName;
    }

    public String getTermTypeName() {
        return termTypeName;
    }

    public void setTermTypeName(String termTypeName) {
        this.termTypeName = termTypeName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDetailURL() {
        return detailURL;
    }

    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }

    @Override
    public String toString() {
        return "DetailaaData [startTime=" + startTime + ", accessModeName=" + accessModeName + ", termTypeName="
                + termTypeName + ", appName=" + appName + ", fullDomain=" + fullDomain + ", detailURL=" + detailURL + ", upFlow=" + upFlow + ", downFlow=" + downFlow + ", C_GGSN_IP=" + C_GGSN_IP + "]";
    }

}
