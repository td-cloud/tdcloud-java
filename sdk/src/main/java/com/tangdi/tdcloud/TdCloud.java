package com.tangdi.tdcloud;

/**
 * TdCloud 初始化类
 * 
 * @author deng_wk
 * @Date 2016-07-01
 */
public class TdCloud {

    /**
     * TdCloud JAVA SDK release version.
     */
    public static final String kTdCloudVersionString = "1.00";
    public static final double kTdCloudVersionNumber = 1.00;

    /**
     * 设置appID、appSecret、masterSecret，在程序开始时运行一遍即可
     * 
     * @param appID
     * （必填）商户在Beecloud后台注册的app id
     * @param appSecret
     * （必填）用于支付、查询
     * @param masterSecret
     * （必填）用于退款、批量打款
     */
    public static void registerApp(String appID, String testSecret, String appSecret,
            String masterSecret) {

        TCCache.setAppID(appID);
        TCCache.setAppSecret(appSecret);
        TCCache.setMasterKey(masterSecret);
        TCCache.setTestSecret(testSecret);
    }

    /**
     * 设置网络超时时间, 单位ms, 默认5000
     *
     * @param timeout
     * 访问TdCloud的超时时间
     */
    public static void setNetworkTimeout(int timeout) {
        TCCache.setNetworkTimeout(timeout);
    }

    /**
     * 设置sandbox属性为true，开启测试模式 
     * 测试模式时：        {@linkplain com.tangdi.tdcloud.TdCloud registerApp.registerApp} 参数appSecret、masterSecret可为null <br/>
     * 生产模式时：{@linkplain com.tangdi.tdcloud.TdCloud registerApp.registerApp} 参数testSecret可为null 
     * @param sandbox
     */
    public static void setSandbox(boolean sandbox) {
        TCCache.setSandbox(sandbox);
    }
}
