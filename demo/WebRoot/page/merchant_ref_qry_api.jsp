<%@page import="com.tangdi.tdcloud.bean.TCQueryParameter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.tangdi.tdcloud.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="com.tangdi.tdcloud.bean.TCRefund" %>
<%@ page import="com.tangdi.tdcloud.bean.TCException" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.tangdi.tdcloud.bean.TCRefund" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
   /*
	功能：单笔退款查询
	版本：1.0
	日期：2016-07-25
	说明： 单笔退款记录展示页面，用于展示根据id查询返回的退款记录
	以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	该代码仅供学习和研究使用，只是提供一个参考。

	//***********页面功能说明***********
	 该页面可以在本机电脑测试。
	//********************************
	*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; UTF-8">
    <link href="../css/demo.css" rel="stylesheet" type="text/css"/>
    <title>单笔退款订单查询</title>
</head>
<body>
<%

    String id = request.getParameter("id");
    String refund_no = request.getParameter("refund_no");
    if (StringUtils.isBlank(id) && StringUtils.isBlank(refund_no)) {
        out.println("refund_no,id不能全为空!");
        return;
    }
	/*
	* refund_no或者id可根据需求自行传入，本处数据作为样例数据，仅供参考
	*/
	try {
		TCQueryParameter param = new TCQueryParameter();
		 param.setId(id);
		 param.setRefundNo(refund_no);
		 TCRefund result = TCPay.startQueryRefund(param);
         pageContext.setAttribute("refund_order", result);
    } catch (TCException e) {
        out.println(e.getMessage());
    }
%>

<c:if test="${refund_order != null}">
<h1>查询成功</h1>
    <table border="3" class="table">
        <tr>
            <th>退款订单号</th>
            <th>Tdcloud退款标识唯一ID</th>
            <th>支付订单号 </th>
            <th>标题</th>
            <th>订单金额(分)</th>
            <th>退款金额(分)</th>
            <th>退款渠道</th>
            <th>退款是否成功</th>
            <th>渠道详细信息</th>
            <th>退款创建时间</th>
            <th>退款完成时间</th>
            <th>附加数据</th>
        </tr>
        <tr align="center">            
            <td>${refund_order.refundNo}</td>
            <td>${refund_order.id}</td>
            <td>${refund_order.billNo}</td>
            <td>${refund_order.title}</td>
            <td>${refund_order.totalFee}</td>
            <td>${refund_order.refundFee}</td>
            <td>${refund_order.channel}</td>
            <td>${refund_order.refunded}</td>
            <td>${refund_order.messageDetail}</td>
            <td>${refund_order.dateTime}</td>
            <td>${refund_order.successTime}</td>
            <td>${refund_order.optionalString}</td>
        </tr>
    </table>
</c:if>