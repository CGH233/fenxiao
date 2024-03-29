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
  <style type="text/css">
table.imagetable {
font-family: verdana,arial,sans-serif;
font-size:11px;
color:#333333;
border-width: 1px;
border-color: #999999;
border-collapse: collapse;
}
table.imagetable th {
background:#b5cfd2 url('cell-blue.jpg');
border-width: 1px;
padding: 8px;
border-style: solid;
border-color: #999999;
}
table.imagetable td {
background:#dcddc0 url('cell-yellow.jpg');
border-width: 1px;
padding: 8px;
border-style: solid;
border-color: #999999;
}
</style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <meta content="telephone=no" name="format-detection" />
    <script type="text/javascript" src="../js/jquery.min.js"></script>
  <script type="text/javascript" src="../js/zepto.min.js"></script>
  <script type="text/javascript">
  var reward = 0;
  function rewardCheck()
	{
		var mymessage=confirm("是否使用佣金抵扣");
		if(mymessage==true)
		{
			reward=1;
			pay();
		}
		else if(mymessage==false)
		{			
			pay();
		}
	}
  function pay(){
  		$.ajax({
	        url: "../ordersPay?no=${orders.no }&reward="+reward,
	        type: "get",
	        dataType: "json",
	        async: false,
	        success: function(data) {
	        	alertDefaultStyle("mini", data.message);
	        	if(data.href!=null){
	        		setTimeout("window.location.href='"+data.href+"';",2000);
	        	}
	        	if ("1" == data.status) {
	            	setTimeout("window.location.href='ordersDetail?no="+data.no+"';",2000);
	            }
	        },
	        error: function() {
	            alertDefaultStyle("mini", "请求失败");
	        }
	    });
  }
  </script>
  
  <title>订单详情</title>
  <link rel="stylesheet" href="../css/base.css" />
  <link rel="stylesheet" type="text/css" href="../css/cart3.css" />
 </head>
 <body>
  <div class="loagMask" id="loading" style="-webkit-transform-origin: 0px 0px 0px; opacity: 1; -webkit-transform: scale(1, 1); display: none;">
   <div class="sn-mask-loading fixedLoading"></div>
  </div>
  <section class="ww">
   <div class="sn-nav sn-block">
    <div class="sn-nav-back">
     <a class="sn-iconbtn" href="ordersList.jsp">返回</a>
    </div>
    <div class="sn-nav-title of">
     订单详情
    </div>
    <div class="sn-nav-right tr pr"><a href="index.jsp">首页</a></div>
   </div>
   <div class="pd fs26" id="djtTip" style="display: none;"></div>
   <div class="sn-block mb30" id="sndistribution" style="-webkit-transform-origin: 0px 0px 0px; opacity: 1; -webkit-transform: scale(1, 1);">
    <a href="javascript:void(0)" class="pd wbox order-tip">
     <div class="wbox-flex fs30 ml20">
      <span>您的订单<c:if test="${orders.status==0 }">未付款</c:if><c:if test="${orders.status==1 }">已发货</c:if>
      </span>
      <p class="fs24 sn-txt-muted">${orders.payDate }</p>
     </div></a>
   </div>
   <div id="J_order_cert_no" class="sn-block pd mb30 hide"></div>
   <div class="sn-block order-pro-list mb30" id="prodList">
    <ul class="order-opra order-pro-sn"> 
     <li> <a class="pro-list" href="../detail?id=${orders.productId }"> 
       <div class="wbox"> 
        <div class="pro-info wbox-flex"> 
         <div class="pro-name text-clamp2 fs26">
          	${orders.productName }
         </div> 
         <div class="snPrice sn-txt-black fs30">
          &yen;${orders.productMoney }
         </div> 
         <div class="list-opra sn-txt-muted"> 
          <span>x${orders.productNum }</span> 
         </div> 
        </div> 
       </div> </a> 
       </li> 
    </ul>
   </div>
   <div class="sn-block mb30 fs30">
    <ul class="sn-list-input order-list-input">
     <li>订单信息</li>
     <li>
      <div class="wbox-flex" id="payInfoArea">
       <p><label class="w3">订单编号 </label>：${orders.no }</p>
       <p><label class="w3">订单金额</label>：&yen;${orders.money }</p>
       <p><label class="w3">下单时间</label>：${orders.createDate }</p>
       <p><label class="w3">付款时间</label>：${orders.payDate }</p>
       <p><label class="w3">订单状态</label>：
       <c:if test="${orders.status==0 }">未付款
       <a href="javascript:rewardCheck()" id="submitOrderDivId" class="sn-btn sn-btn-assertive sn-btn-big">付款</a>
       </c:if>
       <c:if test="${orders.status==1 }">已付款</c:if>
       </p>
       <!-- 
       <p><label class="w3">订单摘要</label>
       		<table class="imagetable" width="80%" align="center">      		
	       		<tr>
	       			<td align="center">
		       			购买人
		       		</td>    			
		       		<td align="center">
		       			${sessionScope.loginUser.name}
		       		</td>
	       		</tr>      			
       		</table>
       	</p>
       	 -->
      </div></li>
    </ul>
   </div>
  </section>
 </body>
</html>