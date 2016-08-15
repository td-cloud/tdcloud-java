package com.tangdi.tdcloud;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * TdCloud 路径、秘钥工具类
 * 
 * @author deng_wk
 * @Date 2016-07-01
 */
class TCUtilPrivate {

    static final String kApiVersion = "tdcloud-api";

    static String getAppSignature(String timeStamp,String other) {
        String str = TCCache.getAppID() + timeStamp + TCCache.getAppSecret()+other;
        return getMessageDigest(str);
    }

    static String getAppSignature() {
        String str = TCCache.getAppID() + TCCache.getAppSecret();
        return getMessageDigest(str);
    }
    
    static String getAppSignatureWithMasterSecret(String timeStamp,String other) {
        String str = TCCache.getAppID() + timeStamp + TCCache.getMasterKey()+other;
        return getMessageDigest(str);
    }

    static String getAppSignatureWithTestSecret(String timeStamp,String other) {
        String str = TCCache.getAppID() + timeStamp + TCCache.getTestSecret()+other;
        return getMessageDigest(str);
    }

    static String getMessageDigest(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f' };
        try {
            byte[] buffer = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            mdTemp.update(buffer);

            // 获得密文
            byte[] md = mdTemp.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    static String getkApiPay() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/bill";
    }

    static String getkApiRefund() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/refund";
    }

    static String getkApiQueryPay() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/queryPay?para=";
    }
    static String getkApiQueryPays() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/queryPays?para=";
    }
    
    static String getkApiQueryRefund() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/queryRefund?para=";
    }
    
    static String getkApiQueryRefunds() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/queryRefunds?para=";
    }

    /* 沙箱部分api */

    static String getkSandboxApiPay() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/sandbox/bill";
    }

    static String getkApiSandboxQueryPays() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/sandbox/queryPays?para=";
    }

    static String getkApiSandboxQueryPay() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/sandbox/queryPay?para=";
    }

    static String getkApiSandboxNotify() {
        return TCCache.apiHostArray + "/" + TCUtilPrivate.kApiVersion
                + "/rest/sandbox/notify";
    }

    static String transferDateFromToString(String strdate) {
        Date date = null;
        SimpleDateFormat sdf =null;
        if(strdate.trim().length()==14){
        	sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        }else if(strdate.trim().length()==17){
        	sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }else{
        	return strdate;
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			date = sdf.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return sdf2.format(date);
    }
}
