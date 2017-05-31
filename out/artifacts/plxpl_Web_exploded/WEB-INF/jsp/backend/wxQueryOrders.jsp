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
    
    <link rel="icon" href="<%=path%>/images/PLXlogo.ico" type="image/x-icon" />    
    <link rel="shortcut icon" href="<%=path%>/images/PLXlogo.Ico" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/themes/icon.css"> 
	
	<script src="<%=path%>/js/jquery-1.8.3.min.js" ></script><!-- BASIC JQUERY 1.8.3 LIB. JS -->
	<script type="text/javascript" src="<%=path%>/js/jquery.easyui.min.js"></script>
	
    
</head>
<body>
	<div style="padding-top:70px"></div>
	<ul class="breadcrumb">
		<li>
			<a href="#">
				<i class="fa fa-home">
				</i>
				主页
			</a>
		</li>
		<li>»</li>
		<li><a href="#">销售系统<a></li>
		<li>»</li>
		<li class="active">查看编辑订单</li>
	</ul>
	<label>姓名：</label><input id="name"/>
	<label>电话：</label><input id="phone"/>
	<label>下单时间：</label>
	<input id="startTime" type="text" onfocus="WdatePicker()"/>
	~~
	<input id="endTime" type="text" onfocus="WdatePicker()"/>
	<input type="button" value="查询" onclick="query()"/>
	<hr>
	<table id="cxdm"></table>  
	<script type="text/javascript">
		
		//页面加载  
		$(document).ready(function(){  
		            loadGrid();  
		});  
		
		function query(){
			loadGrid();
		}
		  
		//加载表格datagrid  
		function loadGrid()  
		{  
			var name = $("#name").val();
			var phone = $("#phone").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
		    //加载数据  
		    $('#cxdm').datagrid({  
		                width: 'auto',  
		                height:320, 
		                fix:true,              
		                striped: true, 
		                fitColumn: false, 
		                singleSelect : true,  
		                url:'../queryTickes.do',
		                queryParams:{
		                	name:name,
		                	phone:phone,
		                	startTime:startTime,
		                	endTime:endTime
		                },  
		                loadMsg:'数据加载中请稍后……',  
		                pagination: true,  
		                rownumbers: true,
		                pageNumber:1,
						pageSize:10,
						pageList: [10, 20, 50, 100, 150, 200],     
		                columns:[[  
		                    { 
								field: 'name', 
								title: '名字', 
								align: 'center',
								resizable:true,
								width:100,
								formatter:function(value,row,index){
									return value;
								}
							},
					        { 
					        	field: 'phone', 
					        	title: '电话号码', 
					        	align: 'center',
					        	resizable:true,
					        	width:150,
					        	formatter:function(value,row,index){
									return value;
								}
					        },
					        { 
					        	field: 'createTimeStr', 
					        	title: '购买时间', 
					        	align: 'center',
					        	resizable:true,
					        	width:200,
					        	formatter:function(value,row,index){
					        	   return value;
								}
					        },
					        { 
					        	field: 'count', 
					        	title: '票数', 
					        	align: 'center',
					        	resizable:true,
					        	width:100,
					        	formatter:function(value,row,index){
					        	   return value;
								}
					        },
					        { 
					        	field: 'totalFee', 
					        	title: '金额', 
					        	align: 'center',
					        	resizable:true,
					        	width:200,
					        	formatter:function(value,row,index){
					        	   return value+"元";
								}
					        },
					        { 
					        	field: '是否使用', 
					        	title: '是否使用', 
					        	align: 'center',
					        	resizable:true,
					        	width:200,
					        	formatter:function(value,row,index){
					        	   var res = "未知状态!";
					        	   if(row.state==0){
					        	   	res = "未支付！";
					        	   }else if(row.state==1){
					        	    res = "<font style='color:red'>已支付！</font>";
					        	   }else if(row.state==2){
					        	   	res = "支付失败！";
					        	   }else if(row.state==3){
					        	   	res = "<font style='color:#cccccc'>已经兑票！</font>";
					        	   }
					        	   return res;
								}
					        },
					        { 
					        	field: '编辑', 
					        	title: '编辑', 
					        	align: 'center',
					        	resizable:true,
					        	width:100,
					        	formatter:function(value,row,index){
					        		var res = "";
					        		if(row.state==1){
					        			 res = "<input type='button' value='兑票' onclick='getTicket("+row.id+")'/>";
					        		}
					        	   return res;
								}
					        }                                                   
		                ]]  
		            });  
		}  
		
		function getTicket(id){
			if(!confirm("确定兑票？")){
				return;
			}
			var url = "../pay/getTicket.do";
				$.ajax({
		             type: "POST",
		             url: url,
		             data: {tradeInfoId:id},
		             dataType: "json",
		             success: function(data){
		//             				 var obj = eval("("+data+")");
			                         var isSuccess = data.isSuccess;
			                         if(isSuccess==true){
			                         	alert("兑票成功！");
			                         }else{
			                         	alert("兑票失败！");
			                         }
			                      }
			         });
			}
	</script>
</body>
</html>