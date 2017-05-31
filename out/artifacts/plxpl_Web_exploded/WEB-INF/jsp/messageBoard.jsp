<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String jspPath = path+"/WEB-INF/jsp/";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<title>联系我们</title>
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
.list {
    font-size: 14px;
    text-align: left;
    border-bottom:solid 2px #CFD2D7;
	line-height:30px;
}
.list ul li {
    font-weight: bold; 
	font-size: larger; 
	color: black;
	padding-bottom:20px;
}

.list span{
	font-weight: bold; 
	font-size: larger; 
	color: black;
}
</style>
</head>
<body id="page1">
	<header>    
         <div class="slider-bg1">
         	<div style="position: absolute;"><img src="images/PLXlogo.png" alt=""/></div>
            <figure class="page2-img1"><img src="images/pho_img_bg.jpg" alt="" /></figure>
        </div>          
    </header>
    
	<!--==============================content================================-->
    <section id="content">
        <div class="main">
        	<div class="container_24">
				<div class="wrapper p18">
                	<article class="grid_13" style="margin-top: 30px;">
                    	<h3>联系我们</h3>
                        <div class="extra-wrap list">
                         <ul>
                          	<li>通山大幕山盘龙溪漂流有限公司</li>
							<li>地址：湖北省通山县黄沙铺镇源头村</li>
							<li>联系人：孟经理</li>
							<li>电话：0715-2335998</li>
							<li>传真：027-87575212</li>
							<li>手机：13477788266</li>
							<li>手机：15007167302</li>
                          </ul>
                        </div>
                        <div class="clear"></div>
                    </article>
                    <article class="grid_9 push_2" style="margin-top: 30px;">
                    	<h3>发送邮件</h3>
                        <form id="form1" >
                        <div class="success">Contact form submitted!<strong> We will be in touch soon.</strong></div>
                          <fieldset>
                              <label class="name">
                                    <input type="text" value="名称:" />
                                    <span class="error">*请输入正确的名称.</span> <span class="empty">*需要输入名称.</span> 
                                </label>                                        
                                <label class="email">
                                    <input type="text" value="用户邮箱:" />
                                    <span class="error">*不是合法的邮箱地址.</span> <span class="empty">*需要输入邮箱地址.</span> 
                                </label> 
                                 <label class="phone">
                                    <input type="tel" value="手机号:" />
                                    <span class="error">*不是合法的手机号.</span> <span class="empty">*需要输入手机号.</span> 
                                </label>                                                                    
                               <label class="message">
                              <textarea>消息内容:</textarea>
                              <span class="error">*消息太短.</span> <span class="empty">*请输入消息内容.</span> </label>
                              <div class="clear"></div>
                              <div class="link-form">
                              <a class="button2 p17" href="#" data-type="reset">清除</a> 
                              <a class="button2" href="#" data-type="submit">发送</a> 
                              </div>											
                          </fieldset>           
                      </form>
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