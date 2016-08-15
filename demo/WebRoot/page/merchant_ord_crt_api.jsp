<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.tangdi.tdcloud.bean.*"%>
<%@ page import="com.tangdi.tdcloud.*"%>
<%@ page import="com.tangdi.tdcloud.TCEumeration.*" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.Properties" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.lang.Thread" %>
<%@ include file="loadProperty.jsp" %>
<%
   /*
	   功能：用户支付
	   版本：1.0
	   日期：2016-07-12
	   说明： 支付处理页面， 用于发起TdCloud系统的请求，包括支付宝、微信、银联等在线渠道
	   以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	   该代码仅供学习和研究使用，只是提供一个参考。
 */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; UTF-8">
    <title>pay</title>
</head>
<body>

<%
    request.setCharacterEncoding("UTF-8");
    Logger log = Logger.getLogger("merchant_ord_crt_api.jsp");

    //模拟商户的交易编号、标题、金额、附加数据
    String billNo = request.getParameter("bill_no");
    String title = request.getParameter("title");
    String total_fee = request.getParameter("total_fee");
    String bill_timeout = request.getParameter("bill_timeout");
    String return_url = request.getParameter("return_url");
    
    Map<String, Object> optional = new HashMap<String, Object>();
    optional.put("test", "test");
	int amt =Integer.parseInt(total_fee);
	int timeout = Integer.parseInt(bill_timeout);
    String type = request.getParameter("paytype");
    PAY_CHANNEL channel;
    try {
        channel = PAY_CHANNEL.valueOf(type);
    } catch (Exception e) {
        channel = null;
        log.error(e.getMessage(), e);
    }
    boolean success = false;

    TCOrder bcOrder = new TCOrder(channel, amt, billNo, title);
    bcOrder.setBillTimeout(timeout);
    bcOrder.setOptional(optional);

    //以下是WX_JSAPI（公众号内支付）用到的返回参数，需要在页面的js用到
    String jsapiAppid = "";
    String timeStamp = "";
    String nonceStr = "";
    String jsapipackage = "";
    String signType = "";
    String paySign = "";
    
    //以下是每个渠道的return url
    //String aliReturnUrl = "http://124.193.113.122:48080/Tdcloud-PC-Demo/page/returnUrl.jsp";
    //String unReturnUrl = "http://124.193.113.122:48080/Tdcloud-PC-Demo/page/returnUrl.jsp";
    //String wxJSAPIRedirectUrl = "http://124.193.113.122:48080/Tdcloud-PC-Demo/page/returnUrl.jsp?paytype=WX_JSAPI";

    switch (channel) {
        case ALI_WEB:
        case ALI_WAP:
            bcOrder.setReturnUrl(return_url);
            try {
                bcOrder = TCPay.startTCPay(bcOrder);
                out.println(bcOrder.getHtml());
            } catch (TCException e) {
                log.error(e.getMessage(), e);
                out.println(e.getMessage());
            }
            break;
        case ALI_QRCODE:
            bcOrder.setQrPayMode(QR_PAY_MODE.MODE_FRONT);
            bcOrder.setReturnUrl(return_url);
            try {
                bcOrder = TCPay.startTCPay(bcOrder);
                out.println(bcOrder.getHtml());
            } catch (TCException e) {
                log.error(e.getMessage(), e);
                out.println(e.getMessage());
            }
            break;
        case WX:
            break;
        case WX_APP:
            break;
        case WX_NATIVE:
            try {
                bcOrder = TCPay.startTCPay(bcOrder);
                success = true;
            } catch (TCException e) {
                log.error(e.getMessage(), e);
                out.println(e.getMessage());
            }
            break;

        case WX_JSAPI:
            //微信 公众号id（读取配置文件conf.properties）及微信 redirec_uri
            Properties prop = loadProperty();
            String wxJSAPIAppId = prop.get("wxJSAPIAppId").toString();
            String wxJSAPISecret = prop.get("wxJSAPISecret").toString();
            String encodedWSJSAPIRedirectUrl = URLEncoder.encode(return_url);
            if (request.getParameter("code") == null || request.getParameter("code").toString().equals("")) {
                String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxJSAPIAppId + "&redirect_uri=" + encodedWSJSAPIRedirectUrl + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
                log.info("wx jsapi redirct url:" + redirectUrl);
                response.sendRedirect(redirectUrl);
            } else {
                String code = request.getParameter("code");
                String result = sendGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wxJSAPIAppId + "&secret=" + wxJSAPISecret + "&code=" + code + "&grant_type=authorization_code");
                log.info("result:" + result);
                JSONObject resultObject = JSONObject.fromObject(result);
                if (resultObject.containsKey("errcode")) {
                    out.println("获取access_token出错！错误信息为：" + resultObject.get("errmsg").toString());
                } else {
                    String openId = resultObject.get("openid").toString();
                    bcOrder.setOpenId(openId);
                    try {
                        bcOrder = TCPay.startTCPay(bcOrder);
                        out.println(bcOrder.getId());
                        System.out.print(bcOrder.getId());

                        Map<String, String> map = bcOrder.getWxJSAPIMap();
                        jsapiAppid = map.get("appId").toString();
                        timeStamp = map.get("timeStamp").toString();
                        nonceStr = map.get("nonceStr").toString();
                        jsapipackage = map.get("package").toString();
                        signType = map.get("signType").toString();
                        paySign = map.get("paySign").toString();
                    } catch (TCException e) {
                        log.error(e.getMessage(), e);
                        out.println(e.getMessage());
                    }
                }
            }

            break;

        case UN_WEB:
        case UN_WAP:
            bcOrder.setReturnUrl(return_url);
            try {
                bcOrder = TCPay.startTCPay(bcOrder);
                out.println(bcOrder.getHtml());
            } catch (TCException e) {
                log.error(e.getMessage(), e);
                out.println(e.getMessage());
            }
            break;
        case WX_SCAN:
            break;
        case ALI:
            break;
        case ALI_APP:
            break;
        case ALI_SCAN:
            break;
        case ALI_OFFLINE_QRCODE:
            break;
        case UN:
            break;
        case UN_APP:
            break;
        default:
            break;
    }
