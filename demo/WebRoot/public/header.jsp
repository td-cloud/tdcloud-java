<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.tangdi.tdcloud.*"%>
<%
TdCloud.registerApp("748034501028216832", "4bfdd244-574d-4bf3-b034-0c751ed34fe2","39a7a518-9ac8-4a9e-87bc-7885f33cf182","e14ae2db-608c-4f8b-b863-c8c18953e332");
%>
<div class="header">
        <div class="container black">
            <div class="qrcode">
                <div class="littlecode">
                    <img width="16px" src="../img/little_qrcode.jpg" id="licode">
                    <div class="showqrs" id="showqrs">
                        <div class="shtoparrow"></div>
                        <div class="guanzhuqr">
                            <img src="../img/guanzhu_qrcode.png" width="80">
                            <div class="shmsg" style="margin-top:5px;"> 请扫码关注</div>
                            <div class="shmsg" style="margin-bottom:5px;">接收重要信息</div>
                        </div>
                    </div>
                </div>      
            </div>
        </div>
        <div class="container">
            <div class="nav">
                <a href="http://www.tangdi.com.cn/" class="logo"><img src="../img/i_logo.png" height="30px"></a>
                <span class="divier"></span>
                <a onclick="javascript:viod(0);" class="open" target="_blank">Tdcloud-PC-Demo</a>
                <ul class="navbar">
                    <li id="li1"><a href="${pageContext.request.contextPath}/page/merchant_ord_crt.jsp">统一下单</a></li>
            <li id="li2"><a href="${pageContext.request.contextPath}/page/merchant_ord_qry.jsp">订单查询（单笔）</a></li>
            <li id="li3"><a href="${pageContext.request.contextPath}/page/merchant_ref_qry.jsp">退款查询（单笔）</a></li>
            <li id="li4"><a href="${pageContext.request.contextPath}/page/merchant_ord_batchqry.jsp">订单查询（批量）</a></li>
            <li id="li5"><a href="${pageContext.request.contextPath}/page/merchant_ref_batchqry.jsp">退款查询（批量）</a></li>
            <li id="li6"><a href="${pageContext.request.contextPath}/page/merchant_ref_create.jsp">退款发起</a></li>
                </ul>
            </div>
        </div>
        <div class="container blue">
            <div class="title"></div>
        </div>
</div>