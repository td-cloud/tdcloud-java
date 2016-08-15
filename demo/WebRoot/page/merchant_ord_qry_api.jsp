<%@page import="com.tangdi.tdcloud.bean.TCQueryParameter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.tangdi.tdcloud.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="com.tangdi.tdcloud.bean.TCOrder" %>
<%@ page import="com.tangdi.tdcloud.bean.TCException" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.tangdi.tdcloud.bean.TCRefund" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
   /*
	功能：单笔查询
	版本：1.0
	日期：2015-11-25
	说明： 单笔订单记录展示页面，用于展示根据id查询返回的订单记录
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
    <title>商品订单查询</title>
</head>
<body>
<%

    String id = request.getParameter("id");
    String bill_no = request.getParameter("bill_no");
    if (StringUtils.isBlank(id) && StringUtils.isBlank(bill_no)) {
        out.println("bill_no,id不能全为空!");
        return;
    }
	/*
	* bill Id或者refund Id可根据需求自行传入，本处数据作为样例数据，仅供参考
	*/
	try {
		TCQueryParameter param = new TCQueryParameter();
		 param.setId(id);
		 param.setBillNo(bill_no);
        TCOrder result = TCPay.startQueryPay(param);
        pageContext.setAttribute("pay", result);
    } catch (TCException e) {
        out.println(e.getMessage());
    }
%>


<c:if test="${pay != null}">
<h1>查询成功</h1>
    <table border="3" class="table">
        <tr>
            
            <th>订单号</th>
            <th>Tdcloud唯一ID</th>
            <th>标题</th>
            <th>订单金额(分)</th>
            <th>渠道</th>
            <th>渠道交易号</th>
            <th>附加数据</th>
            <th>是否支付成功</th>
            <th>已退款</th>
            <th>渠道详细信息</th>
            <th>订单创建时间</th>
            <th>订单付款时间</th>
            <th>订单是否已经撤销</th>
        </tr>
        <tr align="center">
            <td>${pay.billNo}</td>
            <td>${pay.id}</td>
            <td>${pay.title}</td>
            <td>${pay.totalFee}</td>
            <td>${pay.channel}</td>
            <td>${pay.channelTradeNo}</td>
            <td>${pay.optionalString}</td>
            <td>${pay.result}</td>
            <td>${pay.refundResult}</td>
            <td>${pay.messageDetail}</td>
            <td>${pay.dateTime}</td>
            <td>${pay.successTime}</td>
            <td>${pay.revertResult}</td>
        </tr>
    </table>
</c:if>