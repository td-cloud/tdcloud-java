package com.tangdi.tdcloud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * TdCloud JAVA SDK缓存类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCCache {
	
    private static String appID = null;
    private static String appSecret = null;
    private static String testSecret = null;
    private static String masterKey = null;
    private static boolean sandbox = false;
    private static int networkTimeout = 500000;
    /**
     * TdClod 服务地址（域名）
     */
    static String apiHostArray = initApiHostArray();

    /**
     * 缓存 appSecret
     */
    static void setAppSecret(String appSecret) {
        TCCache.appSecret = appSecret;
    }
    
    /**
     * 缓存 appID
     */
    static void setAppID(String appID) {
        TCCache.appID = appID;
    }
    
    /**
     * 缓存 masterKey
     */
    static void setMasterKey(String masterKey) {
        TCCache.masterKey = masterKey;
    }

    static void setSandbox(boolean sandbox) {
        TCCache.sandbox = sandbox;
    }

    static void setTestSecret(String testSecret) {
        TCCache.testSecret = testSecret;
    }

    static boolean isSandbox() {
        return sandbox;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    public static String getTestSecret() {
        return testSecret;
    }

    public static String getAppID() {
        return appID;
    }
    
    static String getMasterKey() {
        return masterKey;
    }

    static int getNetworkTimeout() {
        return networkTimeout;
    }
    
    /**
     * 缓存 networkTimeout
     */
    static void setNetworkTimeout(int networkTimeout) {
        TCCache.networkTimeout = networkTimeout;
    }
    
    /**
     * 初始化rest api服务器
     */
    static String initApiHostArray()
    {
    	apiHostArray = "http://127.0.0.1:8080";
    	//apiHostArray = "http://124.193.113.122:48080";
    	InputStream inputStream;
    	/*
    	 * 如果类路径下存在配置文件conf.properties，读取其中的backend属性，作为后端连接服务器
    	 */
    	inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
    	if (inputStream != null) {
    		Properties prop = new Properties();
    		try {
				prop.load(inputStream);
				String host = prop.getProperty("backend");
				if (!host.trim().equals("")) {
					apiHostArray = "http://" + host + ":48080";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return apiHostArray;
    }
}
