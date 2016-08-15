package com.tangdi.tdcloud.bean;

import java.util.Map;

import com.tangdi.tdcloud.TCEumeration.PAY_CHANNEL;
import com.tangdi.tdcloud.TCEumeration.QR_PAY_MODE;
import com.tangdi.tdcloud.TCEumeration.GATEWAY_BANK;


/**
 * 支付订单类，封装了TdCloud订单信息
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCOrder {

	/**
	 * 支付订单唯一标识
	 */
    private String id;

    /**
     * 渠道类型
     */
    private PAY_CHANNEL channel;

    /**
     * 订单总金额  单位为分
     */
    private Integer totalFee;

    /**
     * 商户订单号
     */
    private String billNo;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 微信扫码code url， 微信扫码支付下单成功时返回
     */
    private String codeUrl;

    /**
     * 支付提交html
     */
    private String html;

    /**
     * 支付跳转url
     */
    private String url;

    /**
     *  微信公众号支付要素，微信公众号支付下单成功时返回 
     */
    private Map<String, String> wxJSAPIMap;

    /**
     * 附加数据， 用户自定义的参数
     */
    private Map<String, Object> optional;

    /**
     * optional json字符串， 可通过查询获得
     */
    private String optionalString;

    /**
     *  同步返回页面  
     */
    private String returnUrl;

    /**
     * 订单失效时间  必须为非零正整数，单位为秒，建议最短失效时间间隔必须大于360秒 360 
     */
    private Integer billTimeout;

    /**
     * 用户相对于微信公众号的唯一id
     * 微信公众号支付(WX_JSAPI) 的必填
     */
    private String openId;

    /**
     * 商品展示地址
     * 支付宝网页支付(ALI_WEB) 的选填
     */
    private String showUrl;

    /**
     *  二维码类型
     *  支付宝内嵌二维码支付(ALI_QRCODE) 的必填
     */
    private QR_PAY_MODE qrPayMode;

    /**
     * 点卡卡号
     * 易宝点卡支付(YEE_NOBANKCARD) 的必填
     */
    private String cardNo;

    /**
     * 点卡密码
     * 易宝点卡支付(YEE_NOBANKCARD) 的必填
     */
    private String cardPwd;

    /**
     * 点卡类型编码
     * 易宝点卡支付(YEE_NOBANKCARD) 的必填
     */
    private String frqid;

    /**
     * 易宝移动网页（一键）支付用户唯一标识符
     * 易宝移动网页支付(YEE_WAP) 的必填
     */
    private String identityId;

    /**
     * 渠道交易号
     */
    private String channelTradeNo;

    /**
     * 订单是否成功
     */
    private boolean result;

    /**
     * 订单是否已经退款
     */
    private boolean refundResult;

    /**
     * 订单是否已经撤销
     */
    private boolean revertResult;

    /**
     * 渠道详细信息， 当need_detail传入true时返回
     */
    private String messageDetail = "不显示";

    /**
     * 订单创建时间
     */
    private String dateTime;

    /**
     * 订单付款完成时间
     */
    private String successTime;
    
    private GATEWAY_BANK gatewayBank;

    private String tcExpressCardNo;


    public TCOrder() {}

    /**
     * 构造函数，参数为发起支付的4个必填参数
     * 
     * @param channel
     * {@link #setChannel}
     * @param totalFee
     * {@link #setTotalFee}
     * @param billNo
     * {@link #setBillNo}
     * @param title
     * {@link #setTitle}
     */
    public TCOrder(PAY_CHANNEL channel, Integer totalFee, String billNo, String title) {
        this.channel = channel;
        this.totalFee = totalFee;
        this.billNo = billNo;
        this.title = title;
    }

    /**
     * 访问字段 {@link #channel}
     */
    public PAY_CHANNEL getChannel() {
        return channel;
    }

    /**
     * 访问字段 {@link #totalFee}
     */
    public Integer getTotalFee() {
        return totalFee;
    }

    /**
     * 访问字段 {@link #billNo}
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 访问字段 {@link #title}
     */
    public String getTitle() {
        return title;
    }

    /**
     * 访问字段 {@link #optional}
     */
    public Map<String, Object> getOptional() {
        return optional;
    }

    /**
     * @param optional
     * 附加数据， 用户自定义的参数，将会在webhook通知中原样返回，该字段主要用于商户携带订单的自定义数据 (选填)
     */
    public void setOptional(Map<String, Object> optional) {
        this.optional = optional;
    }

    /**
     * 访问字段 {@link #returnUrl}
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * @param returnUrl
     * 同步返回页面， 支付渠道处理完请求后,当前页面自动跳转到商户网站里指定页面的http路径， 当 channel 参数为 ALI_WEB 或
     * ALI_QRCODE 或 UN_WEB 或JD_WEB 或JD_WAP时为必填 (选填)
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * 访问字段 {@link #billTimeout}
     */
    public Integer getBillTimeout() {
        return billTimeout;
    }

    /**
     * @param billTimeout
     * 订单失效时间，单位秒，非零正整数，建议不小于360，快钱(KQ)不支持该参数 (选填)
     */
    public void setBillTimeout(Integer billTimeout) {
        this.billTimeout = billTimeout;
    }

    /**
     * 访问字段 {@link #openId}
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 访问字段 {@link #identityId}
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * @param openId
     * 微信公众号支付(WX_JSAPI)必填 (选填)
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @param identityId
     * 易宝快捷支付(YEE_WAP)必填 (选填)
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    /**
     * 访问字段 {@link #showUrl}
     */
    public String getShowUrl() {
        return showUrl;
    }

    /**
     * @param showUrl
     * 商品展示地址，需以http://开头的完整路径，例如：http://www.商户网址.com/myorder，（ALI_WEB)的选填参数
     * (选填)
     */
    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    /**
     * 访问字段 {@link #qrPayMode}
     */
    public QR_PAY_MODE getQrPayMode() {
        return qrPayMode;
    }

    /**
     * @param qrPayMode
     * 二维码类型，(ALI_QRCODE)的必填参数，二维码类型含义， {@link QR_PAY_MODE#MODE_BRIEF_FRONT}：
     * 订单码-简约前置模式, 对应 iframe 宽度不能小于 600px, 高度不能小于 300px
     * {@link QR_PAY_MODE#MODE_FRONT}： 订单码-前置模式, 对应 iframe 宽度不能小于 300px, 高度不能小于
     * 600px {@link QR_PAY_MODE#MODE_MINI_FRONT}, 对应 iframe 宽度不能小于 75px, 高度不能小于
     * 75px (选填)
     */
    public void setQrPayMode(QR_PAY_MODE qrPayMode) {
        this.qrPayMode = qrPayMode;
    }

    /**
     * 访问字段 {@link #cardNo}
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo
     * 点卡卡号，(YEE_NOBANKCARD)的必填参数，每种卡的要求不一样，例如易宝支持的QQ币卡号是9位的，江苏省内部的QQ币卡号是15位，
     * 易宝不支付 (选填)
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 访问字段 {@link #cardPwd}
     */
    public String getCardPwd() {
        return cardPwd;
    }

    /**
     * @param cardPwd
     * 点卡密码，简称卡密， (YEE_NOBANKCARD)的必填参数 (选填)
     */
    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    /**
     * 访问字段 {@link #frqid}
     */
    public String getFrqid() {
        return frqid;
    }

    /**
     * @param frqid
     * 点卡类型编码，(YEE_NOBANKCARD)的必填参数，包含： 骏网一卡通(JUNNET) 盛大卡(SNDACARD) 神州行(SZX)
     * 征途卡(ZHENGTU) Q币卡(QQCARD) 联通卡(UNICOM) 久游卡(JIUYOU) 易充卡(YICHONGCARD)
     * 网易卡(NETEASE) 完美卡(WANMEI) 搜狐卡(SOHU) 电信卡(TELECOM) 纵游一卡通(ZONGYOU)
     * 天下一卡通(TIANXIA) 天宏一卡通(TIANHONG) 一卡通(THIRTYTWOCARD)
     */
    public void setFrqid(String frqid) {
        this.frqid = frqid;
    }

    /**
     * @param channel
     * 渠道类型， 根据不同场景选择不同的支付方式，包含： {@link PAY_CHANNEL#WX_NATIVE}: 微信公众号二维码支付
     * {@link PAY_CHANNEL#WX_JSAPI}: 微信公众号支付 {@link PAY_CHANNEL#ALI_WEB}:
     * 支付宝网页支付 {@link PAY_CHANNEL#ALI_QRCODE}: 支付宝内嵌二维码支付
     * {@link PAY_CHANNEL#ALI_WAP}: 支付宝移动网页支付 {@link PAY_CHANNEL#UN_WEB}: 银联网页支付
     * {@link PAY_CHANNEL#JD_WAP}: 京东移动网页支付 {@link PAY_CHANNEL#JD_WEB}: 京东PC网页支付
     * {@link PAY_CHANNEL#YEE_WAP}: 易宝移动网页支付 {@link PAY_CHANNEL#YEE_WEB}:
     * 易宝PC网页支付 {@link PAY_CHANNEL#YEE_NOBANKCARD}: 易宝点卡支付
     * {@link PAY_CHANNEL#KUAIQIAN_WAP}: 快钱移动网页支付
     * {@link PAY_CHANNEL#KUAIQIAN_WEB}: 快钱PC网页支付 {@link PAY_CHANNEL#BD_WEB} :
     * 百度PC网页支付 {@link PAY_CHANNEL#BD_WAP}: 百度移动网页支付 (必填)
     */
    public void setChannel(PAY_CHANNEL channel) {
        this.channel = channel;
    }

    /**
     * @param totalFee
     * 订单总金额， 只能为整数，单位为分，例如 1 (必填)
     */
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * @param billNo
     * 商户订单号, 8到32个字符内，数字和/或字母组合，确保在商户系统中唯一, 例如（201506101035040000001） (必填)
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * @param title
     * 订单标题， 32个字节内，最长支持16个汉字 (必填)
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return 渠道交易号， 支付完成之后获得
     */
    public String getChannelTradeNo() {
        return channelTradeNo;
    }

    /**
     * @return 是否退款
     */
    public boolean isRefundResult() {
        return refundResult;
    }

    /**
     * @return 是否支付
     */
    public boolean isResult() {
        return result;
    }

    /**
     * 设置字段 {@link #result}
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * 设置字段 {@link #refundResult}
     */
    public void setRefundResult(boolean refundResult) {
        this.refundResult = refundResult;
    }

    /**
     * @return 订单是否撤销
     */
    public boolean isRevertResult() {
        return revertResult;
    }

    /**
     * 设置字段 {@link #revertResult}
     */
    public void setRevertResult(boolean revertResult) {
        this.revertResult = revertResult;
    }

    /**
     * @return 渠道信息
     */
    public String getMessageDetail() {
        return messageDetail;
    }

    /**
     * 设置字段 {@link #messageDetail}
     */
    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    /**
     * @return 订单唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置字段 {@link #objectId}
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return WX_NATIVE 二维码url
     */
    public String getCodeUrl() {
        return codeUrl;
    }

    /**
     * 设置字段 {@link #codeUrl}
     */
    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    /**
     * @return 支付提交html
     */
    public String getHtml() {
        return html;
    }

    /**
     * 设置字段 {@link #html}
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * @return 支付跳转url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置字段 {@link #url}
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return WX_JSAPI 支付要素
     */
    public Map<String, String> getWxJSAPIMap() {
        return wxJSAPIMap;
    }

    /**
     * 设置字段 {@link #wxJSAPIMap}
     */
    public void setWxJSAPIMap(Map<String, String> wxJSAPIMap) {
        this.wxJSAPIMap = wxJSAPIMap;
    }

    /**
     * @return 订单创建时间
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * 设置字段 {@link #dateTime}
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    

    public String getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}

	/**
     * 设置字段 {@link #channelTradeNo}
     */
    public void setChannelTradeNo(String channelTradeNo) {
        this.channelTradeNo = channelTradeNo;
    }

    /**
     * @return optional json字符串
     */
    public String getOptionalString() {
        return optionalString;
    }

    /**
     * 设置字段 {@link #optionalString}
     */
    public void setOptionalString(String optionalString) {
        this.optionalString = optionalString;
    }

    /**
     * 访问字段 {@link #gatewayBank}
     */
    public GATEWAY_BANK getGatewayBank() {
        return gatewayBank;
    }

    /**
     * @param gatewayBank（预留字段，暂未使用）
     * BeeCloud网关支付支持银行，包含： {@link GATEWAY_BANK#CMB}: 招商银行
     * {@link GATEWAY_BANK#ICBC}: 工商银行 {@link GATEWAY_BANK#CCB}:
     * 建设银行（暂时不支持） {@link GATEWAY_BANK#BOC}: 中国银行
     * {@link GATEWAY_BANK#ABC}: 农业银行 {@link GATEWAY_BANK#BOCM}: 交通银行
     * {@link GATEWAY_BANK#SPDB}: 浦发银行 {@link GATEWAY_BANK#GDB}: 广发银行
     * {@link GATEWAY_BANK#CITIC}: 中信银行 {@link GATEWAY_BANK#CEB}:
     * 光大银行 {@link GATEWAY_BANK#CIB}: 兴业银行
     * {@link GATEWAY_BANK#SDB}: 平安银行
     * {@link GATEWAY_BANK#CMBC}: 民生银行 (选填)，channel为BC_GATEWAY时必填
     */
    public void setGatewayBank(GATEWAY_BANK gatewayBank) {
        this.gatewayBank = gatewayBank;
    }

    /**
     * 访问字段 {@link #tcExpressCardNo}
     */
    public String getTcExpressCardNo() {
        return tcExpressCardNo;
    }

    /**
     * @param tcExpressCardNo
     * BC_EXPRESS指定卡号， 字符串 (选填)
     */
    public void setTcExpressCardNo(String tcExpressCardNo) {
        this.tcExpressCardNo = tcExpressCardNo;
    }
}
