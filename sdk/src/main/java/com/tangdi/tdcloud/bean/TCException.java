package com.tangdi.tdcloud.bean;

/**
 * TdCloud自定义异常，该异常信息包含错误码，错误基本信息、错误详细信息
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCException extends Exception {

    private static final long serialVersionUID = 1L;

    public TCException(String result_code, String result_type, String error_detail) {
        super("{\"result_code\":\"" +  result_code+ "\",\"result_type\":\"" + result_type + "\",\"error_detail\":\""+error_detail+"\"}");
    }

    public TCException(String msg) {
        super(msg);
    }
}
