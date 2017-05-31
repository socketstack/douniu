<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
    <style type="text/css">
    	ol{padding-left: 5px;}
	  	ol li{
		  	list-style-type:decimal;
		    list-style-position:inside;
		  	line-height: 1.5; 
		  	font-size: 0.6em;
	  	}
	  	ul{padding-left: 5px;}
	  	ul li{
		  	list-style-type:disc;
		    list-style-position:inside;
		  	line-height: 2; 
		  	font-size: large;
	  	}
	  	.btn
			{
				width:50%;
				height:1.5em;
				color:#ff3500;
				font-size:1em;
				font-weight:bold;
			}
		.footer{
			position:fixed;
			bottom:0;
			width:12.42rem;
			height:1.58rem;
			background-color:#f2f2f2;
			overflow:hidden;
		}
		span{
			font-size:0.7em;
			font-family:"楷体";
			margin-left:20px;
		}
		.span0{
			font-weight:bold;
		}
		.span1{
			font-size:0.5em;
			color:red;
		}
		.span2{
			font-size:0.5em;
			text-decoration:line-through;
		}
		hr{
   			margin-top: 5px;
    		margin-bottom: 1px;
		}
		
		.favorable input{
			font-size:1em;
			height: 1em;
			width:50%;
		}
		
		.gw_num{border: 5px solid #dbdbdb;width: 100%;line-height: 1em;overflow: hidden;font-size:1.5em;text-align: center;}
		.gw_num em{display: block;height: 1em;width: 25%;float: left;color: #7A7979;border-right: 5px solid #dbdbdb;text-align: center;cursor: pointer;}
		.gw_num .num{display: block;float: left;text-align: center;width: 45%;font-style: normal;font-size: 1em;font-weight:bold;line-height: 1em;border: 1px;}
		.gw_num em.add{float: right;border-right: 1px;border-left: 5px solid #dbdbdb;}
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
		
		<div>
			<hr>
			<span>盘龙溪漂流门票</span>
			<span class="span1">(¥${product.price}/人</span><span class="span2">原价¥195.0/人)</span>
			<br>
			<span>购买数量：</span>
			<div id="totalFee" style="float:right;font-size:0.7em;color:red;margin-right:0.24em;">¥</div><span style="float:right;">总价：</span>
			<div class="gw_num">
				<em class="jian">-</em>
				<input id="count" type="text" value="1" class="num"/>
				<em class="add">+</em>
			</div>
		</div>
		
		<div class="favorable" style="border-radius:30px;background:green;margin-top:5px;">
		<h3>输入姓名和电话</h3>
		<div><span>姓名：</span><input id="name"></input></div>
		<div><span>电话：</span><input id="phone"></input></div>
		</div>
		
		<div class="favorable" style="border-radius:30px;background:green;margin-top:5px;">
			<h3>购买须知</h3>
			<ol>
				<c:if test="${hasLuckyVoucher==true}"><li style="color:yellow;">***您有一张《${luckyVoucherInfoVo.comment }》优惠券，您下单付款之前会自动做相应的扣减!</li></c:if>
				<li style="color:yellow;">请务必提前一天电话景区预约！</li>
				<li style="color:yellow;">请购买门票的游客在下午2点前赶到景区兑换门票</li>
				<li style="color:yellow;">请凭以上填写的姓名和电话兑换门票</li>
				<li>门票包含内容包括：漂流、洗漱、寄存、景区内巴士。</li>
				<li>用餐：自理</li>
			</ol>
		</div>
	</div>
	
	<div class="footer" style="text-align:center;">
		<button type="button" class="btn" onclick="pay()">购买</button>
	</div>	    
	
	<script type="text/javascript">
		var code;
		var price = parseFloat("${product.price}");
		$(document).ready(function(){
			//加的效果
			$(".add").click(function(){
				var n=$(this).prev().val();
				var num=parseInt(n)+1;
				if(num==0){ return;}
				$(this).prev().val(num);
				var cnt = $("#count").val();
				$("#totalFee").empty();
				$("#totalFee").append("¥"+(cnt*price*100)/100);
			});
			//减的效果
			$(".jian").click(function(){
				var n=$(this).next().val();
				var num=parseInt(n)-1;
				if(num==0){ return;}
				$(this).next().val(num);
				var cnt = $("#count").val();
				$("#totalFee").empty();
				$("#totalFee").append("¥"+(cnt*price*100)/100);
			});
			
			//获取微信支付pay code
			code = "${code}";
			var cnt = $("#count").val();
			$("#totalFee").append(cnt*price);
		});
	
		function pay(){
			var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i) ;
			if( !wechatInfo ) {
			    alert("仅支持微信") ;
			    return;
			} else if ( wechatInfo[1] < "5.0" ) {
			    alert("仅支持微信5.0以上版本") ;
			    return;
			}
			var name = $("#name").val();
			var phone = $("#phone").val();
			if(name==""||name==null||phone==""||phone==null){
				alert("必须输入姓名和手机号码，凭此兑票！");
				return;
			}
			if(!confirm("请牢记您填写的姓名和电话，凭此到景区兑换门票!")){
				return;
			}
			var openid = "${openid}";
			var url = "pay/prePay.do";
				$.ajax({
		             type: "POST",
		             url: url,
		             data: {productId:'${product.id}',
		             		count:$("#count").val(),
		             		code:code,
		             		name:name,
		             		phone:phone},
		             dataType: "json",
		             success: function(data){
		//             				 var obj = eval("("+data+")");
			                         var isSuccess = data.isSuccess;
			                         if(isSuccess==true){
			                         	onBridgeReady(data.obj,data.obj1);
			                         }else{
			                         	alert("false,"+data.error);
			                         }
			                      }
			         });
			}
			
//			location.href='pay/prePay.do?productId=${product.id}';
			function onBridgeReady(wxJsPayRequest,tradeId){
				   WeixinJSBridge.invoke(
				       'getBrandWCPayRequest', {
				           "appId":wxJsPayRequest.appId,     //公众号名称，由商户传入     
				           "timeStamp":wxJsPayRequest.timeStamp,         //时间戳，自1970年以来的秒数     
				           "nonceStr":wxJsPayRequest.nonceStr, //随机串     
				           "package":wxJsPayRequest.wxPackage,     
				           "signType":wxJsPayRequest.signType,         //微信签名方式：     
				           "paySign":wxJsPayRequest.paySign//微信签名 
				       },
				       function(res){    
				           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
				            // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
				            alert("支付成功！");
				            location.href='pay/queryTrade.do?tradeId='+tradeId+'&payed=true';
				           }else{
				           	alert("支付失败！");
				           }
				       }
				   ); 
				}
//				if (typeof WeixinJSBridge == "undefined"){
//				   if( document.addEventListener ){
//				       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
//				   }else if (document.attachEvent){
//				       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
//				       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
//				   }
//				}else{
//					alert(11);
//				   onBridgeReady();
//				}
	</script>
</body>
</html>