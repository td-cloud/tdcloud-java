<%@ page language="java" pageEncoding="UTF-8"%><html>
<head>
    <title>单笔订单查询</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/> 
    <link href="${pageContext.request.contextPath}/css/css.css" rel="stylesheet" type="text/css" />
    <script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript" language="javascript"></script>
    <script type="text/javascript">
        $(function(){
        	$("#li2 a").attr("style","color:#0ae");
        	$("div .title").html("单笔商品订单查询(rest/queryPay)");
        });   
    </script>
</head>
<style>
    
</style>
<body>
    <%@include  file="/public/header.jsp"%>
    
    <div class="content" style="height:482px;">
        <form action="merchant_ord_qry_api.jsp" class="alipayform" method="POST" target="_blank">
            <div class="element" style="margin-top:60px;">
                <div class="legend">单笔商品订单查询 </div>
            </div>
            <div class="element">
                <div class="etitle">商城订单号:</div>
                <div class="einput"><input type="text" name="bill_no" id="bill_no"></div>
                <br>
                <div class="mark">注意：商城订单号、id最少保证一个有值</div>
            </div>
            
            <div class="element">
                <div class="etitle">id:</div>
                <div class="einput"><input type="text" name="id" ></div>
                <br>
                <div class="mark">注意：成功发起退款后TdCloud返回退款表记录唯一标识</div>
            </div>
            <div class="element">
                <input type="submit" class="alisubmit" value ="提交查询">
            </div>
        </form>
    </div>
    <%@include  file="/public/footer.jsp"%>
</body>
</html>