%>
<%!
    String sendGet(String url) throws Exception {
        String result = "";
        BufferedReader in = null;
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }
%>
<div align="center" id="qrcode">
</div>
</body>
<script src="../js/qrcode.js"></script>
<script type="text/javascript">
    function makeqrcode() {
        var qr = qrcode(10, 'L');
        qr.addData(codeUrl);
        qr.make();
        var wording = document.createElement('p');
        wording.innerHTML = "扫我，扫我";
        var code = document.createElement('DIV');
        code.innerHTML = qr.createImgTag();
        var element = document.getElementById("qrcode");
        element.appendChild(wording);
        element.appendChild(code);
    }
    var type = '<%=type%>';
    var codeUrl;
    var success = '<%=success%>'
    if (type == 'WX_NATIVE') {
        codeUrl = '<%=bcOrder.getCodeUrl()%>';
    }

    if (type == 'WX_NATIVE' || 'true' == success) {
        makeqrcode();
    }

</script>

<script type="text/javascript">
    callpay();
    function jsApiCall() {
        var data = {
            //以下参数的值由BCPayByChannel方法返回来的数据填入即可
            "appId": "<%=jsapiAppid%>",
            "timeStamp": "<%=timeStamp%>",
            "nonceStr": "<%=nonceStr%>",
            "package": "<%=jsapipackage%>",
            "signType": "<%=signType%>",
            "paySign": "<%=paySign%>"
        };
        alert(JSON.stringify(data));
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest',
                data,
                function (res) {
                    alert(res.err_msg);
                    alert(JSON.stringify(res));
                    WeixinJSBridge.log(res.err_msg);
                }
        );
    }

    function callpay() {
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', jsApiCall);
                document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
            }
        } else {
            jsApiCall();
        }
    }

</script>
</html>