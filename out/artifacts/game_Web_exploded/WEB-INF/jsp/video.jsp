<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>精彩视频</title>
  	<meta charset="utf-8" />
    <meta name="description" content="盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="keywords" content="盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
.vid-wrapper{
    width:100%;
    position:relative;
    padding-bottom:56.25%;    /*需要用padding来维持16:9比例,也就是9除以16*/
    height: 0;
}
.vid-wrapper video{
    position: absolute;
    top:0;
    left: 0;
    width: 100%;
    height: 100%
}
</style>

</head>
<body id="page1">
	<header>    
         <div class="slider-bg1">
         	<div style="position: absolute;"><img src="images/PLXlogo.png" alt=""/></div>
            <figure class="page2-img1"><img src="images/video_img_bg.jpg" alt="" /></figure>
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
					<div class="grid_24" style="text-align: center;"><h2 class="pro_title3" style="margin-bottom: 10px;">精彩视频</h2></div>
					<div class="grid_12">
			            <div class="pro_wrapper pro_pad_port">
			            	<div class="vid-wrapper">
			            		<video id="myVideo" controls preload="auto" poster="images/slide2.jpg">
					                <source src="movie/plx.mp4" type="video/mp4"/>
					            </video>
			            	</div>
			            </div>
			        </div>
			        <div class="grid_12">
			            <div class="pro_wrapper pro_pad_port">
							<div class="vid-wrapper">
			            		<video id="myVideo" controls preload="auto" poster="images/m1_img.jpg">
					                <source src="movie/v2.mp4" type="video/mp4"/>
					            </video>
			            	</div>
			            </div>
			        </div>
			        <div class="grid_12">
			            <div class="pro_wrapper pro_pad_port">
			            	<div class="vid-wrapper">
			            		<video id="myVideo" controls preload="auto" poster="images/m2_img.jpg">
					                <source src="movie/v3.mp4" type="video/mp4"/>
					            </video>
			            	</div>
			            </div>
			        </div>
			        <div class="grid_12">
			            <div class="pro_wrapper pro_pad_port">
							<div class="vid-wrapper">
			            		<video id="myVideo" controls preload="auto" poster="images/m3_img.jpg">
					                <source src="movie/v4.mp4" type="video/mp4"/>
					            </video>
			            	</div>
			            </div>
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
</html>