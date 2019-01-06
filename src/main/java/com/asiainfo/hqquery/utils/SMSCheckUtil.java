package com.asiainfo.hqquery.utils;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author king-pan
 * @date 2019/1/5
 * @Description ${DESCRIPTION}
 */
public class SMSCheckUtil {
    public static String sendPost(String phoneNumber, String smsCheck) {
        String line = "";
        try {
            URL url = new URL("http://10.30.43.203:7211/ebus/httpService/InformationInquiryService_http/getCheckKeyQD?tuxservice=y");
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(con
                    .getOutputStream());
            String xmlInfo = getXmlInfo(phoneNumber, smsCheck);
            out.write(new String(xmlInfo.getBytes("utf-8")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream(), "UTF-8"));
            line = "";
            for (line = br.readLine(); line != null; ) {
                return line;
            }
            return line;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static String getCheckReturnStr(String phoneNumber, String smsCheck) {
        StringBuilder sb = new StringBuilder();
        sb.append("<getCheckKeyQD>");
        sb.append("	<head>");
        sb.append("		<accessType>bsacWeb</accessType>");
        sb.append("		<reqTime>");
        sb.append(DateTime.now().toString("yyyyMMddHHmmss"));
        sb.append("		</reqTime>");
        sb.append("		<reqSeq></reqSeq>");
        sb.append("		<routeType>1</routeType>");
        sb.append("		<telNum>");
        sb.append(phoneNumber);
        sb.append("		</telNum>");
        sb.append("		<region></region>");
        sb.append("		<unitID>bsacWeb</unitID>");
        sb.append("	</head>");
        sb.append("	<body>");
        sb.append("		<telNum>");
        sb.append(phoneNumber);
        sb.append("		</telNum>");
        sb.append("		<chkkey>");
        sb.append(smsCheck);
        sb.append("		</chkkey>");
        sb.append("	</body>");
        sb.append("</getCheckKeyQD>");
        return sb.toString();
    }

    public static String getXmlInfo(String phoneNumber, String smsCheck) {
        return getCheckReturnStr(phoneNumber, smsCheck);
    }

    public static String startMethod(String phoneNumber, String smsCheck) {
        return sendPost(phoneNumber, smsCheck);
    }
}
