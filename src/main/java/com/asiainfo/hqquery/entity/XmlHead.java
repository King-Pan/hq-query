package com.asiainfo.hqquery.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description xml相应头
 */
@Getter
@Setter
public class XmlHead {

    private String retType;

    private String retcode;

    private String retmsg;

    public XmlHead(){}

    public XmlHead(String retType,String retcode,String retmsg){
        this.retType = retType;
        this.retcode = retcode;
        this.retmsg = retmsg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<retType>").append(retType).append("</retType>");
        sb.append("<retcode>").append(retcode).append("</retcode>");
        sb.append("<retmsg>").append(retmsg).append("</retmsg>");
        return sb.toString();
    }
}
