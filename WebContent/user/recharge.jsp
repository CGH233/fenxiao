<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<link rel="stylesheet" type="text/css" href="../css/cart.css">
	<title>在线充值</title>
	<script type="text/javascript" async="" src="../js/aywmq.js"></script>
	<script async="" src="../js/analytics.js"></script>
	<script type="text/javascript" async="" src="../js/da_opt.js"></script>
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/jsapi.js"></script>
	<script>
	//对象失去焦点时调用的函数
	function moneyOnblur(){
  		var money = $("#money").val();
  		var reg = /^[0-9]+\.{0,1}[0-9]{0,2}$/; //整数及小数正则式
  		if(!reg.test(money)){
  			$("#money").val("0");
  		}else{
  			if(money<=0){
  				$("#money").val("0");
  				alertDefaultStyle("mini", "金额必须大于0");
  			}
  		}
  	}
  	//充值
  	function rechargeSave(){
  		var money = $("#money").val();
  	    if(money == ""){
  			alertDefaultStyle("mini", "请输入充值金额");
  		}else if(money<=0){
  		    $("#money").val("0");
  			alertDefaultStyle("mini", "金额必须大于0");
  		}else {
  		    var bank = $("#bank").val();
  		    if(bank=="alipay"){
  		        //由于微信浏览器屏蔽支付宝支付
  		        //先跳转至提示页面到外部浏览器中打开再调用支付接口
  		        window.location.href="alipayHint.jsp?money="+money;       
  		    }else if(bank=="wechat"){
  		        var appId,timeStamp,nonceStr,prepay_id,signType,paySign;
  		        $.ajax({
  		             url:"wxpayApi?money="+money,
  		             type: "GET",
		             dataType: "json",
		             async: false,
		             success: function(data) {                
		                 appId=data.appId;
  		                 timeStamp=data.timeStamp;
  		                 nonceStr=data.nonceStr;
  		                 prepay_id=data.prepay_id;
  		                 signType=data.signType;
  		                 paySign=data.paySign;
		             }      
  		        });
  		        
  		        if (typeof WeixinJSBridge == "undefined"){
                       if( document.addEventListener ){
                              document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                       }else if (document.attachEvent){
                              document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
                              document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                        }
                 }else{
                      //调用JSAPI支付，返回前端同步结果
                      onBridgeReady(appId,timeStamp,nonceStr,prepay_id,signType,paySign);
                 }
  		    }
           
  		}	
  	}
    </script>
</head>
<body>
    <div class="sn-nav">
		<div class="sn-nav-back"><a href="javascript:history.back(-1)">返回</a></div>
		<div class="sn-nav-title of" id="addAddr">在线充值</div>
		<div class="sn-nav-right tr pr"><a href="index.jsp">首页</a></div>
	</div>

	<section class="sn-main pr">
	    <div class="input-a sn-block wbox mt30 pr">
			<span>付款类型:</span>
			<div class="wbox-flex ml30 pr">
				<select name="bank" id="bank" style="BORDER-STYLE: none;">				
				<option value="wechat">微信</option>
				<option value="alipay">支付宝</option>
				</select>
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>充值金额:</span>
			<div class="wbox-flex ml30 pr">
				<input type="text" id="money" name="money" maxlength="32" onblur="moneyOnblur()">
			</div>
			<em class="delete" style="display:none" name="Wap_reg_person_001"></em>
		</div>
		<a href="javascript:rechargeSave()" name="Wap_reg_person_005" class="first-step sn-btn sn-btn-big sn-btn-block m30 sn-btn-positive">充值</a>
	</section>
	<script type="text/javascript" src="../js/zepto.min.js"></script>

</body></html>