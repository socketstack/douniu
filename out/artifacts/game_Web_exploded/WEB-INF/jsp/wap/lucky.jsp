<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>盘龙溪漂流幸运大转盘抽奖</title>
  	<meta charset="utf-8" />
    <meta name="description" content="漂流 盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="keywords" content="漂流 盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="author" content="盘龙溪漂流" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <link rel="icon" href="images/PLXlogo.ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="images/PLXlogo.Ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%=path%>/css/wap/style.css">
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/wap/lucky/style.css">
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/weui.css">
	
	<script src="<%=path%>/js/wap/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=path%>/js/wap/Adaptive.js"></script>
	<script src="<%=path%>/js/wap/lucky/awardRotate.js" type="text/javascript"></script>
	
    <style type="text/css">
    	ol{margin-left:50px;}
    	li{list-style:decimal inside !important;}  
    </style>
</head>
<body style="background:#e62d2d;overflow-x:hidden;">
<br>
<!-- 代码 开始 -->
<img src="<%=path%>/images/wap/lucky/1.png" id="shan-img" style="display:none;" />
<img src="<%=path%>/images/wap/lucky/2.png" id="sorry-img" style="display:none;" />
<div class="banner">
	<div class="turnplate" style="background-image:url(<%=path%>/images/wap/lucky/turnplate-bg.png);background-size:100% 100%;">
		<canvas class="item" id="wheelcanvas" width="422px" height="422px"></canvas>
		<img class="pointer" src="<%=path%>/images/wap/lucky/turnplate-pointer.png"/>
	</div>
</div>
<!-- 代码 结束 -->

<div style="margin:20px 20px;margin-bottom: 200px;font: normal 30px/35px 'MicroSoft YaHei';color: white;">
	<p>注意事项：</p>
	<ol>
		<li>每人每天只能抽奖${frequencyPerDay }次</li>
		<li>每次抽奖有${luckyRate }的概率中奖</li>
		<li>中奖后有效期为${termOfValid }天</li>
		<li>每人${luckyInTime }天内只能中奖一次</li>
	</ol>
	<hr style="margin-bottom: 10px;margin-top: 10px;">
	<p>您的中奖记录：</p>
	<p>
		<c:if test="${haveLuckyVoucher ==true}">您有一张于${luckyVoucher.luckyTime}
		抽中的《${luckyVoucher.comment }》优惠券,请于${luckyVoucher.deadTime }天内使用!
		兑奖码为:<span style="font-size: 40px;">${luckyVoucher.luckyCode }</span>，可凭次兑奖码到景区购票时兑换，也可以微信购票，直接兑换。
		</c:if>
		<c:if test="${haveLuckyVoucher !=true}">您有效期内没有中奖券！</c:if>
	</p>
</div>
<div style='display:none'>
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__hd"><strong class="weui-dialog__title">弹窗标题</strong></div>
        <div class="weui-dialog__bd">弹窗内容，告知当前页面信息等</div>
        <div class="weui-dialog__ft">
            <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp" flush="true">  
  <jsp:param name="view" value="action"/>
</jsp:include>

</body>
<script type="text/javascript">
		var openid = "${openid}";
	
		var turnplate={
				restaraunts:[],				//大转盘奖品名称
				colors:[],					//大转盘奖品区块对应背景颜色
				outsideRadius:192,			//大转盘外圆的半径
				textRadius:155,				//大转盘奖品位置距离圆心的距离
				insideRadius:68,			//大转盘内圆的半径
				startAngle:0,				//开始角度
				
				luckyVoucherTypes:[],			//中奖券实体对象
				bRotate:false				//false:停止;ture:旋转
		};
		
		$(document).ready(function(){
			
			$.ajax({
				type:'POST',
				url:'wxLucky/queryPrizeTypes.do',
				data:'json',
				success:function(data){
					var jsonObj=eval("("+data+")");  
			        $.each(jsonObj, function (i, item) {
			        	turnplate.restaraunts[2*i+1]="谢谢参与";
			        	turnplate.restaraunts[2*i]=item.comment;
			        	turnplate.luckyVoucherTypes[2*i+1]=null;
			        	turnplate.luckyVoucherTypes[2*i]=item;
			        	
			        	turnplate.colors[2*i]="#FFF4D6";
			        	turnplate.colors[2*i+1]="#FFFFFF";
			        });
			        drawRouletteWheel();
				},
				error:function(data){
					alert(data);
				}
			});
			//动态添加大转盘的奖品与奖品区域背景颜色
//			turnplate.restaraunts = ["二免一", "谢谢参与", "三免一", "谢谢参与", "五折", "谢谢参与", "100元抵用券", "谢谢参与", "八折","谢谢参与"];
//			turnplate.colors = ["#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF","#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF","#FFF4D6", "#FFFFFF"];
			
			var rotateTimeOut = function (){
				$('#wheelcanvas').rotate({
					angle:0,
					animateTo:2160,
					duration:8000,
					callback:function (){
						alert('网络超时，请检查您的网络设置！');
					}
				});
			};
		
			//旋转转盘 item:奖品位置; txt：提示语;
			var rotateFn = function (item, txt, isLucky){
				if(item == -1){
					turnplate.bRotate = !turnplate.bRotate;
					return;
				}
				var angles = item * (360 / turnplate.restaraunts.length) - (360 / (turnplate.restaraunts.length*2));
				if(angles<270){
					angles = 270 - angles; 
				}else{
					angles = 360 - angles + 270;
				}
				$('#wheelcanvas').stopRotate();
				$('#wheelcanvas').rotate({
					angle:0,
					animateTo:angles+1800,
					duration:8000,
					callback:function (){
						alert(txt);
						turnplate.bRotate = !turnplate.bRotate;
						if(isLucky == true){
							window.location.href="wxLucky.do";
						}
					}
				});
			};
		
			$('.pointer').click(function (){
				if(turnplate.bRotate)return;
				turnplate.bRotate = !turnplate.bRotate;
				//获取随机数(奖品个数范围内)
				//var item = rnd(1,turnplate.restaraunts.length);
				var item;
				$.ajax({
					type:'POST',
					url:'wxLucky/luckDraw.do',
					data:{
						openid:openid
					},
					async: false,
					success:function(data){
				        item = data;
				        var jsonObj=eval("("+data+")");  
				        if(jsonObj.isSuccess==false){
				        	alert(jsonObj.value); 
				        	item = -1;
				        }else{
				        	item = jsonObj.value;
				        }
					},
					error:function(data){
						alert(data);
					}
				});
				//奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
				var text;
				var ii = item-1;
				if(item == 0){//防止item为0的时候item-1为-1
					ii = 1;
				}
				if(turnplate.restaraunts[ii]!="谢谢参与"){
					text = "恭喜您中奖，"+turnplate.restaraunts[ii] + "优惠券一张！";
					rotateFn(item, text, true);
				}else{
					text = "很遗憾您没有中奖，"+turnplate.restaraunts[ii];
					rotateFn(item, text, false);
				}
				/* switch (item) {
					case 1:
						rotateFn(252, turnplate.restaraunts[0]);
						break;
					case 2:
						rotateFn(216, turnplate.restaraunts[1]);
						break;
					case 3:
						rotateFn(180, turnplate.restaraunts[2]);
						break;
					case 4:
						rotateFn(144, turnplate.restaraunts[3]);
						break;
					case 5:
						rotateFn(108, turnplate.restaraunts[4]);
						break;
					case 6:
						rotateFn(72, turnplate.restaraunts[5]);
						break;
					case 7:
						rotateFn(36, turnplate.restaraunts[6]);
						break;
					case 8:
						rotateFn(360, turnplate.restaraunts[7]);
						break;
					case 9:
						rotateFn(324, turnplate.restaraunts[8]);
						break;
					case 10:
						rotateFn(288, turnplate.restaraunts[9]);
						break;
				} */
				console.log(item);
			});
		});
		
		function rnd(n, m){
			var random = Math.floor(Math.random()*(m-n+1)+n);
			return random;
			
		}
		
		
		//页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
