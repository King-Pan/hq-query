package com.asiainfo.hqquery.entity;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description ${DESCRIPTION}
 */
public class ResponseHead {
    private String retType;
    private String retCode;
    private String retMsg;
    public ResponseHead(String retType, String retCode, String retMsg) {
        super();
        this.retType = retType;
        this.retCode = retCode;
        this.retMsg = retMsg;
    }
    public String getRetType() {
        return retType;
    }
    public void setRetType(String retType) {
        this.retType = retType;
    }
    public String getRetCode() {
        return retCode;
    }
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
    public String getRetMsg() {
        return retMsg;
    }
    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

}