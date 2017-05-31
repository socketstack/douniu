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
    <meta name="description" content="盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="keywords" content="盘龙溪漂 湖北漂流 流 湖北旅游 夏季旅游" />
    <meta name="author" content="盘龙溪漂流" />
    
    <link rel="icon" href="images/PLXlogo.Ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="images/PLXlogo.Ico" type="image/x-icon" />
    <link rel="stylesheet" href="css/animation.css">
	<link rel="stylesheet" href="css/index.css">
    <script src="js/jquery-1.7.1.min.js"></script>
<!--[if lt IE 9]>
	<script src="js/html5.js"></script>
	<link rel="stylesheet" href="css/ie.css"> 
<![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>
<body>
<div>
<!--效果html开始-->
<div id="code"></div>
<div id="code_img" style="z-index: 100;"></div>
<a id="gotop" href="javascript:void(0)"><img src="images/fhjt.png" alt=""/></a>
<!--效果html结束-->
<div class="clear"></div>
</div>
</body>
<script type="text/javascript">
    function b(){
        h = $(window).height();
        t = $(document).scrollTop();
        if(t > 0){
            $('#gotop').fadeIn(200);
        }else{
            $('#gotop').fadeOut(200);
        }
    }
    $(document).ready(function(e) {
        b();
        $('#gotop').click(function(){
			$("body,html").animate({scrollTop:0},300);
        });

        $('#code').hover(function(){
            $(this).attr('id','code_hover');
            $('#code_img').show();
            $('#code_img').addClass('a-fadeinL');
        },function(){
            $(this).attr('id','code');
            $('#code_img').hide();
        });

    });

    $(window).scroll(function(e){
        b();
    });
</script>
</html>