<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta content="telephone=no" name="format-detection">
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<link rel="stylesheet" type="text/css" href="../css/cart.css">
	<title>下级统计</title>
	<script type="text/javascript" async="" src="../js/aywmq.js"></script>
	<script async="" src="../js/analytics.js"></script>
	<script type="text/javascript" async="" src="../js/da_opt.js"></script>
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/dateutils.js"></script>
	<script>
	var startDate,endDate
	$(function(){
	    performanceNow()
	});
	function performance(){
	    $.ajax({
	        url: "userPerformance?startDate="+startStr+"&endDate="+endStr,
		    type: "GET",
		    dataType: "json",
		    async: false,
		    success: function(data) {
		        $("#performance").html(data.performance+"元");
		    }
	    });
	    
	}
	//本月查询时间段
	function performanceNow(){
	   startDate = new Date();
	   startDate = getMonthStartDate(startDate);
       endDate = new Date();
       startStr=formatDate(startDate);
       endStr = formatDate(endDate)
       $("#startDate").html(startStr);
	   $("#endDate").html(endStr);
	   performance()
	}
	//上月查询时间段
	function performanceLast(){
	   startDate = getLastMonthStartDate(startDate)
	   endDate = getLastMonthEndDate(endDate)
	   startStr=formatDate(startDate);
       endStr = formatDate(endDate)
       $("#startDate").html(startStr);
	   $("#endDate").html(endStr);
	   performance()
	}
	
	</script>
    
	<!--  <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page"> -->
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <div class="sn-nav">
		<div class="sn-nav-back"><a href="javascript:history.back(-1)">返回</a></div>
		<div class="sn-nav-title of" id="addAddr">我的业绩</div>
		<div class="sn-nav-right tr pr"><a href="index.jsp">首页</a></div>
	</div>

	<section class="sn-main pr">
	    <div class="input-a sn-block wbox mt30 pr">
			<span>开始时间:</span>
			<div class="wbox-flex ml30 pr" id="startDate">
				0
			</div>
		</div>
		<div class="input-a sn-block wbox mt30 pr">
			<span>结束时间:</span>
			<div class="wbox-flex ml30 pr" id="endDate">
				0
			</div>
		</div>
        <div class="input-a sn-block wbox mt30 pr">
			<span>我的业绩:</span>
			<div class="wbox-flex ml30 pr" id="performance">
				0
			</div>
		</div>
		
		<a href="javascript:performanceNow()" name="Wap_reg_person_005" class="first-step sn-btn sn-btn-big sn-btn-block m30 sn-btn-positive">本月</a>
		<a href="javascript:performanceLast()" name="Wap_reg_person_005" class="first-step sn-btn sn-btn-big sn-btn-block m30 sn-btn-positive">上月</a>
		
	</section>
	<script type="text/javascript" src="../js/zepto.min.js"></script>

</body>
</html>
