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
		<link rel="stylesheet" type="text/css" href="../css/style.css" media="all">
	<title>推广二维码</title>
	<script type="text/javascript" async="" src="../js/aywmq.js"></script>
	<script async="" src="../js/analytics.js"></script>
	<script type="text/javascript" async="" src="../js/da_opt.js"></script>
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
	<script type="text/javascript">
		wx.config({
                    debug : false,
                    appId : "${sessionScope.appId}",
                    timestamp : "${sessionScope.timestamp}",
                    nonceStr : "${sessionScope.nonceStr}",
                    signature : "${sessionScope.signature}",
                    jsApiList : [ 'checkJsApi', 'onMenuShareAppMessage']
        });
 		wx.ready(function(){
 			wx.checkJsApi({
 				jsApiList: ['onMenuShareAppMessage'],
 					success: function(res) {
 						console.log(res);
 					}
 				})
 				
 			wx.onMenuShareAppMessage({
 			      title: '裤头方会员推荐注册',
 			      desc: '点击链接注册账号',
 			      link: '${sessionScope.link}',
 			      imgUrl: '',
 			      /*trigger: function (res) {
 			        alert('用户点击发送给朋友 ');
 			      },
 			      success: function (res) {
 			        alert('已分享');
 			      },
 			      cancel: function (res) {
 			        alert('已取消');
 			      },
 			      fail: function (res) {
 			        alert(JSON.stringify(res));
 			      }*/
 			})
 			
 		});
 		
 		wx.error(function (res) {
 			  alert(res.errMsg);
 		});
	</script>
</head>
<body>
	<section class="sn-main pr">
	<c:if test="${sessionScope.loginUser.status==0 }">
		<div class="input-a sn-block wbox mt30 pr">
			<span><a href="../list.jsp">您的账号未激活，请先购买商品激活账号</a></span>
		</div>
	</c:if>
	<c:if test="${sessionScope.loginUser.status==1 }">
		<div >
			<img src=userQRCode></img>
		</div>
	</c:if>
	</section>
	<script type="text/javascript" src="../js/zepto.min.js"></script>

</body></html>