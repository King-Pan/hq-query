package com.asiainfo.hqquery.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description ${DESCRIPTION}
 */
@XmlRootElement(name = "xmlResponse")
public class XmlResponse {

    @XmlElement
    public String response_head;


    @XmlElement
    public String response_body;


    @XmlTransient
    public String getResponse_head() {
        return response_head;
    }

    public void setResponse_head(String response_head) {
        this.response_head = response_head;
    }

    @XmlTransient
    public String getResponse_body() {
        return response_body;
    }

    public void setResponse_body(String response_body) {
        this.response_body = response_body;
    }

    public XmlResponse(String response_head, String response_body) {
        this.response_head = response_head;
        this.response_body = response_body;
    }

    public XmlResponse(){}

}
