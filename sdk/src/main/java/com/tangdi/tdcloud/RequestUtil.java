package com.tangdi.tdcloud;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import net.sf.json.JSONObject;

/**
 * 通讯公共处理类
 * @author deng_wk
 * @Date 2016-06-29
 * @version 1.0.0
 */
public class RequestUtil {
	
    public enum REQUEST_TYPE {
        POST,
        PUT,
        GET
    }

    /**
     * doPost方法，封装rest api POST方式请求
     *
     * @param requestUrl
     * 请求url
     * @param param
     * 请求参数
     * @return rest api返回参数
     * @throws TCException
     */
    public static Map<String, Object> doPost(String requestUrl, Map<String, Object> param) throws TCException {
        return request(requestUrl, param, REQUEST_TYPE.POST);
    }

    /**
     * doPut方法，封装rest api PUT方式请求
     *
     * @param requestUrl
     * 请求url
     * @param param
     * 请求参数
     * @return rest api返回参数
     * @throws TCException
     */
    public static Map<String, Object> doPut(String requestUrl, Map<String, Object> param) throws TCException {
        return request(requestUrl, param, REQUEST_TYPE.PUT);
    }

    /**
     * doGet方法，封装rest api GET方式请求
     *
     * @param requestUrl
     * 请求url
     * @param param
     * 请求参数
     * @return rest api返回参数
     * @throws TCException
     */
    public static Map<String, Object> doGet(String requestUrl, Map<String, Object> param) throws TCException {
        return request(requestUrl, param, REQUEST_TYPE.GET);
    }

    /***
     *
     * @param requestUrl
     * @param param
     * @param request_type
     * @return
     * @throws TCException
     */
    public static Map<String, Object> request(String requestUrl, Map<String, Object> param, REQUEST_TYPE request_type)
            throws TCException {
        HttpURLConnection connection = null;
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        Integer reponseStatus;
        try {
            if (request_type == REQUEST_TYPE.GET) {
                if(param!=null){requestUrl = requestUrl + URLEncoder.encode(JSONObject.fromObject(param).toString(), "UTF-8");}
                System.out.println("请求url="+requestUrl);
            }
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request_type.name());
            connection.setRequestProperty("Content-Type"  , "application/json");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType"   , "utf-8");
            connection.setRequestProperty("Charset"       , "utf-8");

            connection.setReadTimeout(20000);
            connection.setConnectTimeout(20000);
            connection.setDoInput(true);

            // POST || PUT
            if (request_type == REQUEST_TYPE.PUT || request_type == REQUEST_TYPE.POST) {
                connection.setDoOutput(true);
                // Send request
                // 获取URLConnection对象对应的输出流
                out= new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
                // 发送请求参数
                //String text = new String(JSONObject.fromObject(param).toString().getBytes(),"UTF-8");
                out.print(JSONObject.fromObject(param));
                // flush输出流的缓冲
                out.flush();
            }

            reponseStatus = connection.getResponseCode();
            if(reponseStatus !=200){
            	throw new TCException(TCErrorCode.e_120002.code(), RESULT_TYPE.COMMUNICATION_ERROR.name(), TCErrorCode.e_120002.error());
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            JSONObject jsonObject = JSONObject.fromObject(StrUtil.toStr(result));
            String resultCode = jsonObject.getString("result_code");
            String resultMessage = jsonObject.getString("result_type");
            String errorDetail = jsonObject.getString("error_detail");
            if (TCErrorCode.e_000000.code().equals(resultCode)) {
                return jsonToMap(jsonObject);
            } else {
                throw new TCException(resultCode, resultMessage, errorDetail);
            }
        } catch (TCException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new TCException(TCErrorCode.e_120005.code(), RESULT_TYPE.COMMUNICATION_ERROR.name(), e.getMessage());
        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new TCException(TCErrorCode.e_120001.code(), RESULT_TYPE.COMMUNICATION_ERROR.name(), e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new TCException(TCErrorCode.e_120004.code(), RESULT_TYPE.COMMUNICATION_ERROR.name(), e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new TCException(TCErrorCode.e_120001.code(), RESULT_TYPE.COMMUNICATION_ERROR.name(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TCException(TCErrorCode.e_120001.code(), RESULT_TYPE.OTHER_ERROR.name(), e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Object key : json.keySet()) {
            resultMap.put(StrUtil.toStr(key), json.get(key));
        }
        return resultMap;
    }
}
