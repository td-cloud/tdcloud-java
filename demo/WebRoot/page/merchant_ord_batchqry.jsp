<%@ page language="java" pageEncoding="UTF-8"%><html>
<head>
    <title>批量查询</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/> 
    <link href="${pageContext.request.contextPath}/css/css.css" rel="stylesheet" type="text/css" />
    <script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript" language="javascript"></script>
</head>

    <script type="text/javascript">
        $(function(){
        	$("#li4 a").attr("style","color:#0ae");
        	$("div .title").html("批量订单查询(rest/queryPays)");
        	
        	var date = new Date();
            var year = date.getFullYear();
            var mon  = date.getMonth() + 1;
            var day  = date.getDate();
            var hour = date.getHours();
            var mins = date.getMinutes();
            var sec  = date.getSeconds();   
            if(mon < 10){
                mon = "0" + mon;
            } 
            if(day < 10){
                day = "0" + day;
            } 
            if(hour < 10){
                hour = "0" + hour;
            } 
            if(mins < 10){
                mins = "0" + mins;
            } 
            if(sec < 10){
                sec = "0" + sec;
            } 
            
            $('#end_time').val(year + "" + mon + day + hour + mins + sec); 
        });   
    </script>
<body>
    <%@include  file="/public/header.jsp"%>
    <div class="content" style="height:1150px;">
        <form action="merchant_ord_batchqry_api.jsp" class="alipayform" method="POST" target="_blank">
            <div class="element" style="margin-top:60px;">
                <div class="legend">批量订单查询 </div>
            </div>
            <div class="element">
                <div class="etitle">商城订单号:</div>
                <div class="einput"><input type="text" name="bill_no"  maxlength="32"></div>
                <br>
                <div class="mark">提示： 商城订单号即商城支付时上送TdCloud的订单号bill_no</div>
            </div>
            
            <div class="element" style="height:160px;">
                <div class="etitle">选择渠道：</div>
                <div class="einput_td1">
                     <ul class="clear">
                        <li class="clicked" onclick="paySwitch(this)">
						    <input type="radio" value="" name="paytype" checked="checked">
						    <img src="../img/all.png" alt="">
						</li>
			            <li onclick="paySwitch(this)">
			                <input type="radio" value="ALI_WEB" name="paytype">
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
            
            <div class="element">
                <div class="etitle">订单是否成功:</div>
                <div class="einput"><input style="width:20px;" type="radio"  name="spay_result" value="true">true</input>
                            <input style="width:20px;" type="radio" name="spay_result" value="false">false</input>
                </div>
                <br>
                <div class="mark">注意： 默认包含（true+false）</div>
            </div>
            <div class="element">
                <div class="etitle">返回渠道信息：</div>
                <div class="einput"><input style="width:20px;" type="radio"  name="need_detail" value="true">true</input>
                            <input style="width:20px;" type="radio" name="need_detail" value="false">false</input>
                </div>
                <br>
                <div class="mark">注意： 默认为false</div>
            </div>
             <div class="element">
                <div class="etitle">开始时间：</div>
                <div class="einput"><input type="text" id="start_time" name="start_time"  maxlength="14"></div>
                <br>
                <div class="mark">提示：开始时间若为空，系统默认开始时间为当前日期的前一年</div>
            </div>
            <div class="element">
                <div class="etitle">结束时间:</div>
                <div class="einput"><input type="text" id="end_time" name="end_time"  maxlength="14" ></div>
                <br>
            </div>
            <div class="element">
                <div class="etitle">第几页:</div>
                <div class="einput"><input type="text"  id="pages" name="pages"  maxlength="10"></div>
                 <br>
                <div class="mark">注意： 页码为正整数（默认第一页）</div>
                <br>
            </div>
            <div class="element">
                <div class="etitle">每页多少条:</div>
                <div class="einput"><input type="text"  id="number" name="number"  maxlength="14"></div>
                 <br>
                <div class="mark">注意： TdCloud默认为一页50条，最大不超过200条一页。</div>
                <br>
            </div>
            
           <div class="element">
               <div class="mark" >其他说明： bill_no, start_time, end_time等查询条件互相为<font color='red'>且</font>关系 <br/>
                            start_time, end_time指的是订单生成的时间，而不是订单支付的时间 </div>
            </div>
            <div class="element">
                <input type="submit" class="alisubmit" value ="提交查询">
            </div>
        </form>
    </div>
    
    <%@include  file="/public/footer.jsp"%>
</body>
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
</html>