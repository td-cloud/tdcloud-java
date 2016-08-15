<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.tangdi.tdcloud.*"%>
<html>

<head>
    <title>统一下单</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/> 
    <link href="${pageContext.request.contextPath}/css/css.css" rel="stylesheet" type="text/css" />
    <script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript" language="javascript"></script>

    <script type="text/javascript">
    
        $(function(){
        	$("#li1 a").attr("style","color:#0ae");
        	$("div .title").html("统一下单(rest/bill)");
        });   
    </script>
</head>
<body>
    <%@include  file="/public/header.jsp"%>
    
    <div class="content" style="height:1050px;">
        <form action="merchant_ord_crt_api.jsp" class="alipayform" method="POST" target="_blank">
            <div class="element" style="margin-top:60px;">
                <div class="legend">统一下单 </div>
            </div>
            <div class="element">
                <div class="etitle">商城订单号：</div>
                <div class="einput"><input type="text" name="bill_no" id="bill_no" maxlength="32" minlength="8" value="<%out.println(TCUtil.generateRandomUUIDPure()); %>"></div>
                <br>
                <div class="mark">注意：8到32位数字和/或字母组合，请自行确保在商户系统中唯一</div>
            </div>
            
            <div class="element">
                <div class="etitle">订单标题：</div>
                <div class="einput"><input type="text" name="title"  maxlength="200" value="测试商品标题"></div>
                <br>
                <div class="mark">注意：订单标题最长支持100个汉字</div>
            </div>
            <div class="element">
                <div class="etitle">订单金额：</div>
                <div class="einput"><input type="text" name="total_fee"  maxlength="15" value="1"></div>
                <br>
                <div class="mark">注意：必须是正整数，单位为分</div>
            </div>
             <div class="element" style="height:auto;">
                <div class="etitle">同步返回地址：</div>
                <div class="einput"><input type="text" name="return_url"  maxlength="200" value="http://124.193.113.122:48085/Tdcloud-PC-Demo/page/returnUrl.jsp"></div>
                <br>
                <div class="mark" style="height: 60px;">注意：支付渠道处理完请求后,当前页面自动跳转到商户网站里指定页面的http路径，中间请勿有#,?等字符</div>
            </div>
            
            <div class="element">
                <div class="etitle">订单失效时间：</div>
                <div class="einput"><input type="text" name="bill_timeout"  maxlength="14" value="600"></div>
                <br>
                <div class="mark">注意：必须为非零正整数，单位为秒，建议最短失效时间间隔必须大于360秒</div>
            </div>
            
            <div class="element" style="height:140px;">
                <div class="etitle">选择渠道：</div>
                <div class="einput_td1">
                     <ul class="clear">
			            <li class="clicked" onclick="paySwitch(this)">
			                <input type="radio" value="ALI_WEB" name="paytype" checked="checked">
			                <img src="http://beeclouddoc.qiniudn.com/ali.png" alt="">
			            </li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="ALI_QRCODE" name="paytype">
			                <img src="http://beeclouddoc.qiniudn.com/alis.png" alt="">
			            </li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="ALI_WAP" name="paytype">
			                <img src="http://beeclouddoc.qiniudn.com/aliwap.png" alt="">
			            </li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="WX_NATIVE" name="paytype">
			                <img src="http://beeclouddoc.qiniudn.com/wechats.png" alt="">
			            </li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="WX_JSAPI" name="paytype">
			                <img src="http://7xavqo.com1.z0.glb.clouddn.com/wechatgzh.png" alt="">
			            </li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="UN_WEB" name="paytype">
			                <img src="http://beeclouddoc.qiniudn.com/unionpay.png" alt="">
			            </li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="UN_WAP" name="paytype">
			                <img src="http://beeclouddoc.qiniudn.com/icon-unwap.png" alt="">
			            </li>
			        </ul>
                </div>
                <br>
                
            </div>
            <div class="element" style="height: 160px;">
                <div class="mark" >提示： 以上仅为PC端支持的渠道，完整渠道对应码如下： <br>
								WX_APP: 微信手机原生APP支付 ;WX_NATIVE: 微信公众号二维码支付 ;WX_JSAPI: 微信公众号支付 ;
								ALI_APP: 支付宝手机原生APP支付 ;ALI_WEB: 支付宝PC网页支付 ;ALI_QRCODE: 支付宝内嵌二维码支付 ;
								ALI_OFFLINE_QRCODE: 支付宝线下二维码支付 ;ALI_WAP: 支付宝移动网页支;UN_APP: 银联手机原生APP支付;
								UN_WEB: 银联PC网页支付 ;UN_WAP: 银联移动网页支付 </div>
            </div>
            
            <div class="element">
                <input type="submit" class="alisubmit" value ="提交">
            </div>
            
        </form>
    </div>
    <%@include  file="/public/footer.jsp"%>
</body>
</html>

<script type="text/javascript">
    function paySwitch(that) {
        var li = that.parentNode.children;
        for (var i = 0; i < li.length; i++) {
            li[i].className = "";
            li[i].childNodes[1].removeAttribute("checked");
        }
        console.log(li);
        that.className = "clicked";
        that.childNodes[1].setAttribute("checked", "checked");
    }
</script>