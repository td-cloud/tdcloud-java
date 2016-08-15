package com.tangdi.tdcloud;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;


/**
 * TdCloud 支付宝MD5签名处理核心文件
 * 
 * @author deng_wk
 * @Date 2016-06-29
 * @version 1.0.0
 */

class MD5 {

    /**
     * 签名字符串
     *
     * @param text
     * 需要签名的字符串
     * @param key
     * 密钥
     * @param inputCharset
     * 编码格式
     * @return 签名结果
     */
    static String sign(String text, String key, String inputCharset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, inputCharset));
    }

    /**
     * 签名字符串
     *
     * @param text
     * 需要签名的字符串
     * @param sign
     * 签名结果
     * @param key
     * 密钥
     * @param inputCharset
     * 编码格式
     * @return 签名结果
     */
    static boolean verify(String text, String sign, String key, String inputCharset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, inputCharset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
