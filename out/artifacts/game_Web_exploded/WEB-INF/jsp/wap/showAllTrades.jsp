<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>盘龙溪漂流</title>
  	<meta charset="utf-8" />
    <meta name="description" content="漂流 盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="keywords" content="漂流 盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="author" content="盘龙溪漂流" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <link rel="icon" href="images/PLXlogo.ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="images/PLXlogo.Ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="css/wap/style.css">
	<link rel="stylesheet" type="text/css" href="css/wap/index.css">
	
	<script src="js/wap/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/wap/Adaptive.js"></script>
    <style type="text/css">
    	.fb-text{
    		height:3.8rem;
    		font-size:25px;
    		text-align:center;
    	}
    	.btn
			{
				width:80%;
				height:1.5em;
				color:#ff3500;
				font-size:1em;
				font-weight:bold;
			}
    </style>
</head>
<body>
	
	<div id="_contain">
	
	<div class="banner">
		<img src="images/wap/index/banner.jpg">
	    <div class="logo-dbs">
	    	<div class="dbs-img">
	        	<img src="images/wap/icon/PLXlogo80px.jpg">
	        </div>
	        <p style="color:red">盘龙溪漂流,安全第一,惊险刺激！</p>
	    </div>
	</div>
	
	<div class="favorable">
	
		<c:if test="${noTickets=='yes' }">
		<font style="color:red">您没有购买记录！</font>
		</c:if>
		<c:forEach items="${tradeInfos}" var="tradeInfo">
			<div>
				<c:if test="${tradeInfo.state==1 || tradeInfo.state==3}">
				    <div>
				    	${tradeInfo.name}/${tradeInfo.phone}于${tradeInfo.createTimeStr}
				    	购买了${tradeInfo.count}张票，总计${tradeInfo.totalFee}元
				    	<font style="color:red">
				    		<c:if test="${tradeInfo.state==1}">(可以使用)</c:if>
				    	</font>
				    	<font style="color:#CCCCCC">
				    		<c:if test="${tradeInfo.state==3}">(已经使用)</c:if>
				    	</font>
				    </div>
				    <hr>
				</c:if>
			</div>
		</c:forEach>
	
	</div>
	
	</div>
	
<!-- =====================================footer=============================== -->
<jsp:include page="footer.jsp" flush="true">  
  <jsp:param name="view" value="action"/>
</jsp:include>
	    
	<script type="text/javascript">
		
	</script>
</body>
</html>