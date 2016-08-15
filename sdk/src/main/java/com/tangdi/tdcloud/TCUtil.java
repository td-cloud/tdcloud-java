package com.tangdi.tdcloud;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;
import java.util.UUID;





/**
 * TdCloud 工具类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCUtil {
	
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateRandomUUIDPure() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Integer generateNumberWith3to24digitals() {
        Random random = new Random();
        Integer intRandom;
        double ran = Math.random();
        if (ran <= 0.17) { // 3 λ
            intRandom = 100 + random.nextInt(900);
        } else if ((ran > 0.17) && (ran <= 0.34)) { // 4 λ
            intRandom = 1000 + random.nextInt(9000);
        } else if ((ran > 0.34) && (ran <= 0.51)) { // 5 λ
            intRandom = 10000 + random.nextInt(90000);
        } else if ((ran > 0.51) && (ran <= 0.68)) { // 6 λ
            intRandom = 100000 + random.nextInt(900000);
        } else if ((ran > 0.68) && (ran <= 0.85)) { // 7 λ
            intRandom = 1000000 + random.nextInt(9000000);
        } else { // 8λ
            intRandom = 10000000 + random.nextInt(90000000);
        }
        return intRandom;
    }

    /**
     * 生成mock html
     */
    public static String generateSandboxHtmlWithUrl(String url) {
        StringBuffer mockHtml = new StringBuffer();
        String[] urlString = url.split("\\?");
        String urlSection = urlString[0];
        String[] paraSection = urlString[1].split("&");

        mockHtml.append("<form id=\"tdcloudmock\" name=\"tdcloudmock\" action=\"" + urlSection
                + "\" method=\"get\">");

        for (int i = 0; i < paraSection.length; i++) {
            String name = paraSection[i].split("=")[0];
            String value = paraSection[i].split("=")[1];

            try {
                mockHtml.append(
                        "<input type=\"hidden\" name=\"" + name + "\" value=\"" + URLDecoder.decode(value, "UTF-8") + "\"/>");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // submit按钮控件请不要含有name属性
        mockHtml.append("<input type=\"submit\" value=\"确认\" style=\"display:none;\"></form>");
        mockHtml.append("<script>document.forms['tdcloudmock'].submit();</script>");
        return mockHtml.toString();
    }
    
    
}
