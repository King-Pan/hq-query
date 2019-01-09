package com.asiainfo.hqquery.entity;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description ${DESCRIPTION}
 */
public class XmlBody {

    private String groupValueName;

    private String acceFlow;

    public XmlBody(){}

    public XmlBody(String groupValueName,String acceFlow){
        this.groupValueName = groupValueName;
        this.acceFlow = acceFlow;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<data>");
        sb.append("<GroupValueName>").append(groupValueName).append("</GroupValueName>");
        sb.append("<AcceFlow>").append(acceFlow).append("</AcceFlow>");
        sb.append("</data>");
        return super.toString();
    }
}
