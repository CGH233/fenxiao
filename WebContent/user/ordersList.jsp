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
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <meta content="telephone=no" name="format-detection" />
  <meta name="wap-font-scale" content="no" />
  <title>订单列表</title>
  <link rel="stylesheet" href="../css/base.css" />
  <link rel="stylesheet" type="text/css" href="../css/cart3.css" />
  <script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/zepto.min.js"></script>
	<script>
	var page = 1;
	var type = 2;
	</script>
 </head>
 <body>
  <section class="ww order-wrap">
   <div class="sn-nav sn-block">
    <div class="sn-nav-back">
     <a class="sn-iconbtn" href="index.jsp">返回</a>
    </div>
    <div class="sn-nav-title of">
   	  订单列表
    </div>
    <div class="sn-nav-right tr pr"><a href="index.jsp">首页</a></div>
   </div>
   <div class="order-tab" id="b2cOrder" style="-webkit-transform-origin: 0px 0px 0px; opacity: 1; -webkit-transform: scale(1, 1);">
    <ul class="order-nav wwbox" id="orderTab">
     <li class="cur" id="orderTab1"><a href="javascript:void(0);" onclick="javascript:change(2);">全部订单</a></li>
     <li class="" id="orderTab2"><a href="javascript:void(0);" onclick="javascript:change(0);">待支付</a></li>
     <li class="" id="orderTab3"><a href="javascript:void(0);" onclick="javascript:change(1);">待收货</a></li>
    </ul>
    <div class="order-tab-count" id="orderList">

    </div>
    <script>
    $(function(){
		list();
	});
	function change(t){
		if(t==2){
			$("#orderTab1").removeClass().addClass("cur");
			$("#orderTab2").removeClass();
			$("#orderTab3").removeClass();
		}else if(t==0){
			$("#orderTab1").removeClass();
			$("#orderTab2").removeClass().addClass("cur");
			$("#orderTab3").removeClass();
		}else{
			$("#orderTab1").removeClass();
			$("#orderTab2").removeClass();
			$("#orderTab3").removeClass().addClass("cur");
		}
		page=1;
		type=t;
		$("#orderList").empty();
		list();
	}
	function list(){
		$.ajax({
		    url: "ordersList?type="+type+"&p="+page,
		    type: "GET",
		    dataType: "json",
		    async: false,
		    success: function(data) {
		        if(data.status==1){
		        	var list = data.list;
		        	var status = "";
		        	for(var i=0;i<list.length;i++){
		        		if(list[i].status=="0"){
		        			status = "未付款";
		        		}else{
		        			status = "已成功";
		        		}
		        		var d = new Date(list[i].createDate);
		        		var date = d.getFullYear() + "-";
							date+=("0"+(d.getMonth()+1)).slice(-2) + "-";
							date+= ("0"+d.getDate()).slice(-2) + " ";
							date+= ("0"+d.getHours()).slice(-2) + ":";	   
							date+= ("0"+d.getMinutes()).slice(-2) + ":";
							date+=("0"+d.getSeconds()).slice(-2);
		        		$("#orderList").append("<div class='order-list' id='"+list[i].id+"'><a href='ordersDetail?no="+list[i].no+"'><div class='cart-title wbox'><div class='of f30'>订单号&nbsp;&nbsp;"+list[i].no+"</div><div class='wbox-flex tr sn-txt-muted'>应付款 <i class='f30 sn-txt-black'> ￥"+list[i].money+" </i></div></div></a><ul class='order-pro-list order-box'><li id='"+list[i].id+"_'><a href='ordersDetail?no="+list[i].no+"' class='pro-list'><div class='wbox'><div class='pro-info wbox-flex'><div class='pro-name text-clamp2 fs26'>"+list[i].productName+"</div><div class='snPrice sn-txt-black fs30'>"+date+"</div><div class='list-opra sn-txt-muted'>"+status+"&nbsp;&nbsp;&nbsp;&nbsp;数量："+list[i].productNum+"</div></div></div></a></li></ul></div>");
		        	}
		        	$("#nextPage").html("点击加载更多");
		        	$("#nextPage").attr("style","display:");
		        	$("#noLoadNextPage").attr("style","display:none");
		        	if(data.isNextPage==0){
		        		$("#nextPage").html("已全部加载完");
		        		$("#nextPage").attr("style","display:none");
		        		$("#noLoadNextPage").attr("style","display:");
		        	}
		        	page ++;
		        }else{
		        	alertDefaultStyle("mini", "暂无数据");
		        }
		    }
		});
	}
    </script>
   </div>
   </div>
   <div class="mb30 sn-txt-muted tc" id="noLoadNextPage" style="display:none">
    <span>Duang~到底了</span>
   </div>
   <div id="loadNextPage">
    <div class="mb30 tc">
     <a href="javascript:void(0)" id="nextPage" name="Wap_reg_person_005" onclick="list();return false;" class="sn-txt-muted">点击加载更多</a>
    </div>
   </div>
  </section>
 </body>
</html>