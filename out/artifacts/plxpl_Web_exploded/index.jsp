<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
String newLocn = "/index.do";
response.setHeader("Location",newLocn);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>盘龙溪漂流</title>
  	<meta charset="utf-8" />
    <meta name="description" content="盘龙溪漂流" />
    <meta name="keywords" content="盘龙溪漂流  湖北漂流" />
    <meta name="author" content="盘龙溪漂流" />
    
</head>
<body>

</body>
</html>