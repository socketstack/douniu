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
    
    <link rel="icon" href="images/PLXlogo.Ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="images/PLXlogo.Ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="css/wap/style.css">
	<link rel="stylesheet" type="text/css" href="css/wap/index.css">
	<link rel="stylesheet" type="text/css" href="css/pro_pages_style.css">
	
	
	<script src="js/wap/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/wap/Adaptive.js"></script>
	
    
  <style type="text/css">
	ul li{
		width:100%;
	    position:relative;
	    padding-bottom:56.25%;    /*需要用padding来维持16:9比例,也就是9除以16*/
	    height: 0;
	    margin-bottom:2px;
	}
	ul li video{
		position: absolute;
	    top:0;
	    left: 0;
	    width: 100%;
	    height: 100%
    }
  </style>
  
  	<script type="text/javascript">
		
	</script>
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
		
		
			<div id="MainContent">
			<h3 style="text-align: center; margin-bottom: 1px; padding-top: 10px;">精彩视频</h3>
				<ul>
					<li>
						<video id="myVideo" controls preload="auto" poster="images/slide2.jpg">
					         <source src="movie/plx.mp4" type="video/mp4"/>
					    </video>
					</li>
					<li>
						<video id="myVideo" controls preload="auto" poster="images/m1_img.jpg">
					         <source src="movie/v2.mp4" type="video/mp4"/>
					    </video>
					</li>
					<li>
						<video id="myVideo" controls preload="auto" poster="images/m2_img.jpg">
					         <source src="movie/v3.mp4" type="video/mp4"/>
					    </video>
					</li>
					<li>
						<video id="myVideo" controls preload="auto" poster="images/m3_img.jpg">
					         <source src="movie/v4.mp4" type="video/mp4"/>
					    </video>
					</li>
				</ul>
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