<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- <!DOCTYPE html> -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta content="telephone=no" name="format-detection">
		<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
		
		<c:choose>
    	<c:when test = "${session.type == 1}">
			<title>推广二维码</title>
		</c:when>
		<c:otherwise>
			<title>会员注册</title>
		</c:otherwise>
		</c:choose>
	<script type="text/javascript" async="" src="js/aywmq.js"></script>
	<script async="" src="js/analytics.js"></script>
	<script type="text/javascript" async="" src="js/da_opt.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jsAddress.js"></script>	
	<script type="text/javascript">
	var tuijianren=getUrlVars()["tuijianren"];
	$(document).ready(function() {
		createUserNo();
		$("#tuijianren").val(tuijianren);
	});
	function getUrlVars()  
{  
    var vars = [], hash;  
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');  
    for(var i = 0; i < hashes.length; i++)  
    {  
        hash = hashes[i].split('=');  
        vars.push(hash[0]);  
        vars[hash[0]] = hash[1];  
    }  
    return vars;  
}  
	
	</script>
</head>
<body>
<%
String errorInfo = (String)request.getAttribute("message");         // 获取错误属性
if(errorInfo != null) {
%>
<script type="text/javascript" language="javascript">
alert("<%=errorInfo%>");                                            // 弹出错误信息
</script> 
<%
}
%>
		<c:choose>
    	<c:when test = "${session.type == 1}" >
		<section class="sn-main pr">
		<c:if test="${sessionScope.loginUser.status == 0}">
			<div class="input-a sn-block wbox mt30 pr">
				<span><a href="list.jsp">您的账号未激活，请先购买商品激活账号</a></span>
			</div>
		</c:if>
		<c:if test="${sessionScope.loginUser.status==1 }">
			<div>
				<img src=user/userQRCode></img>
			</a>
			</div>
		</c:if>
		</section>
		</c:when>
		<c:otherwise>
		<div class="sn-nav">
			<div class="sn-nav-back">
			<a class="sn-iconbtn" href="index.jsp">返回</a></div>
			<div class="sn-nav-title of">注册</div>
		</div>		
		<section class="sn-main pr">
			<form action="register()" class="pageForm" data-toggle="validate" data-reload-navtab="true">
			<input type="hidden" id="no" name="user.no" value="" placeholder="请刷新生成编号" maxlength="6" readonly="readonly">			
		<% if (errorInfo != null) {%>
		<input type="hidden" id="tuijianren" name="tuijianren" value="${tuijianren}" placeholder="请输入推荐人电话号码" maxlength="32">		
		<div class="input-a sn-block wbox mt30 pr">
			<span>真实姓名</span>
			<div class="wbox-flex ml30 pr">
				<input type="text" id="name" name="user.name" value="${user.name}" placeholder="请输入真实姓名" maxlength="32">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>手机号</span>
			<div class="wbox-flex ml30 pr">
				<input type="tel" id="phone" name="user.phone" value="${user.phone}" placeholder="请输入11位手机号码" maxlength="11">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>密码</span>
			<div class="wbox-flex ml30 pr">
				<input type="password" id="password" name="user.password" value="${user.password}" placeholder="请输入密码" maxlength="32">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>确认密码</span>
			<div class="wbox-flex ml30 pr">
				<input type="password" id="repassword" name="repassword" value="${repassword}" placeholder="请重新输入密码" maxlength="32">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>	
		<% }else{ %>						
		<div class="input-a sn-block wbox mt30 pr">
			<span>真实姓名</span>
			<div class="wbox-flex ml30 pr">
				<input type="text" id="name" name="user.name" value="" placeholder="请输入真实姓名" maxlength="32">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>手机号</span>
			<div class="wbox-flex ml30 pr">
				<input type="tel" id="phone" name="user.phone" value="" placeholder="请输入11位手机号码" maxlength="11">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>密码</span>
			<div class="wbox-flex ml30 pr">
				<input type="password" id="password" name="user.password" value="" placeholder="请输入密码" maxlength="32">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>确认密码</span>
			<div class="wbox-flex ml30 pr">
				<input type="password" id="repassword" name="repassword" value="" placeholder="请重新输入密码" maxlength="32">	
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<input type="hidden" id="tuijianren" name="tuijianren" value="" placeholder="请输入推荐人编号" maxlength="32">				
		<%} %>
		<div class="input-a sn-block wbox mt30 pr">
			<span>地址</span>
			<select id="cmbProvince" name="cmbProvince" value=""></select>
			<select id="cmbCity" name="cmbCity" value=""></select>
			<select id="cmbArea" name="cmbArea" value=""></select>			
			<script>	
				addressInit('cmbProvince', 'cmbCity', 'cmbArea');	 
			</script>				 	
		</div>
		<button type="submit" class="first-step sn-btn sn-btn-big sn-btn-block m60 sn-btn-positive">注册</button>
		</form>
		<p class="assisFun f14 m30"><a href="login.jsp" name="WAP_login_none_register">登录</a>
		<a href="findPassword.jsp">忘记密码?</a></p>				
	</section>
	</c:otherwise>
	</c:choose>
	<script type="text/javascript" src="js/zepto.min.js"></script>
</body></html>