/**
 * *************************
 *
 * @Date: Sep 18, 2015
 * @Time: 2:54:12 PM
 * @Author: Joseph Gao
 * <p/>
 * **************************
 */
package com.tangdi.tdcloud;

import com.tangdi.tdcloud.TCEumeration.API_NAME;
import com.tangdi.tdcloud.TCEumeration.PAY_CHANNEL;
import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCOrder;
import com.tangdi.tdcloud.bean.TCOrders;
import com.tangdi.tdcloud.bean.TCQueryParameter;
import com.tangdi.tdcloud.bean.TCRefund;
import com.tangdi.tdcloud.bean.TCRefunds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * TdCloud 主要为了一个商户下有多个收款APP使用，参考@TCPay
 *
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCPayMultiApp {

    private String appId;
    private String appSecret;
    private String testSecret;
    private String masterSecret;

    public TCPayMultiApp(String appId, String testSecret, String appSecret, String masterSecret) {
        this.appId = appId;
        this.testSecret = testSecret;
        this.appSecret = appSecret;
        this.masterSecret = masterSecret;
    }

    /**
     * 支付接口
     *
     * @param order {@link TCOrder} (必填) 支付参数
     * @return 调起BeeCloud支付后的返回结果
     * @throws TCException
     */
    public TCOrder startTCPay(TCOrder order) throws TCException {

    	ValidationUtil.validateTCPay(order);

        Map<String, Object> param = new HashMap<String, Object>();

        buildPayParam(param, order);

        if (TCCache.isSandbox()) {
            if (order.getChannel().equals(PAY_CHANNEL.WX_JSAPI)) {
                throw new TCException(TCErrorCode.e_105001.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105001.error());
            }
            Map<String, Object> ret = RequestUtil.doPost(TCUtilPrivate.getkSandboxApiPay(), param);
            placeSandboxOrder(order, ret);
            // 易宝点卡支付代码调用回调
            if (order.getChannel().equals(PAY_CHANNEL.YEE_NOBANKCARD)) {
            	RequestUtil.doGet(TCUtilPrivate.getkApiSandboxNotify() + "/" + this.appId + "/"
                        + order.getId() + "?para=", new HashMap<String, Object>());
            }
            return order;
        }
        Map<String, Object> ret = RequestUtil.doPost(TCUtilPrivate.getkApiPay(), param);

        placeOrder(order, ret);

        return order;
    }

    /**
     * 退款接口
     *
     * @param refund {@link TCRefund} （必填） 退款参数
     * @return 发起退款的返回结果
     * @throws TCException
     */
    public TCRefund startTCRefund(TCRefund refund) throws TCException {

        checkTestModeSwitch();

        ValidationUtil.validateTCRefund(refund);

        Map<String, Object> param = new HashMap<String, Object>();

        buildRefundParam(param, refund);

        Map<String, Object> ret = RequestUtil.doPost(TCUtilPrivate.getkApiRefund(), param);

        refund.setId(StrUtil.toStr(ret.get("id")));
        if (ret.containsKey("html")) {
            refund.setAliRefundHtml(StrUtil.toStr(ret.get("html")));
        }

        return refund;
    }

    /**
     * 订单查询（批量）接口
     *
     * @param para {@link TCQueryParameter} （必填） 订单查询参数
     * @return 订单查询返回的结果
     * @throws TCException
     */
    public TCOrders startQueryPays(TCQueryParameter para) throws TCException {

        ValidationUtil.validateQueryPays(para);

        Map<String, Object> param = new HashMap<String, Object>();

        buildQueryParam(param, para,API_NAME.QUERYPAYS);
        
        if (TCCache.isSandbox()) {
            Map<String, Object> ret = RequestUtil.doGet(TCUtilPrivate.getkApiSandboxQueryPays(), param);
            return generateTCOrderList(ret);
        }
        Map<String, Object> ret = RequestUtil.doGet(TCUtilPrivate.getkApiQueryPays(), param);

        return generateTCOrderList(ret);

    }

    /**
     * 订单查询（单笔，根据id）接口
     *
     * @param objectId （必填） 订单记录唯一标识
     * @return id查询返回结果
     * @throws TCException
     */
    @SuppressWarnings("unchecked")
	public TCOrder startQueryPay(TCQueryParameter para) throws TCException {

     	 ValidationUtil.validateQuerySingle(para);
    	 Map<String, Object> param = new HashMap<String, Object>();
         buildQueryParam(param, para, API_NAME.QUERYPAY);
         String url = TCCache.isSandbox()?TCUtilPrivate.getkApiSandboxQueryPay():TCUtilPrivate.getkApiQueryPay();
         
         Map<String, Object> ret = RequestUtil.doGet(url, param);
         
         return generateTCOrder((Map<String, Object>) ret.get("pay"));
    }

    

    /**
     * 退款记录查询（批量）接口
     *
     * @param para {@link TCQueryParameter} （必填）订单查询参数
     * @return 退款查询返回的结果
     * @throws TCException
     */
	public TCRefunds startQueryRefunds(TCQueryParameter para) throws TCException {

        checkTestModeSwitch();

        ValidationUtil.validateQueryRefunds(para);

        Map<String, Object> param = new HashMap<String, Object>();

        buildQueryParam(param, para,API_NAME.QUERYREFUNDS);

        Map<String, Object> ret = RequestUtil.doGet(TCUtilPrivate.getkApiQueryRefunds(), param);

        return generateTCRefundList(ret);
    }

    /**
     * 退款查询接口（根据 id）
     *
     * @param objectId (必填) 退款记录唯一标识
     * @return 单笔退款记录查询返回结果
     * @throws TCException
     */
    @SuppressWarnings("unchecked")
	public TCRefund startQueryRefund(TCQueryParameter para) throws TCException {

        checkTestModeSwitch();

        ValidationUtil.validateRefundQuerySingle(para);
        Map<String, Object> param = new HashMap<String, Object>();
        buildQueryParam(param, para,API_NAME.QUERYREFUND);
        
        Map<String, Object> ret = RequestUtil.doGet(TCUtilPrivate.getkApiQueryRefund(), param);
        return generateTCRefund((Map<String, Object>) ret.get("refund"));

    }

    /**
     * @param sign      Webhook提供的签名
     * @param timestamp Webhook提供的timestamp，注意是String格式
     * @return 签名是否正确
     */
    public  boolean verifySign(String sign, String timestamp) {
        String mySign = MD5.sign(this.appId + this.appSecret, timestamp, "UTF-8");

        if (sign.equals(mySign))
            return true;
        else
            return false;
    }

    /**
     * 构建支付rest api参数
     */
    private void buildPayParam(Map<String, Object> param, TCOrder para) {

        param.put("app_id", this.appId);
        param.put("timestamp", System.currentTimeMillis());
        String other=StrUtil.toStr(para.getBillNo())+StrUtil.toStr(para.getTotalFee());    // 根据实际业务需要定义签名字段
        if (TCCache.isSandbox()) {
            param.put("app_sign",
                    this.getAppSignatureWithTestSecret(StrUtil.toStr(param.get("timestamp")),other));
        } else {
            param.put("app_sign", this.getAppSignature(StrUtil.toStr(param.get("timestamp")),other));
        }
        param.put("channel", StrUtil.toStr(para.getChannel()));
        param.put("total_fee", para.getTotalFee());
        param.put("bill_no", para.getBillNo());
        param.put("title", para.getTitle());

        if (para.getReturnUrl() != null) {
            param.put("return_url", para.getReturnUrl());
        }
        if (para.getOptional() != null && para.getOptional().size() > 0) {
            param.put("optional", para.getOptional());
        }
        if (para.getOpenId() != null) {
            param.put("openid", para.getOpenId());
        }
        if (para.getIdentityId() != null) {
            param.put("identity_id", para.getIdentityId());
        }
        if (para.getShowUrl() != null) {
            param.put("show_url", para.getShowUrl());
        }
        if (para.getQrPayMode() != null) {
            if (para.getQrPayMode().ordinal() == 2) {
                param.put("qr_pay_mode", String.valueOf(para.getQrPayMode().ordinal() + 1));
            } else {
                param.put("qr_pay_mode", String.valueOf(para.getQrPayMode().ordinal()));
            }
        }
        if (para.getBillTimeout() != null) {
            param.put("bill_timeout", para.getBillTimeout());
        }
        if (para.getChannel().equals(PAY_CHANNEL.YEE_NOBANKCARD)) {
            param.put("cardno", para.getCardNo());
            param.put("cardpwd", para.getCardPwd());
            param.put("frqid", para.getFrqid());
        }
        if (para.getGatewayBank() != null) {
            param.put("bank", StrUtil.toStr(para.getGatewayBank()));
        }
    }

    private Object getAppSignatureWithTestSecret(String timestamp,String other) {
        String str = appId + timestamp + testSecret+other;
        return TCUtilPrivate.getMessageDigest(str);
    }

    /**
     * 构建退款rest api参数
     */
    private void buildRefundParam(Map<String, Object> param, TCRefund para) {

        param.put("app_id", this.appId);
        param.put("timestamp", System.currentTimeMillis());
        param.put("app_sign",
                this.getAppSignatureWithMasterSecret(StrUtil.toStr(param.get("timestamp"))));
        param.put("refund_no", para.getRefundNo());
        param.put("bill_no", para.getBillNo());
        param.put("refund_fee", para.getRefundFee());

        if (para.getChannel() != null) {
            param.put("channel", StrUtil.toStr(para.getChannel()));
        }
        if (para.getOptional() != null && para.getOptional().size() > 0)
            param.put("optional", para.getOptional());
    }

    /**
     * 构建查询rest api参数
     */
    private void buildQueryParam(Map<String, Object> param, TCQueryParameter para,API_NAME apiName) {
        param.put("app_id", this.appId);
        param.put("timestamp", System.currentTimeMillis());
        
        //业务参数
        String other="";
        
        switch (apiName) {
	        case QUERYPAY:
	        	other=StrUtil.toStr(para.getBillNo())+StrUtil.toStr(para.getId());
	        	break;
	        case QUERYPAYS:
	        	break;
	        case QUERYREFUND:
	        	break;
	        case QUERYREFUNDS:
	        	break;
	        default:
	            break;
        }
        
        if (TCCache.isSandbox()) {
            param.put("app_sign",
                    this.getAppSignatureWithTestSecret(StrUtil.toStr(param.get("timestamp")),other));
        } else {
            param.put("app_sign", this.getAppSignature(StrUtil.toStr(param.get("timestamp")),other));
        }
        if (para.getChannel() != null) {
            param.put("channel", StrUtil.toStr(para.getChannel()));
        }
        if (para.getBillNo() != null) {
            param.put("bill_no", para.getBillNo());
        }
        if (para.getRefundNo() != null) {
            param.put("refund_no", para.getRefundNo());
        }
        if (para.getPages() != null) {
            param.put("pages", para.getPages());
        }
        if (para.getNumber() != null) {
            param.put("number", para.getNumber());
        }
        if (para.getStartTime() != null) {
            param.put("start_time", para.getStartTime());
        }
        if (para.getEndTime() != null) {
            param.put("end_time", para.getEndTime());
        }
        if (para.getPayResult() != null) {
            param.put("spay_result", para.getPayResult());
        }
        if (para.getRefundResult() != null) {
            param.put("refund_result", para.getRefundResult());
        }
        if (para.getNeedDetail() != null && para.getNeedDetail()) {
            param.put("need_detail", para.getNeedDetail());
        }
        if (para.getNeedApproval() != null && para.getNeedApproval()) {
            param.put("need_approval", para.getNeedApproval());
        }
    }

    /**
     * 生成返回TCOrder list
     */
    private static List<TCOrder> generateTCOrderList(List<Map<String, Object>> bills) {

        List<TCOrder> tcOrderList = new ArrayList<TCOrder>();
        for (Map<String, Object> bill : bills) {
            TCOrder tcOrder = new TCOrder();
            generateTCOrderBean(bill, tcOrder);
            tcOrderList.add(tcOrder);
        }
        return tcOrderList;
    }

    /**
     * 生成返回TCOrder
     */
    private static TCOrder generateTCOrder(Map<String, Object> bill) {
        TCOrder tcOrder = new TCOrder();
        generateTCOrderBean(bill, tcOrder);
        return tcOrder;
    }

    /**
     * 生成返回TCRefund list
     */
    private static List<TCRefund> generateTCRefundList(List<Map<String, Object>> refundList) {

        List<TCRefund> tcRefundList = new ArrayList<TCRefund>();
        for (Map<String, Object> refund : refundList) {
            TCRefund tcRefund = new TCRefund();
            generateTCRefundBean(refund, tcRefund);
            tcRefundList.add(tcRefund);
        }
        return tcRefundList;
    }

    /**
     * 生成返回TCRefund
     */
    private static TCRefund generateTCRefund(Map<String, Object> refund) {
        TCRefund tcRefund = new TCRefund();
        generateTCRefundBean(refund, tcRefund);
        return tcRefund;
    }

    /**
     * 构建返回TCOrder bean
     */
    private static void generateTCOrderBean(Map<String, Object> bill, TCOrder tcOrder) {
        tcOrder.setId(StrUtil.toStr(bill.get("id")));
        tcOrder.setBillNo(StrUtil.toStr(bill.get("bill_no")));
        tcOrder.setTotalFee((Integer) bill.get("total_fee"));
        tcOrder.setTitle(StrUtil.toStr(bill.get("title")));
        tcOrder.setChannel(PAY_CHANNEL.valueOf(StrUtil.toStr(bill.get("sub_channel"))));
        tcOrder.setResult(((Boolean) bill.get("spay_result")));
        if (bill.containsKey("trade_no") && bill.get("trade_no") != null) {
            tcOrder.setChannelTradeNo(StrUtil.toStr(bill.get("trade_no")));
        }
        tcOrder.setOptionalString((StrUtil.toStr(bill.get("optional"))));
        tcOrder.setDateTime(
        		 TCUtilPrivate.transferDateFromToString(StrUtil.toStr(bill.get("create_time"))));
        if (bill.containsKey("message_detail")) {
            tcOrder.setMessageDetail(StrUtil.toStr(bill.get("message_detail")));
        }
        tcOrder.setRefundResult((Boolean) bill.get("refund_result"));
        tcOrder.setRevertResult((Boolean) bill.get("revert_result"));
    }

    /**
     * 构建返回TCRefund bean
     */
    private static void generateTCRefundBean(Map<String, Object> refund, TCRefund tcRefund) {
        tcRefund.setId(StrUtil.toStr(refund.get("id")));
        tcRefund.setBillNo(StrUtil.toStr(refund.get("bill_no")));
        tcRefund.setChannel(PAY_CHANNEL.valueOf(StrUtil.toStr(refund.get("sub_channel"))));
        tcRefund.setDateTime(
                TCUtilPrivate.transferDateFromToString(StrUtil.toStr(refund.get("create_time"))));
        tcRefund.setOptionalString(StrUtil.toStr(refund.get("optional")));
        tcRefund.setRefunded((Boolean) refund.get("result"));
        tcRefund.setTitle(StrUtil.toStr(refund.get("title")));
        tcRefund.setTotalFee((Integer) refund.get("total_fee"));
        tcRefund.setRefundFee((Integer) refund.get("refund_fee"));
        tcRefund.setRefundNo(StrUtil.toStr(refund.get("refund_no")));
        tcRefund.setDateTime(
                TCUtilPrivate.transferDateFromToString(StrUtil.toStr(refund.get("create_time"))));
        if (refund.containsKey("message_detail")) {
            tcRefund.setMessageDetail(StrUtil.toStr(refund.get("message_detail")));
        }
    }

    /**
     * 构建WXJSAPI返回Map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<String, String> generateWXJSAPIMap(Map<String, Object> ret) {
        HashMap map = new HashMap<String, Object>();
        map.put("appId", ret.get("app_id"));
        map.put("package", ret.get("package"));
        map.put("nonceStr", ret.get("nonce_str"));
        map.put("timeStamp", ret.get("timestamp"));
        map.put("paySign", ret.get("pay_sign"));
        map.put("signType", ret.get("sign_type"));

        return map;
    }
    
    /**
     * 生成返回TCOrder list
     */
    private static TCOrders generateTCOrderList(Map<String, Object> ret) {
    	TCOrders orders = new TCOrders();
    	orders.setTotal((Integer)ret.get("total"));
    	List<Map<String, Object>> bills= (List<Map<String, Object>>)ret.get("pays");
        List<TCOrder> tcOrderList = new ArrayList<TCOrder>();
        for (Map<String, Object> bill : bills) {
            TCOrder tcOrder = new TCOrder();
            generateTCOrderBean(bill, tcOrder);
            tcOrderList.add(tcOrder);
        }
        orders.setOrders(tcOrderList);
        return orders;
    }
    /**
     * 生成返回TCRefund list
     */
    private static TCRefunds generateTCRefundList(Map<String, Object> ret) {

    	TCRefunds  orders = new TCRefunds();
    	orders.setTotal((Integer)ret.get("total"));
    	List<Map<String, Object>> bills= (List<Map<String, Object>>)ret.get("refunds");
        List<TCRefund> tcRefundList = new ArrayList<TCRefund>();
        
        for (Map<String, Object> bill : bills) {
        	TCRefund tcRefund = new TCRefund();
            generateTCRefundBean(bill, tcRefund);
            tcRefundList.add(tcRefund);
        }
        orders.setOrders(tcRefundList);
        return orders;
    }
    
    /**
     * 组建返回订单
     */
    private static void placeOrder(TCOrder order, Map<String, Object> ret) {
        order.setId(StrUtil.toStr(ret.get("id")));
        switch (order.getChannel()) {
            case WX_NATIVE:
                if (ret.containsKey("code_url") && null != ret.get("code_url")) {
                    order.setCodeUrl(StrUtil.toStr(ret.get("code_url")));
                }
                break;
            case WX_JSAPI:
                order.setWxJSAPIMap(generateWXJSAPIMap(ret));
                break;
            case ALI_WEB:
            case ALI_QRCODE:
            case ALI_WAP:
                if (ret.containsKey("html") && null != ret.get("html") && ret.containsKey("url")
                        && null != ret.get("url")) {
                    order.setHtml(StrUtil.toStr(ret.get("html")));
                    order.setUrl(StrUtil.toStr(ret.get("url")));
                }
                break;
            case UN_WEB:
            case JD_WAP:
            case JD_WEB:
            case KUAIQIAN_WAP:
            case KUAIQIAN_WEB:
                if (ret.containsKey("html") && null != ret.get("html")) {
                    order.setHtml(StrUtil.toStr(ret.get("html")));
                }
                break;
            case YEE_WAP:
            case YEE_WEB:
            case BD_WEB:
            case BD_WAP:
                if (ret.containsKey("url") && null != ret.get("url")) {
                    order.setUrl(StrUtil.toStr(ret.get("url")));
                }
            default:
                break;
        }
    }

    /**
     * 组建返回沙箱支付订单
     */
    private static void placeSandboxOrder(TCOrder order, Map<String, Object> ret) {
        order.setId(StrUtil.toStr(ret.get("id")));
        switch (order.getChannel()) {
            case WX_NATIVE:
                if (ret.containsKey("url") && null != ret.get("url")) {
                    order.setCodeUrl(StrUtil.toStr(ret.get("url")));
                }
                break;
            case WX_JSAPI:
                order.setWxJSAPIMap(generateWXJSAPIMap(ret));
                break;
            case ALI_WEB:
            case ALI_QRCODE:
            case ALI_WAP:
                if (ret.containsKey("url")
                        && null != ret.get("url")) {
                    order.setHtml(TCUtil.generateSandboxHtmlWithUrl(StrUtil.toStr(ret.get("url"))));
                    order.setUrl(StrUtil.toStr(ret.get("url")));
                }
                break;
            case UN_WEB:
            case JD_WAP:
            case JD_WEB:
            case KUAIQIAN_WAP:
            case KUAIQIAN_WEB:
                if (ret.containsKey("url") && null != ret.get("url")) {
                    order.setHtml(TCUtil.generateSandboxHtmlWithUrl(StrUtil.toStr(ret.get("url"))));
                }
                break;
            case YEE_WAP:
            case YEE_WEB:
            case BD_WEB:
            case BD_WAP:
                if (ret.containsKey("url") && null != ret.get("url")) {
                    order.setUrl(StrUtil.toStr(ret.get("url")));
                }
            default:
                break;
        }
    }

    /**
     * 检查某一借口是否支持测试模式
     */
    private static void checkTestModeSwitch() throws TCException {
        if (TCCache.isSandbox()) {
        	 throw new TCException(TCErrorCode.e_105001.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105001.error());
        }
    }

    private String getAppSignature(String timeStamp,String other) {
        String str = appId + timeStamp + appSecret+other;
        return TCUtilPrivate.getMessageDigest(str);
    }

    private String getAppSignatureWithMasterSecret(String timeStamp) {
        String str = appId + timeStamp + masterSecret;
        return TCUtilPrivate.getMessageDigest(str);
    }
}
