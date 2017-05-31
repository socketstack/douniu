<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>自驾导航</title>
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
					<div class="grid_24" style="text-align: center;"><h2 class="pro_title3" style="margin-bottom: 10px;">自驾导航</h2></div>
			        <div class="grid_24" style="border:1px solid #000">
			        	<div class="container_24">
			        		<div class="pro_wrapper">
			        			<div id="container" class="grid_18" style="min-height:600px;font-size: large;">
			            		</div>
			            		<div id="container" id="container1" class="grid_6" style="height:600px;overflow: auto;">
			            			<div id="results" style="font-size: large;">请允许共享您的地理位置，才能为您导航！</div>
			            		</div>
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

<script type="text/javascript">

function initialize() {
	var mp = new BMap.Map('container');
	var point = new BMap.Point(114.629, 29.747);
	mp.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
	mp.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
	mp.centerAndZoom(point, 15);
	var marker = new BMap.Marker(point);        // 创建标注    
	mp.addOverlay(marker);                     // 将标注添加到地图中
	var opts = {    
	 width : 30,     // 信息窗口宽度    
	 height: 20,     // 信息窗口高度    
	 title : "盘龙溪漂流"  // 信息窗口标题   
	}    
	var infoWindow = new BMap.InfoWindow("欢迎您！", opts);  // 创建信息窗口对象    
	mp.openInfoWindow(infoWindow, mp.getCenter());      // 打开信息窗口
	
	var sContent =
	"<h4 style='margin:0 0 5px 0;padding:0.2em 0'>盘龙溪</h4>" + 
	"<img style='float:right;margin:4px' id='imgDemo' src='./ckfinder/userfiles/images/2011060985122457.jpg' width='139' height='104' title='天安门'/>" + 
	"<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>通山大幕山盘龙溪漂流有限公司位于国家级风景区九宫山北麓省级森林公园--大幕山森林公园脚下...</p>" + 
	"</div>";
	var infoWindows = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
	marker.addEventListener("click", function(){          
	   this.openInfoWindow(infoWindows);
	   //图片加载完毕重绘infowindow
	   document.getElementById('imgDemo').onload = function (){
		   infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
	   }
	});
	
	var driving = new BMap.DrivingRoute(mp, {    
	 renderOptions: {    
	   map: mp,    
	   panel : "results",    
	   //autoViewport: true,
	   enableDragging: true ,
	   Policy:BMAP_DRIVING_POLICY_AVOID_HIGHWAYS
	 }    
	});    
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			var mk = new BMap.Marker(r.point);
			mp.addOverlay(mk);
			mp.panTo(r.point);
			driving.search(r, new BMap.Point(114.629, 29.747));
		}      
	},{enableHighAccuracy: true})
	
	
	marker.enableDragging();    
	marker.addEventListener("dragend", function(e){    
	alert("当前位置：" + e.point.lng + ", " + e.point.lat);    
	})
	
	var pos = {type:BMAP_NAVIGATION_CONTROL_ZOOM};
	var opts = {offset:new BMap.Size(150,5)};
	mp.addControl(new BMap.NavigationControl());
	mp.addControl(new BMap.ScaleControl());    
	mp.addControl(new BMap.OverviewMapControl());    
	mp.addControl(new BMap.MapTypeControl());    
	//mp.setCurrentCity("北京"); 
	
	// 定义一个控件类,即function
	function SwitchControl(){
	  // 默认停靠位置和偏移量
	  this.defaultAnchor = BMAP_ANCHOR_BOTTOM_RIGHT;
	  this.defaultOffset = new BMap.Size(50, 10);
	}
	SwitchControl.prototype = new BMap.Control();
	SwitchControl.prototype.initialize = function(mp){
		// 创建一个DOM元素
		var div = document.createElement("div");
		var div1 = document.createElement("div");
		var div2 = document.createElement("div");
		div.appendChild(div1);
		div.appendChild(div2);
		// 添加文字说明
		div1.appendChild(document.createTextNode("地图"));
		div2.appendChild(document.createTextNode("详情"));
		div1.style.border = "0px solid gray";
		div1.style.width = "50%";
		div1.style.backgroundColor = "white";
		div2.style.width = "50%";
		div2.style.border = "0px solid gray";
		div2.style.backgroundColor = "gray";
		// 设置样式
		div1.style.float = "left";
		div2.style.float = "left";
		div.style.cursor = "pointer";
		div.style.border = "1px solid gray";
		div.style.backgroundColor = "white";
		
		var con = document.getElementById("container");
		var con1 = document.getElementById("container1");
		con1.style.display="none";
		// 绑定事件,点击一次放大两级
		div1.onclick = function(e){
			//mp.setZoom(mp.getZoom() + 2);
			/*con.style.display = "block";
			con1.style.display = "none";*/
			con.style.width = "100%";
			con1.style.display="none";
			div1.style.backgroundColor = "white";
			div2.style.backgroundColor = "gray";
		}
		div2.onclick = function(e){
			//mp.setZoom(mp.getZoom() + 2);
			/*con.style.display = "none";
			con1.style.display = "block";*/
			con.style.width = "70%";
			con1.style.display="";
			div1.style.backgroundColor = "gray";
			div2.style.backgroundColor = "white";
		}
		// 添加DOM元素到地图中
		mp.getContainer().appendChild(div);
		// 将DOM元素返回
		return div;
	}
	mp.addControl(new SwitchControl());
}
 
function loadScript() {
	var script = document.createElement("script");
	script.src = "http://api.map.baidu.com/api?v=2.0&ak=QWnEot7LK6Uo3CC5FWTf9HEx&callback=initialize";//此为v1.5版本的引用方式
	// http://api.map.baidu.com/api?v=1.5&amp;ak=您的密钥&amp;callback=initialize"; //此为v1.4版本及以前版本的引用方式
	document.body.appendChild(script);
}
 
window.onload = loadScript;
</script>
</html>