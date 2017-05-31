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
	<script src="<%=path%>/layer/layer.js" ></script>
    
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
		<li><a href="#">微信后台</a></li>
		<li>»</li>
		<li class="active">产品编辑</li>
	</ul>
	<button type="button" class="btn btn-primary" style="margin-left:30px" onclick="addProduct()">
                    新增产品
    </button>
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
		                url:'<%=path%>/backend/queryProducts.do',
		                queryParams:{
		                	
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
								title: '名称', 
								align: 'center',
								resizable:true,
								width:100,
								formatter:function(value,row,index){
									return value;
								}
							},
					        { 
					        	field: 'body', 
					        	title: '商品描述', 
					        	align: 'center',
					        	resizable:true,
					        	width:150,
					        	formatter:function(value,row,index){
									return value;
								}
					        },
					        { 
					        	field: 'detail', 
					        	title: '商品详情', 
					        	align: 'center',
					        	resizable:true,
					        	width:200,
					        	formatter:function(value,row,index){
					        	   return value;
								}
					        },
					        { 
					        	field: 'attach', 
					        	title: '附加信息', 
					        	align: 'center',
					        	resizable:true,
					        	width:100,
					        	formatter:function(value,row,index){
					        	   return value;
								}
					        },
					        { 
					        	field: 'price', 
					        	title: '商品单价', 
					        	align: 'center',
					        	resizable:true,
					        	width:200,
					        	formatter:function(value,row,index){
					        	   return value+"元";
								}
					        },
					        { 
					        	field: 'imgSrc', 
					        	title: '路径', 
					        	align: 'center',
					        	resizable:true,
					        	width:200,
					        	formatter:function(value,row,index){
					        	   return value;
								}
					        },
					        { 
					        	field: '编辑', 
					        	title: '编辑', 
					        	align: 'center',
					        	resizable:true,
					        	width:100,
					        	formatter:function(value,row,index){
					        		var	res = "<input type='button' value='编辑' onclick='editeProduct("+row.id+")'/>"+
					        			 		"<input type='button' value='删除' onclick='deleteProduct("+row.id+")'/>";
					        	   return res;
								}
					        }                                                   
		                ]]  
		            });  
		}  
		
		function editeProduct(productId){
			layer.open({
						  type: 2,
						  title: '编辑产品',
						  maxmin: true,
						  shadeClose: false, //点击遮罩关闭层
						  area : ['800px' , '520px'],
						  content: '<%=path%>/backend/editeProduct.do?productId='+productId
				  });
		}
		
		function addProduct(){
			layer.open({
						  type: 2,
						  title: '新增产品',
						  maxmin: true,
						  shadeClose: false, //点击遮罩关闭层
						  area : ['800px' , '520px'],
						  content: '<%=path%>/backend/toAddProduct.do'
				  });
		}
		
		function deleteProduct(productId){
			layer.confirm('确定删除？', {
				  btn: ['确定','取消'] //按钮
				}, function(){
					$.ajax({
		             type: "POST",
		             url: '<%=path%>/backend/deleteProduct.do',
		             data: {productId:productId},
		             dataType: "json",
		             success: function(data){
		//             				 var obj = eval("("+data+")");
			                         var isSuccess = data.isSuccess;
			                         if(isSuccess==true){
			                         	layer.msg('删除成功', {icon: 1});
			                         	loadGrid();
			                         }else{
			                         	layer.msg('删除失败', {icon: 2});
			                         }
			                      }
			         });
				}, function(){
				  return;
				});
		}
		
		function getTicket(id){
			if(!confirm("是否已经兑票！")){
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
			                         	layer.msg('兑票成功', {icon: 1});
			                         }else{
			                         	layer.msg('兑票失败', {icon: 2});
			                         }
			                      }
			         });
			}
	</script>
</body>
</html>