<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.tangdi.tdcloud.bean.*"%>
<%@ page import="com.tangdi.tdcloud.*"%>
<%@ page import="com.tangdi.tdcloud.TCEumeration.*" %>
<%@ page import="com.tangdi.tdcloud.bean.TCRefund" %>
<%@ page import="com.tangdi.tdcloud.bean.TCRefunds" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
   /*
	功能：退款批量查询
	版本：1.0
	日期：2016-07-16
	说明： 该页面演示了退款订单批量查询及显示
	以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	该代码仅供学习和研究使用，只是提供一个参考。

	//***********页面功能说明***********
	 该页面可以在本机电脑测试。
	//******************************
	*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; UTF-8">
    <title>退款订单批量查询结果</title>
</head>
<body>
<%
    String bill_no = request.getParameter("bill_no");
    String refund_no = request.getParameter("refund_no");
	String paytype = request.getParameter("paytype");
	String need_detail = request.getParameter("need_detail");
	String start_time = request.getParameter("start_time");
	String end_time = request.getParameter("end_time");
	String pages = request.getParameter("pages");
	String number = request.getParameter("number");
	TCQueryParameter param = new TCQueryParameter();
	
	if(StringUtils.isNotBlank(bill_no)){
		param.setBillNo(bill_no);
	}
	if(StringUtils.isNotBlank(refund_no)){
		param.setRefundNo(refund_no);
	}
	PAY_CHANNEL channel;
	if(StringUtils.isNotBlank(paytype)){
		channel = PAY_CHANNEL.valueOf(paytype);
		param.setChannel(channel);
	}
	if(StringUtils.isNotBlank(need_detail)){
		param.setNeedDetail(Boolean.parseBoolean(need_detail));
	}

	if(StringUtils.isNotBlank(start_time)){
		param.setStartTime(start_time);
	}
	if(StringUtils.isNotBlank(end_time)){
		param.setEndTime(end_time);
	}
	if(StringUtils.isNotBlank(pages)){
		param.setPages(Integer.parseInt(pages));
	}
	if(StringUtils.isNotBlank(number)){
		param.setNumber(Integer.parseInt(number));
	}
	
	
    try {
    	 
    	TCRefunds ret =TCPay.startQueryRefunds(param);
        List<TCRefund> bills = ret.getOrders();
        int total = ret.getTotal();
        pageContext.setAttribute("total", total);
        pageContext.setAttribute("bills", bills);
        
    } catch (TCException e) {
        out.println(e.getMessage());
    }
    
    
%>
<c:if test="${total != null and total !=0}">
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
        <c:forEach var="bill" items="${bills}" varStatus="index">
            <tr>
            <td>${bill.refundNo}</td>
            <td>${bill.id}</td>
            <td>${bill.billNo}</td>
            <td>${bill.title}</td>
            <td>${bill.totalFee}</td>
            <td>${bill.refundFee}</td>
            <td>${bill.channel}</td>
            <td>${bill.refunded}</td>
            <td>${bill.messageDetail}</td>
            <td>${bill.dateTime}</td>
            <td>${bill.successTime}</td>
            <td>${bill.optionalString}</td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="20"><strong>符合条件记录总条数:</strong><font color="green">${total}</font></td>
        </tr>
    </table>
</c:if>
<c:if test="${total ==0}">
  查无记录！
</c:if>