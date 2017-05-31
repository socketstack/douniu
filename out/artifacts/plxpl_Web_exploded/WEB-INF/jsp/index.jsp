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
<body id="page1" style="font-size: 13px;">
<!--==============================header=================================-->
    <header>    
    	<div class="main">
            <div class="slider-bg">
                <div class="slider">
                	<div style="position: absolute;"><img src="images/PLXlogo.png" alt=""/></div>
                    <ul class="items">
                        <li><img src="images/slide1.jpg" alt="" /><div class="banner"><span class="text1">盘龙溪</span> <span class="text3">漂流</span> <span class="text2">安全第一、惊险刺激！</span></div></li>
                        <li><img src="images/slide2.jpg" alt="" /><div class="banner"><span class="text1">盘龙溪</span> <span class="text3">漂流</span> <span class="text2">安全第一、惊险刺激！</span></div></li>
                        <li><img src="images/slide3.jpg" alt="" /><div class="banner"><span class="text1">盘龙溪</span> <span class="text3">漂流</span> <span class="text2">安全第一、惊险刺激！</span></div></li>
                    </ul>
                    <a href="#" class="prev"></a> <a href="#" class="next"></a>
                </div>
                <div class="clear"></div>
            </div>
        </div>
    </header>
    
    <!--==============================sideBar================================-->
    <jsp:include page="sideBar.jsp" flush="true">  
	  <jsp:param name="view" value="action"/>
	</jsp:include>
	<!--==============================content================================-->
    <section id="content">
        <div class="main">
        	<div class="page1-box1">
            	<figure class="page1-img1 img-bot1"><img src="images/page11-img11.jpg" alt="" /></figure>
                <div class="extra-wrap">
                	<span>了解 <strong>更多</strong> 关于盘龙溪</span>
                    <p>通山大幕山盘龙溪漂流景区位于国家级风景区九宫山北麓省级森林公园--大幕山森林公园脚下。 <br />距武汉市120公里，距咸宁市50公里，距省地质公园隐水洞8公里，距杭瑞高速黄沙入口5公里...</p>
                    <a class="button1" href="introduction.do">更多</a>
                </div>
            </div>
            <div class="container_24">
                <div class="wrapper p10">
                    <article class="grid_8">
                    	<h3>精彩视频 </h3>
                        <div class="page1-box2" style="overflow: visible;">
                        	<div class="page1-img2 img-bot">
					            <video id="myVideo" controls preload="auto" poster="images/slide2.jpg">
					                <source src="movie/plx.mp4" type="video/mp4"/>
					            </video>
					        </div>
                            <div class="clear"></div>
                            <p class="p6">盘龙溪全程3公里，落差186米，有48级跌水，连续跌水达138米， <br />惊险刺激！全程无日晒竹海漂流，耗时约2小时左右。</p>
                            <a class="button2" href="video.do">更多</a>
                        </div>
                    </article>
                    <article class="grid_7 push_2">
                    	<h3 class="p7">景区动态</h3>
                        <div class="page1-box3">
                        	<span class="text4">2016年度盛大开漂</span>
                            <strong>2016年将于5月18号开漂. </strong>
                            <p class="p8">本景区将于今年5月18号开漂，届时欢迎光临...</p>
                        </div>
                        <div class="page1-box3">
                        	<span class="text4">微信公众号开通</span>
                            <strong>我景区微信公众号已开通</strong>
                            <p class="p8">我景区微信公众号已开通,欢迎关注... <br /> </p>
                        </div>
                        <div class="page1-box3 last1">
                        	<span class="text4">网站改版</span>
                            <strong>我景区官网已经改版上线. </strong>
                            <p class="p8">经过数月时间的该本，我景区新版官网已经正式上线... </p>
                        </div>
                        <a class="button2" href="news.do">更多</a>
                    </article>
                    <article class="grid_6 push_3">
                    	<h3>景区图库</h3>
                        <article class="grid_3 alpha"><a href="images/img11.jpg" data-gal="prettyPhoto[gallery]" class=" lightbox-image page1-img3 img-bot"><img src="images/img11_min.jpg" alt="" /></a></article>
                       <article class="grid_3 omega"><a href="images/img22.jpg" data-gal="prettyPhoto[gallery]" class=" lightbox-image page1-img3 img-bot"><img src="images/img22_min.jpg" alt="" /></a></article>
                        <div class="clear"></div>
                         <article class="grid_3 alpha"><a href="images/img33.jpg" data-gal="prettyPhoto[gallery]" class=" lightbox-image page1-img3 img-bot"><img src="images/img33_min.jpg" alt="" /></a></article>
                       <article class="grid_3 omega"><a href="images/img44.jpg" data-gal="prettyPhoto[gallery]" class=" lightbox-image page1-img3 img-bot"><img src="images/img44_min.jpg" alt="" /></a></article>
                        <div class="clear"></div>
                         <article class="grid_3 alpha"><a href="images/img55.jpg" data-gal="prettyPhoto[gallery]" class=" lightbox-image page1-img3 img-bot"><img src="images/img55_min.jpg" alt="" /></a></article>
                       <article class="grid_3 omega"><a href="images/img66.jpg" data-gal="prettyPhoto[gallery]" class=" lightbox-image page1-img3 img-bot"><img src="images/img66_min.jpg" alt="" /></a></article>
                        <div class="clear"></div>
                        <a class="button2 p9" href="photos.do">更多</a>
                    </article>
                </div>
            </div>
            <hr style="margin-top:20px">
            <div class="container_24">
	            <div class="wrapper p12">
	            		<article class="grid_8">
	                    	<h3>联系我们</h3>
	                        <div class="page2-box1">
	                        	<figure class="page2-img2 img-bot"><img src="images/page1_4.jpg" alt="" style="width: 235px;"/></figure>
	                            <div class="clear"></div>
	                            	<ul>
	                            		<li>通山大幕山盘龙溪漂流有限公司</li>
										<li>地址：湖北省通山县黄沙铺镇源头村</li>
										<li>联系人：孟经理</li>
										<li>电话：0715-2335998</li>
										<li>传真：027-87575212</li>
										<li>手机：13477788266</li>
										<li>手机：15007167302</li>
	                            	</ul>
	                            <a class="button2" href="contact.do">查看更多</a>
	                        </div>
	                    </article>
	                    <article class="grid_14 push_2">
	                    	<h3>漂流小知识</h3>
	                        <div class="page2-box2">
	                        	<figure class="page2-img2 img-bot"><img src="images/img66_min.jpg" alt="" style="width: 150px;"/></figure>
	                            <div class="extra-wrap">
	                                <p>漂流前请认真阅读漂流注意事项，漂流时请听从安全员的指挥，不听指挥者安全员有权停止其漂流活动。有精神病、高血压、痴呆症、心脏病、身体残疾不健康、饮酒后、怀孕及骨质疏松症的病史严禁漂流...
	                            </div>
	                            <a class="button2" href="tripTips.do">更多</a>
	                            <div class="clear"></div>
	                        </div>
	                        <div class="page2-box2 last1">
		                    	<h3>交通地图</h3>
	                        	<figure class="page2-img2 img-bot"><img src="images/trafficMap.jpg" alt="" style="width: 150px;"/></figure>
	                            <div class="extra-wrap">
	                                <p>请根据您的实际情况选择合适的线路.</p>
	                            </div>
	                            <a class="button2" href="trafficMap.do">查看</a>
	                            <div class="clear"></div>
	                        </div>
	                    </article>
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