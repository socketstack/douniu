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
    
</head>
<body>
<!--	<header>-->
<!--		<i class="logo"></i>-->
<!--	    <a href="#">-->
<!--	    <div class="sch">-->
<!--	    	<p><img src="images/wap/icon/sch.png">搜索目的地、帖子、景点</p>-->
<!--	    </div>-->
<!--	    </a>-->
<!--	</header>-->
	
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
	
	<div class="fb">
	    <div class="fb-lt">
	    	<a href="wxIntrodection.do"><img src="images/wap/scenics/introduction.jpg"></a>
	    </div>
	</div>
	<div class="fb">
	    <div class="fb-lt">
	    	<a href="wxNotice.do"><img src="images/wap/scenics/notice.jpg"></a>
	    </div>
	</div>
	<div class="fb">
	    <div class="fb-lt">
	    	<a href="wxScenery.do"><img src="images/wap/scenics/view.jpg"></a>
	    </div>
	</div>
	<div class="fb">
	    <div class="fb-lt">
	    	<a href="wxVideo.do"><img src="images/wap/scenics/video.jpg"></a>
	    </div>
	</div>
	
	<div class="fb">
	    <div class="fb-lt">
	    	<a href="wxTicketGroup.do"><img src="images/wap/scenics/pay.jpg"></a>
	    </div>
	</div>
	
	<div class="fb">
	    <div class="fb-lt">
	    	<a href="wxLucky.do"><img src="images/wap/scenics/lucky.jpg"></a>
	    </div>
	</div>
	
	</div>
	 
	</div>
	
<!-- =====================================footer=============================== -->
<jsp:include page="footer.jsp" flush="true">  
  <jsp:param name="view" value="action"/>
</jsp:include>
<!--	<footer>-->
<!--		<div class="foot act">-->
<!--			<img src="images/wap/menu/ios-8-dialer-icon.png">-->
<!--	        <p>联系</p>-->
<!--	    </div>-->
<!--		<div class="foot">-->
<!--			<a href="wxScenery.do">-->
<!--				<img src="images/wap/menu/ios-8-gallery-icon.png">-->
<!--	       	 	<p>图库</p>-->
<!--			</a>-->
<!--	    </div>-->
<!--		<div class="foot">-->
<!--			<a href="wxVideo.do">-->
<!--				<img src="images/wap/menu/ios-8-video-icon.png">-->
<!--	        	<p>视频</p>-->
<!--			</a>-->
<!--	    </div>-->
<!--		<div class="foot">-->
<!--			<img src="images/wap/menu/nav_4.png">-->
<!--	        <p>导航</p>-->
<!--	    </div>-->
<!--	</footer>    -->
	    
	<script type="text/javascript">

	</script>
</body>
</html>