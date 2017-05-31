<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    <script type="text/javascript"    
		src="https://wifi.weixin.qq.com/resources/js/wechatticket/wechatutil.js" ></script>
</head>
<body>
</body>
<script type="text/javascript">
	$(function(){
		var appId = "${appId}";
		var extend = "${extend}";
		var timestamp = "{timestamp}";
		var sign = "{sign}";
		var shop_id = "{shop_id}";
		var authUrl = "{authUrl}";
		var mac = "{authUrl}";
		var ssid = "{ssid}";
		Wechat_GotoRedirect(
			appId,      
			extend,     
			timestamp, 
			sign,       
			shop_id,   
			authUrl,   
			mac,      
			ssid );
	});
</script>
</html>