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
    	html{font-size: 36px;}
    	ol{padding-left: 5px;}
	  	ol li{
		  	list-style-type:decimal;
		    list-style-position:inside;
		  	line-height: 1.5; 
		  	font-size: 1.5em;
	  	}
    </style>
</head>
<body>
	
	<div id="_contain">
		
		<div class="favorable" style="border-radius:1em;background:#e5ffe5;margin-top:5px;">
			<hr>
			<h3>请您凭姓名和电话号码到景区领票</h3>
			<ol>
				<li>您的姓名为:${tradeInfo.name}</li>
				<li>您的电话为:${tradeInfo.phone}</li>
				<li>您购买票数为:${tradeInfo.count}</li>
				<li style="color:red;">请牢记您填写的姓名和电话，凭此到景区兑换门票！如有问题请拨打景区电话:0715-2335998</li>
			</ol>
			<br>
			<hr>
		
		</div>
	</div>

	<script type="text/javascript">
	</script>    
</body>
</html>