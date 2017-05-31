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
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/weui.css">
	
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
				color:#F3F3F3;
				font-size:1em;
				font-weight:bold;
				background-color:#1AAD19;
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
	
		<c:forEach items="${products}" var="product">
			<div class="fb" style="height:6.0rem;">
				<a href="wxPrePay.do?type=${product.id}">
				    <div class="fb-lt">
				    	<img src="${product.imgSrc}">
				    </div>
				    <div class="fb-text">
				    	<ul>
				    		<li>${product.name}</li>
				    		<li>${product.price}元/人</li>
				    	</ul>
				    </div>
			    </a>
			</div>
		</c:forEach>
	
	</div>
	
	<div style="text-align:center;margin-top:1rem;">
		<button type="button" class="btn" onclick="queryTickes()">查询已购买门票</button>
	</div>	
	 
	</div>
	
<!-- =====================================footer=============================== -->
<jsp:include page="footer.jsp" flush="true">  
  <jsp:param name="view" value="action"/>
</jsp:include>
	    
	<script type="text/javascript">
		function queryTickes(){
			window.location = "wxQueryAllTrades.do";
		}
	</script>
</body>
</html>