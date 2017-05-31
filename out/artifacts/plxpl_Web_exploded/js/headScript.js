// Panel
$(function(){
	var advancedDiv = '<div id="advanced">'+
	  				'<span class="trigger">'+
	  					'<strong></strong><em></em>'+
	  				'</span>'+
	  			  '<div class="bg_pro">'+
	  			  	'<div class="pro_main">'+
	  			  		'<ul class="pro_menu">'+
	  			  			'<li><a href="index.jsp"><img src="images/pro_images/pro_home.png" alt=""></a></li>'+
	  			  			'<li><a href="#">景区介绍<span></span></a>'+
	  			  				'<ul>'+
	  			  					'<li><a href="introduction.do">景区简介</a></li>'+
	  			  					'<li><a href="photos.do">景区图库</a></li>'+
	  			  					'<li><a href="video.do">景区视频</a></li>'+
	  			  				'</ul>'+
	  			  			'</li>'+
	  			  			'<li><a href="news.do">景区动态</a></li>'+
	  			  			'<li><a href="#">景区指南<span></span></a>'+
	  			  				'<ul>'+
	  			  					'<li><a href="trafficMap.do">交通地图</a></li>'+
	  			  					'<li><a href="navigation.do">自驾导航</a></li>'+
	  			  				'</ul>'+
	  			  			'</li>'+
	  			  			'<li><a href="contact.do">联系我们</a></li>'+
	  			  		'<li><a href="tripTips.do">漂流须知</a></li>'+
	  			  		'</ul>'+
	  			  	'<div class="pro_clear"></div>'+
	  			   '</div>'+
	  			  '</div>'+
	  			 '<div class="bg_pro2"></div>'+
	  			 '</div>';
$('body').prepend(advancedDiv);

	$(window).scroll(function(){if ($(this).scrollTop() > 0) {$("#advanced").css({position:'fixed'});} else {$("#advanced").css({position:'relative'});}});
	$.cookie("panel");	
	$.cookie("panel2");	
	var strCookies = $.cookie("panel");
	var strCookies2 = $.cookie("panel2");
	if(strCookies=='boo')
	{
		if(strCookies2=='opened'){$("#advanced").css({marginTop:'0px'}).removeClass('closed')}else{$("#advanced").css({marginTop:'-40px'}).addClass('closed')}
		$("#advanced .trigger").toggle(function(){
			$(this).find('strong').animate({opacity:0}).parent().parent('#advanced').removeClass('closed').animate({marginTop:'0px'},"fast");
			strCookies2=$.cookie("panel2",'opened');
			strCookies=$.cookie("panel",null);},
		function(){
			$(this).find('strong').animate({opacity:1}).parent().parent('#advanced').addClass('closed').animate({marginTop:'-40px'},"fast")
			strCookies2=$.cookie("panel2",null);
			strCookies=$.cookie("panel",'boo');});
		if ($(window).scrollTop() > 0) {$("#advanced").css({position:'fixed'});}else {$("#advanced").css({position:'relative'});}
	}
	else
	{
		$("#advanced").css({marginTop:'0px'}).removeClass('closed');
		$("#advanced .trigger").toggle(function(){
			$(this).find('strong').animate({opacity:1}).parent().parent('#advanced').addClass('closed').animate({marginTop:'-40px'},"fast");
			strCookies2=$.cookie("panel2",null);
			strCookies=$.cookie("panel",'boo');},
		function(){
			$(this).find('strong').animate({opacity:0}).parent().parent('#advanced').removeClass('closed').animate({marginTop:'0px'},"fast")
			strCookies2=$.cookie("panel2",'opened');
			strCookies=$.cookie("panel",null);});
		if ($(window).scrollTop() > 0) {$("#advanced").css({position:'fixed'});} else {$("#advanced").css({position:'relative'});}
	}
	// Main Slider
	$('.slider')._TMS({
		show:0,
		pauseOnHover:false,
		duration:1000,
		preset:'simpleFade',
		prevBu:'.prev',
		nextBu:'.next',
		pagination:false,
		pagNums:false,
		slideshow:7000,
		numStatus:false,
		banners:'fade',// fromLeft, fromRight, fromTop, fromBottom
		waitBannerAnimation:false})	
});