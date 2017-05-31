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
		
	</style>
    
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
		
		
		<div style="width:inherit;border-radius:0.5em;background:green;margin-top:0.1em;margin-left:0.05em;margin-right:0.05em;">
			<h3 style="text-align: center; margin-bottom: 1px; padding-top: 10px;">景区简介</h3>
			            <div class="grid_24" style="line-height: 2; font-size: 0.7em; padding-top: 25px;">
		            	    <div>
						        <div>
						            <img src="images/itr_img1.jpg" alt="" style="width: 100%;">
						        </div>
						        <p> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp通山大幕山盘龙溪漂流有限公司位于国家级风景区九宫山北麓省级森林公园--大幕山森林公园脚下。
			                方圆2平方公里。距武汉市120公里，距咸宁市50公里，距省地质公园隐水洞8公里，距杭瑞高速黄沙入口5公里，
			                交通十分便利，是您节假日旅行探幽的首选之地。
			                这里群山环绕、碧水如玉、峰峦叠翠、古木参天、竹海生涛，尽展大自然的旖旎风光和神奇风韵，被誉为天然的大氧吧。</p>
						    </div>
			                <p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp盘龙溪全程3公里，落差186米，有48级跌水，连续跌水达138米，惊险刺激！全程无日晒竹海漂流，耗时约2小时左右。</p>
			                <p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp漂流过程可享受以下三大特色。</p>
			                <div class="pro_wrapper">
						        <div>
						            <img src="images/itr_img2.jpg" alt="" style="width: 100%;">
						        </div>
				                <p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp特色一，竹篁通幽：盘龙溪源头峰岭叠翠，蜿蜒起伏，秀竹连绵，郁郁葱葱。这里是竹的世界，竹的海洋！
				                “瞻彼淇奥，绿竹猗猗”、“余处幽篁兮终不见天”。来到这里，顿觉神清气爽，俗虑全消，让你深深体会：
				                “无竹令人瘦，无竹令人俗”；“家中虽有八珍尝，那及山家竹笋香”的意境！源头水流平缓，清澈见底，甘甜清爽。
				                                          两岸绿竹护堤，古木参天，枝颈相牵，编成天然的绿色隧道，让你感觉到：如入仙境博舟，似在画中畅游，宁静而恬淡，
				                                          让人心向神往，流连忘返！</p>
				            </div>
				            <div>
						        <div>
						            <img src="images/itr_img3.jpg" alt="" style="width: 100%;">
						        </div>
				                <p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp特色二， 中流击水：如果说竹篁通幽让您心如止水，来到这里一定让您神情振奋，豪情万丈。这里两岸悬崖壁立，
				                                           水流湍急，有连续138米跌水，让您顿生征服自然，挑战极限，超越自我的雄心壮志！两岸流泉飞瀑为您唱彩，竹影婆娑为您伴舞，
				                                           让您惊叹大自然的博大精深，充分体现您征服自然，驾御自然的智慧和毅力，让您欢笑，让您跳，这份体验挑战的乐趣和激情，
				                                           一定让您终生获益，永世难忘。</p>
			                </div>
			                <div>
						        <div>
						            <img src="images/itr_img4.jpg" alt="" style="width: 100%">
						        </div>
				                <p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp特色三，回归自然：经历了动静结合，刚柔相济的历练，享受了万流奔腾、一泄千里、惊涛骇浪的激越；
				                                            感受了大自然的博大精深和神奇美丽。在这里您尺可抛弃舒适和慵懒，天人合一，回归自然，释放出久积于心的压力，
				                                            舒展紧绷的神经，尽情享受两岸田园风光、村舍茅屋。与自然对话，与碧水亲近，捕捉自然的激越和欣喜！</p>
				            </div>
			                <p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp来吧！朋友，盘龙溪漂流欢迎您！</p>
			            </div>
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