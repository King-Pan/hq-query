package com.asiainfo.hqquery.entity;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description 相应对象
 */
public class Response {
    private ResponseHead head;
    private Object body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response [head=" + head + ", body=" + body + "]";
    }

}
