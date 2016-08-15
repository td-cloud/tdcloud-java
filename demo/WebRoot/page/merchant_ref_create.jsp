<%@ page language="java" pageEncoding="UTF-8"%><html>
<%@ page import="com.tangdi.tdcloud.*"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<head>
    <title>发起退款</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/> 
    <link href="${pageContext.request.contextPath}/css/css.css" rel="stylesheet" type="text/css" target="_blank"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript" language="javascript"></script>
    <script type="text/javascript">
        $(function(){
        	$("#li6 a").attr("style","color:#0ae");
        	$("div .title").html("发起退款(/rest/refund)");
        });   
    </script>
</head>
<body>
    <%@include  file="/public/header.jsp"%>
    <div class="content" style="height:712px;">
        <form action="merchant_ref_create_api.jsp" class="alipayform" method="POST" target="_blank">
            <div class="element" style="margin-top:60px;">
                <div class="legend">发起退款 </div>
            </div>
            <div class="element">
                <div class="etitle">商户订单号:</div>
                <div class="einput"><input type="text" name="bill_no"  maxlength="32"></div>
                <br>
                <div class="mark">发起支付时填写的订单号</div>
            </div>
            
            <div class="element">
                <div class="etitle">退款金额:</div>
                <div class="einput"><input type="text" name="refund_fee" maxlength="15"></div>
                <br>
                <div class="mark">注意：(以分为单位) 必须小于或等于对应的已支付订单的total_fee</div>
            </div>
            <div class="element" style="height:140px;">
                <div class="etitle">商户退款单号:</div>
                <div class="einput"><input type="text" name="refund_no" maxlength="32" value="<%out.println(new SimpleDateFormat("yyyyMMdd").format(new Date()) + TCUtil.generateNumberWith3to24digitals()+"");%>"></div>
                <br>
                <div class="mark" style="height:140px;">说明：格式为:退款日期(8位) + 流水号(3~24 位)，请自行确保在商户系统中唯一。且退款日期必须是发起退款的当天日期,同一退款单号不可重复提交，否则会造成退款单重复。流水号可以接受数字或英文字符，建议使用数字，但不可接受“000”</div>
            </div>
            <div class="element">
                <div class="mark" style="color:red;">其他说明：一笔订单可以支持多次退款，发起退款时请确保累计退款金额不多于原订单总金额 <br>
                        	    所有退款默认均为退回原支付账号</div>
            </div>
             
            <div class="element">
                <input type="submit" class="alisubmit" value ="提交查询">
            </div>
        </form>
    </div>
    <%@include  file="/public/footer.jsp"%>
</body>
</html>