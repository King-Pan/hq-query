package com.asiainfo.hqquery;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description ${DESCRIPTION}
 */
public class FastJsonTest {


    @Test
    public void removeTest(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a","a");
        jsonObject.put("n","n");

        jsonObject.put("c","c");
        jsonObject.remove("c");
        System.out.println(jsonObject.toJSONString());
    }
}
