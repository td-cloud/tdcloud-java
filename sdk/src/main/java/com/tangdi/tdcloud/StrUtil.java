package com.tangdi.tdcloud;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


/**
 * TdCloud 字符串相关处理类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 * 
 */
class StrUtil {

    private static String hexString = "0123456789ABCDEF ";

    static String toStr(Object obj) {
        if (obj == null)
            return "";
        else
            return obj.toString();
    }

    static boolean empty(String s) {
        boolean rtn = false;
        if (s == null || s.length() == 0)
            rtn = true;
        else if (s.trim().length() == 0)
            rtn = true;
        return rtn;
    }

    static boolean empty(Object s) {
        if (s == null || s.toString().trim().equals(""))
            return true;
        return false;
    }

    static int parseInt(String s) {
        return parseInt(s, 0);
    }

    static int ceilDivide(long a, long b) {
        long c = a % b;
        return (int) (a / b + (c > 0 ? 1 : 0));
    }

    @SuppressWarnings("rawtypes")
	static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    static int parseInt(String s, int iDefault) {
        if (s == null || s.equals(""))
            return iDefault;
        if (s.equals("true"))
            return 1;
        if (s.equals("false"))
            return 0;
        try {
            s = s.replaceAll(",", "");
            int l = s.indexOf(".");
            if (l > 0)
                s = s.substring(0, l);
            return Integer.parseInt(s);
        } catch (Exception e) {
            return iDefault;
        }
    }

    static long parseLong(String s) {
        return parseLong(s, 0L);
    }

    static long parseLong(String s, long iDefault) {
        if (s == null || s.equals(""))
            return iDefault;
        try {
            s = s.replaceAll(",", "");
            int l = s.indexOf(".");
            if (l > 0)
                s = s.substring(0, l);
            return Long.parseLong(s);
        } catch (Exception e) {
            return iDefault;
        }
    }

    static int len(String src) {
        int len = 0;
        try {
            byte[] bb = src.getBytes("GBK");
            len = bb.length;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
        }
        return len;
    }

    static String encode(String str) {
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    static String decode(String bytes, String code) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes
                    .charAt(i + 1))));
        String s = "";
        try {
            s = new String(baos.toByteArray(), code);
        } catch (UnsupportedEncodingException e) {}
        return s;
    }

    static String date2String(String pattern, Date date) {
        String retStr = "";
        java.text.SimpleDateFormat ff = new java.text.SimpleDateFormat();
        ff.applyPattern(pattern);
        retStr = ff.format(date);
        return retStr;
    }

    static Date string2Date(String pattern, String str) {
        Date date = new Date();
        if (empty(str)) {
            return date;
        }
        java.text.SimpleDateFormat ff = new java.text.SimpleDateFormat();
        ff.applyPattern(pattern);
        try {
            date = ff.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dd = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dd == 6 || dd == 0)
            return true;
        return false;
    }

    static Float parseFloat(String s) {
        return parseFloat(s, 0.0f);
    }

    static Float parseFloat(String s, Float defaultValue) {
        if (s == null || s == "") {
            return defaultValue;
        }
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static long parseIpV4ToLong(String ip) {
        if (StrUtil.empty(ip)) {
            return 0;
        }
        String[] ips = ip.split("\\.");
        if (ips.length != 4) {
            return 0;
        }

        return (parseLong(ips[0]) << 24) + (parseLong(ips[1]) << 16) + (parseLong(ips[2]) << 8)
                + parseLong(ips[3]);
    }
    
    /**
     * 单位缩进字符串。
     */
    private static String SPACE = "   ";
    
    /**
     * 返回格式化JSON字符串。
     * 
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public static String formatJson(String json)
    {
        StringBuffer result = new StringBuffer();
        
        int length = json.length();
        int number = 0;
        char key = 0;
        
        //遍历输入字符串。
        for (int i = 0; i < length; i++)
        {
            //1、获取当前字符。
            key = json.charAt(i);
            
            //2、如果当前字符是前方括号、前花括号做如下处理：
            if((key == '[') || (key == '{') )
            {
                //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if((i - 1 > 0) && (json.charAt(i - 1) == ':'))
                {
                    result.append('\n');
                    result.append(indent(number));
                }
                
                //（2）打印：当前字符。
                result.append(key);
                
                //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');
                
                //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));
                
                //（5）进行下一次循环。
                continue;
            }
            
            //3、如果当前字符是后方括号、后花括号做如下处理：
            if((key == ']') || (key == '}') )
            {
                //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');
                
                //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));
                
                //（3）打印：当前字符。
                result.append(key);
                
                //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if(((i + 1) < length) && (json.charAt(i + 1) != ','))
                {
                    result.append('\n');
                }
                
                //（5）继续下一次循环。
                continue;
            }
            
            //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if((key == ','))
            {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }
            
            //5、打印：当前字符。
            result.append(key);
        }
        
        return result.toString();
    }
    
    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     * 
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number)
    {
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < number; i++)
        {
            result.append(SPACE);
        }
        return result.toString();
    }
}
