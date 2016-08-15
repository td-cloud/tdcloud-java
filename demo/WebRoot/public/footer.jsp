<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
    <div class="footer">
        <p class="footer-sub">
            <a>关于TdCloud</a><span>|</span>
            <a>商家中心</a><span>|</span>
            <a>诚征英才</a><span>|</span>
            <a>联系我们</a><span>|</span>
            <br>
             <span>TdCloud版权所有</span>
            <span class="footer-date">2015-2016</span>
            <span><a href="http://www.miitbeian.gov.cn/" target="_blank">沪ICP备10016518号</a></span>
        </p>
    </div>
<script>
	var even = document.getElementById("licode");	
	var showqrs = document.getElementById("showqrs");
	 even.onmouseover = function(){
	 	showqrs.style.display = "block"; 
	 }
	 even.onmouseleave = function(){
	 	showqrs.style.display = "none";
	 }
</script>