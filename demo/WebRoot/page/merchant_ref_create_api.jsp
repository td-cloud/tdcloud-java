<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.tangdi.tdcloud.*" %>
<%@ page import="com.tangdi.tdcloud.bean.*" %>
<%@ page import="com.tangdi.tdcloud.bean.TCRefund" %>
<%@ page import="com.tangdi.tdcloud.TCPay" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@page import="org.apache.log4j.*" %>
<%
   /*
	   功能：退款
	   版本：1.0
	   日期：2016-07-27
	   说明：退款处理页面， 用于对已支付的订单发起退款
	   以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	   该代码仅供学习和研究使用，只是提供一个参考。
	*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; UTF-8">
    <title>Start Refund</title>
    <script type="text/javascript">
    </script>
</head>
<body>
<%
    Logger log = Logger.getLogger("merchant_ref_create_api.jsp");
	String refundNo = request.getParameter("refund_no");
	Map<String, Object> optional = new HashMap<String, Object>();
    optional.put("test", "test");
    
    String billNo = request.getParameter("bill_no");
    
    if (StringUtils.isBlank(refundNo)) {
        out.println("refundNo不能为空!");
        return;
    }
    
	String amt = request.getParameter("refund_fee");
	
    if (StringUtils.isBlank(billNo)) {
        out.println("billNo不能为空!");
        return;
    }
    if (StringUtils.isBlank(amt)) {
        out.println("refund_fee不能为空!");
        return;
    }
    
    Integer refundFee = Integer.parseInt(amt);

    TCRefund param = new TCRefund(billNo, refundNo, refundFee);
    param.setOptional(optional);
    try {

    	TCRefund refund = TCPay.startTCRefund(param);
        if (refund.getAliRefundHtml() != null) {
            out.println(refund.getAliRefundHtml());
        } else {
        	out.println("退款成功！WX渠道还需要定期查询退款结果！");
        	out.println("TdCloud响应退款唯一ID为："+refund.getId());
        }
    } catch (TCException e) {
    	out.println(e.getMessage());
        e.printStackTrace();
    }
    log.info("after start refund!");
%>
</body>