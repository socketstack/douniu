<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>交通地图</title>
  	<meta charset="utf-8" />
    <meta name="description" content="盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="keywords" content="盘龙溪漂,湖北漂流,流 湖北旅游,夏季旅游" />
    <meta name="author" content="盘龙溪漂流" />
    
    <link rel="icon" href="images/PLXlogo.Ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="images/PLXlogo.Ico" type="image/x-icon" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/demo.css" />
    <script src="js/jquery-1.7.1.min.js"></script>
    <script src="js/script.js"></script>
<!--[if lt IE 9]>
	<script src="js/html5.js"></script>
	<link rel="stylesheet" href="css/ie.css"> 
<![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>
<body id="page1">
	<header>    
         <div class="slider-bg1">
         	<div style="position: absolute;"><img src="images/PLXlogo.png" alt=""/></div>
            <figure class="page2-img1"><img src="images/pho_img_bg.jpg" alt="" /></figure>
        </div>          
    </header>
    <!--==============================sideBar================================-->
    <jsp:include page="sideBar.jsp" flush="true">  
	  <jsp:param name="view" value="action"/>
	</jsp:include>
	<!--==============================content================================-->
    <section id="content">
        <div class="main">
        	<div class="container_24">
				<div class="pro_wrapper">
					<div class="grid_24" style="text-align: center;"><h2 class="pro_title3" style="margin-bottom: 10px;">交通地图</h2></div>
			        <div class="grid_24" style="border:1px solid #000">
			        	<img alt="交通地图" src="images/trafficMap.jpg" style="width: 100%;">
			        </div>
				</div>
			</div>
        </div>
    </section>
	<!--==============================footer=================================-->
    <jsp:include page="footer.jsp" flush="true">  
	  <jsp:param name="view" value="action"/>
	</jsp:include>

</body>