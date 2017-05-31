<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>preview</title>
  	<meta charset="utf-8" />
    <meta name="description" content="漂流 盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="keywords" content="漂流 盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="author" content="盘龙溪漂流" />
    
    <link rel="icon" href="images/PLXlogo.Ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="images/PLXlogo.Ico" type="image/x-icon" />
    <link rel="stylesheet" href="css/style.css" />
     <link rel="stylesheet" href="css/demo.css" />
     <link rel="stylesheet" href="css/animation.css">
	<link rel="stylesheet" href="css/index.css">
    <script src="js/jquery-1.7.1.min.js"></script>
    <script src="js/script.js"></script>
    <script src="js/headScript.js"></script>
<!--[if lt IE 9]>
	<script src="js/html5.js"></script>
	<link rel="stylesheet" href="css/ie.css"> 
<![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>
<body id="page1">
    <footer>
        <div class="main">
            <div class="container_24">
            	<div class="wrapper">
                	<article class="grid_10">
                    	<h6 class="p11">导航栏</h6>
                       <article class="grid_4 alpha">
                        <ul class="list1">
                            <li><a href="index.jsp">主页</a></li>
                            <li><a href="introduction.do">简介</a></li>
                            <li><a href="photos.do">相册</a></li>
                            <li><a href="video.do">视频</a></li>
                        </ul>
                        </article>
                        <article class="grid_4 push_1">
                        	<ul class="list1">
                            <li><a href="news.do">动态</a></li>
                            <li class="last1"><a href="contact.do">联系我们</a></li>
                        </ul>
                        </article>
                    </article>
                    <article class="grid_9">
                    	<h6 class="p11">帮助</h6>
                       <article class="grid_4 alpha"> 
                       <ul class="list1">
                       		<li><a href="trafficMap.do">交通地图</a></li>
                            <li><a href="navigation.do">自驾导航 </a></li>
                            <li><a href="tripTips.do">漂流须知</a></li>
                        </ul>
                        </article>
                        <article class="grid_4 omega push_1">
                        	<ul class="list1">
                                <li><a href="trafficMap.do">交通地图</a></li>
                                <li><a href="navigation.do">自驾导航 </a></li>
                            </ul>
                        </article>
                    </article>
                    <article class="grid_4 push_1">
                    	<h6 class="p11">其他</h6>
                        <ul class="list1">
                            <li><a href="http://www.tongshan.gov.cn/" target="_blank">通山政务网</a></li>
                            <li><a href="http://www.weather.com.cn/weather/101200706.shtml" target="_blank">天气预报</a></li>
                            <li><a href="http://www.tstour.gov.cn/" target="_blank">通山旅游网</a></li>
                            <li class="last1"><a href="http://www.jiugongshan.gov.cn/" target="_blank">九宫山</a></li>
                        </ul>
                    </article>
                </div>
            </div>
        </div>
        <div class="foot-page"> 
        <div style="text-align: center;">通山大幕山盘龙溪漂流有限公司 © 版权所有 鄂ICP备14006686 
        	<a href='http://221.232.141.246/iciaicweb/dzbscheck.do?method=change&id=E2014091100058164'>
        		<img style="width:20px; height:20px" alt='网络经济主体信息' border='0' DRAGOVER='true' src='images/gslogo.png' />
        	</a>
        </div>
        <div class="fright"><!-- {%FOOTER_LINK} --></div>
        <div class="clear"></div>
        </div>
    </footer>

</body>
</html>