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
	<link href="<%=path%>/css/backend/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
	<link href="<%=path%>/css/backend/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
	<link href="<%=path%>/assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON CSS -->
	<link rel="stylesheet" href="<%=path%>/assets/file-uploader/css/jquery.fileupload.css"><!-- FILE UPLOAD  CSS -->
	<link href="<%=path%>/assets/file-uploader/css/jquery.fileupload-ui.css" rel="stylesheet" type="text/css"><!-- FILE UPLOAD UI CSS -->
	<link href="<%=path%>/css/backend/style.css" rel="stylesheet"><!-- THEME BASIC CSS -->
    <link href="<%=path%>/css/backend/style-responsive.css" rel="stylesheet"><!-- THEME BASIC RESPONSIVE  CSS -->
    
    <link href="<%=path%>/css/backend/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
    <link href="<%=path%>/css/backend/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
    <link href="<%=path%>/assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON STYLESHEET -->
		
	<script src="<%=path%>/js/jquery-1.8.3.min.js" ></script><!-- BASIC JQUERY 1.8.3 LIB. JS -->
	<script type="text/javascript" src="<%=path%>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/js/ajaxfileupload.js"></script>
	<script src="<%=path%>/layer/layer.js" ></script>
	<!--[if lt IE 9]>
    <script src="<%=path%>/js/backend/html5shiv.js"></script>
    <script src="<%=path%>/js/backend/respond.min.js"></script>
    
    <![endif]-->
    <style type="text/css">
    	ul{
    		margin-top:5px;margin-left:35px;
    	}
    	ul li{
    		padding-top:15px
    	}
    	ul li span{
    		display:block;
    	}
    	ul li img{
    		width:150px;
    		height:100px;
    	}
    	.style{
    		
    	}
    </style>
