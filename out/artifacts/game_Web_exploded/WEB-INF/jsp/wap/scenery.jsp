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
	<link href="css/wap/styles.css" type="text/css" rel="stylesheet"/>
	<link href="css/wap/photoswipe.css" type="text/css" rel="stylesheet"/>
	
	
	<script src="js/wap/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/wap/Adaptive.js"></script>
	
	<script type="text/javascript" src="js/wap/klass.min.js"></script>
	<script type="text/javascript"  src="js/wap/code.photoswipe-3.0.5.min.js"></script>
    
  <style type="text/css">

  </style>
  
  	<script type="text/javascript">
		(function(window, PhotoSwipe){
			document.addEventListener('DOMContentLoaded', function(){
				var options = {getToolbar: function(){
							return '<div class="ps-toolbar-close" style="padding-top: 12px;">关闭</div><div class="ps-toolbar-play" style="padding-top: 12px;">轮播</div><div class="ps-toolbar-previous" style="padding-top: 12px;">前一张</div><div class="ps-toolbar-next" style="padding-top: 12px;">下一张</div>';
						}},
					instance = PhotoSwipe.attach( window.document.querySelectorAll('#Gallery a'), options );
			
			}, false);
			
			
		}(window, window.Code.PhotoSwipe));
		
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
			<h3 style="text-align: center; margin-bottom: 1px; padding-top: 10px;">景区图库</h3>
				<ul id="Gallery" class="gallery">
					<li><a href="images/wap/scenery/000.jpg"><img src="images/wap/scenery/000_m.jpg" alt="Image 000" /></a></li>
					<li><a href="images/wap/scenery/001.jpg"><img src="images/wap/scenery/001_m.jpg" alt="Image 001" /></a></li>
					<li><a href="images/wap/scenery/002.jpg"><img src="images/wap/scenery/002_m.jpg" alt="Image 002" /></a></li>
					<li><a href="images/wap/scenery/003.jpg"><img src="images/wap/scenery/003_m.jpg" alt="Image 003" /></a></li>
					<li><a href="images/wap/scenery/004.jpg"><img src="images/wap/scenery/004_m.jpg" alt="Image 004" /></a></li>
					<li><a href="images/wap/scenery/005.jpg"><img src="images/wap/scenery/005_m.jpg" alt="Image 005" /></a></li>
					<li><a href="images/wap/scenery/006.jpg"><img src="images/wap/scenery/006_m.jpg" alt="Image 006" /></a></li>
					<li><a href="images/wap/scenery/007.jpg"><img src="images/wap/scenery/007_m.jpg" alt="Image 007" /></a></li>
					<li><a href="images/wap/scenery/008.jpg"><img src="images/wap/scenery/008_m.jpg" alt="Image 008" /></a></li>
					<li><a href="images/wap/scenery/009.jpg"><img src="images/wap/scenery/009_m.jpg" alt="Image 009" /></a></li>
					<li><a href="images/wap/scenery/010.jpg"><img src="images/wap/scenery/010_m.jpg" alt="Image 010" /></a></li>
					<li><a href="images/wap/scenery/011.jpg"><img src="images/wap/scenery/011_m.jpg" alt="Image 011" /></a></li>
					<li><a href="images/wap/scenery/012.jpg"><img src="images/wap/scenery/012_m.jpg" alt="Image 012" /></a></li>
					<li><a href="images/wap/scenery/013.jpg"><img src="images/wap/scenery/013_m.jpg" alt="Image 013" /></a></li>
					<li><a href="images/wap/scenery/014.jpg"><img src="images/wap/scenery/014_m.jpg" alt="Image 014" /></a></li>
					<li><a href="images/wap/scenery/015.jpg"><img src="images/wap/scenery/015_m.jpg" alt="Image 015" /></a></li>
					<li><a href="images/wap/scenery/016.jpg"><img src="images/wap/scenery/016_m.jpg" alt="Image 016" /></a></li>
					<li><a href="images/wap/scenery/017.jpg"><img src="images/wap/scenery/017_m.jpg" alt="Image 017" /></a></li>
					<li><a href="images/wap/scenery/018.jpg"><img src="images/wap/scenery/018_m.jpg" alt="Image 018" /></a></li>
					<li><a href="images/wap/scenery/019.jpg"><img src="images/wap/scenery/019_m.jpg" alt="Image 019" /></a></li>
					<li><a href="images/wap/scenery/020.jpg"><img src="images/wap/scenery/020_m.jpg" alt="Image 020" /></a></li>
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