//		window.onload=function(){
//			drawRouletteWheel();
//		};
		
		function drawRouletteWheel() {    
		  var canvas = document.getElementById("wheelcanvas");    
		  if (canvas.getContext) {
			  //根据奖品个数计算圆周角度
			  var arc = Math.PI / (turnplate.restaraunts.length/2);
			  var ctx = canvas.getContext("2d");
			  //在给定矩形内清空一个矩形
			  ctx.clearRect(0,0,422,422);
			  //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式  
			  ctx.strokeStyle = "#FFBE04";
			  //font 属性设置或返回画布上文本内容的当前字体属性
			  ctx.font = '16px Microsoft YaHei';      
			  for(var i = 0; i < turnplate.restaraunts.length; i++) {       
				  var angle = turnplate.startAngle + i * arc;
				  ctx.fillStyle = turnplate.colors[i];
				  ctx.beginPath();
				  //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）    
				  ctx.arc(211, 211, turnplate.outsideRadius, angle, angle + arc, false);    
				  ctx.arc(211, 211, turnplate.insideRadius, angle + arc, angle, true);
				  ctx.stroke();  
				  ctx.fill();
				  //锁画布(为了保存之前的画布状态)
				  ctx.save();   
				  
				  //----绘制奖品开始----
				  ctx.fillStyle = "#E5302F";
				  var text = turnplate.restaraunts[i];
				  var line_height = 17;
				  //translate方法重新映射画布上的 (0,0) 位置
				  ctx.translate(211 + Math.cos(angle + arc / 2) * turnplate.textRadius, 211 + Math.sin(angle + arc / 2) * turnplate.textRadius);
				  
				  //rotate方法旋转当前的绘图
				  ctx.rotate(angle + arc / 2 + Math.PI / 2);
				  
				  /** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **/
				  if(text.indexOf("M")>0){//流量包
					  var texts = text.split("M");
					  for(var j = 0; j<texts.length; j++){
						  ctx.font = j == 0?'bold 20px Microsoft YaHei':'16px Microsoft YaHei';
						  if(j == 0){
							  ctx.fillText(texts[j]+"M", -ctx.measureText(texts[j]+"M").width / 2, j * line_height);
						  }else{
							  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
						  }
					  }
				  }else if(text.indexOf("M") == -1 && text.length>6){//奖品名称长度超过一定范围 
					  text = text.substring(0,6)+"||"+text.substring(6);
					  var texts = text.split("||");
					  for(var j = 0; j<texts.length; j++){
						  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
					  }
				  }else{
					  //在画布上绘制填色的文本。文本的默认颜色是黑色
					  //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
					  ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
				  }
				  
				  //添加对应图标
				  if(text.indexOf("谢谢参与")>=0){
					  var img= document.getElementById("sorry-img");
					  img.onload=function(){  
						  ctx.drawImage(img,-15,20);      
					  };  
					  ctx.drawImage(img,-15,20); 
				  }else{
					  var img= document.getElementById("shan-img");
					  img.onload=function(){  
						  ctx.drawImage(img,-15,20);      
					  }; 
					  ctx.drawImage(img,-15,20);   
				  }
				  //把当前画布返回（调整）到上一个save()状态之前 
				  ctx.restore();
				  //----绘制奖品结束----
			  }     
		  } 
		}
		
</script>
</html>