</head>
<body>
	<div style="width:50%;float:left; ">
	<input type="hidden" id="prodcutId" value="${product.id }"/>
		<ul>
			<li><span>产品名称:</span><input id="productName" value="${product.name }"></li>
			<li><span>产品单价:</span><input id="productPrice" value="${product.price }"></li>
			<li><span>产品图片：</span><img id="imgSrc" src="<%=path%>/${product.imgSrc }"/></li>
			<span class="btn btn-success fileinput-button" style="margin-top:15px">
	            <span>上传新图片</span>
	            <input type="file" id="file" name="file" onchange="FileUpload_onchange()">
            </span>
            <button onclick="submitPic();" class="btn btn-primary start" style="margin-top:15px">
                  	<span>开始上传</span>
            </button>
			<div id="picTip"></div>
			<input type="hidden" id="uploadImagesSuccess"/>
		</ul>
	</div>
	<div style="width:50%;float:left; ">
		<ul>
			<li><span>产品描述：</span><textarea rows="3" cols="20" id="productBody">${product.body }</textarea></li>
			<li><span>产品详情:</span><textarea rows="3" cols="20" id="productDetail">${product.detail }</textarea></li>
			<li><span>产品附加信息：</span><textarea rows="3" cols="20" id="productAttach">${product.attach }</textarea></li>
		</ul>
	</div>
	<div style="width:50%;margin-left:auto;margin-right: auto;">
		<button type="button" class="btn btn-info btn-lg btn-block" onclick="save()">
                   	保存
        </button>
	</div>
	
	<script type="text/javascript">
		function save(){
			var prodcutId = $("#prodcutId").val();
			if (prodcutId == null || prodcutId == undefined || prodcutId == '') { 
				layer.alert("修改异常！",{icon:7});
				return;
			}
			var productName = $("#productName").val();
			if (productName == null || productName == undefined || productName == '') { 
				layer.alert('产品名称为空!',{icon:2});
				$("#productName").focus();
			}
			
			var productPrice = $("#productPrice").val();
			if (productPrice == null || productPrice == undefined || productPrice == '') { 
				layer.alert('产品价格为空!',{icon:2});
				$("#productPrice").focus();
			}else if(isNaN(productPrice)){
				layer.alert('产品价格不是数字!',{icon:2});
			}
			
			var imgSrc = $("#imgSrc").attr("src");
			var a = imgSrc.split("/");
			a.splice(0,2);
			imgSrc = a.join("/");
			var productBody = $("#productBody").text();
			var productDetail = $("#productDetail").text();
			var productAttach = $("#productAttach").text();
			
			$.ajax( {  
			    url:'<%=path%>/backend/updateProductInfo.do',// 跳转到 action  
			    data:{  
			    		 id : prodcutId,
			             name : productName,  
			             price : productPrice,  
			             imgSrc : imgSrc,  
			             body : productBody,
			             detail : productDetail,
			             attach : productAttach
			    },  
			    type:'post',  
			    cache:false,  
			    dataType:'json',  
			    success:function(data) {  
			        if(data.isSuccess ==true ){  
			            // view("修改成功！");  
			            layer.alert("修改成功！",{icon:6});
			        }else{  
			             layer.alert("修改失败！",{icon:5});
			        }  
			     },  
			     error : function() {  
			           layer.alert("修改异常！",{icon:7});
			     }  
			});
		}
		
		function FileUpload_onchange()
        {
            var path;
            path=$("#file").val();      //C:\Documents and Settings\hud\桌面\AddFile.jpg
            var aa;
            aa=path.split('.');
            var name;
            name=path.split('\\'); 
            var pictureName=name[name.length-1]; 
            $("#picTip").html("<span style='color:Red'>"+pictureName+"</span>");   
            var namePre = pictureName.substr(0,pictureName.indexOf('.'));       
            document.all('picTip').value=namePre;  //AddFile 结果
        }
        
		//新建或编辑 保存提交  
		function submitPic(){  
//		    if(!$("#picForm").form('validate')){  
//		        return false;  
//		    }  
		    var picName=$("#picTip").val();  
		    var type="1";//展示图片  
		    var f = $("#file").val();  
		    if(f==null||f==""){  
		        $("#picTip").html("<span style='color:Red'>错误提示:上传文件不能为空,请重新选择文件</span>");  
		        return false;  
		      }else{  
		       var extname = f.substring(f.lastIndexOf(".")+1,f.length);  
		       extname = extname.toLowerCase();//处理了大小写  
		       if(extname!= "jpeg"&&extname!= "jpg"&&extname!= "gif"&&extname!= "png"){  
		         $("#picTip").html("<span style='color:Red'>错误提示:格式不正确,支持的图片格式为：JPEG、GIF、PNG！</span>");  
		         return false;  
		        }  
		      }  
		     var file = document.getElementById("file").files;    
		     var size = file[0].size;  
		     if(size>2097152){  
		          $("#picTip").html("<span style='color:Red'>错误提示:所选择的图片太大，图片大小最多支持2M!</span>");   
		          return false;  
		      }  
		    ajaxFileUploadPic(picName,type);  
		}  
		  
		function ajaxFileUploadPic(picName,type) {  
		    $.ajaxFileUpload({  
		        url : '<%=path%>/upload/imageUpload.do?picName='+picName+'&type='+type, //用于文件上传的服务器端请求地址  
		        secureuri : false, //一般设置为false  
		        fileElementId : 'file', //文件上传空间的id属性  <input type="file" id="file" name="file" />  
		        type : 'post',  
		        dataType : 'json', //返回值类型 一般设置为json  
		        success : function(data, status) //服务器成功响应处理函数  
		        {  
//		             $("#picList").datagrid('reload');  
//		             $('#uploadPicWindow').window('close');  
		             if(data.status=="success"){
		             	$("#uploadImagesSuccess").val("yes");
		             	$("#imgSrc").attr("src","<%=path%>/"+data.imgSrc);
		             	layer.alert('上传成功!', {icon: 1});
		             }else{
		             	layer.alert('上传失败!', {icon: 5});
		             }
		        },  
		        error : function(data, status, e)//服务器响应失败处理函数  
		        {  
//		             $("#picList").datagrid('reload');  
//		             $('#uploadPicWindow').window('close');  
		             alert(data.status);  
		        }  
		    });  
		    return false;  
		}  

	</script>
</body>
</html>