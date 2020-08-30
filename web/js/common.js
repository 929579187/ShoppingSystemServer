var serverPath = "http://localhost:8888/ShoppingSystemServer/apis";
var imageServerPath="http://localhost:8887/file/"
//通过设置允许Ajax携带凭证信息
$.ajaxSetup({
	xhrFields: {
		withCredentials: true
	}
});

//加载页面的通用头部
function loadHead(){
	$.get("head.html",function(html){
		//头部信息拼接到页面中
		$("body").prepend(html);
		
		//为登录操作绑定事件
		$("#logout>a").on("click",function(){
			$.post(serverPath+"/user/logout",function(res){
				if(res.success){
					location.href="login.html";
				}
			});
		});
		
		//获取当前登录的用户信息
		$.get(serverPath+"/user/loginUser",function(res){
			if(res.success){
				$("#name>a").html(res.data.userName);
				$(".gouwuche").show();
			}else{
				//location.href="login.html";
				$(".gouwuche").hide();
			}
		});
	});
}


//获取url中请求参数的具体内容
function getUrlParams(){
	var params={};
	//格式： ["pid=1352516", "param1=1", "param2=2"]
	var paramNameValuePairs=location.search.replace("?","").split("&");
	for(var i=0;i<paramNameValuePairs.length;i++){
		//[pid,1]
		var paramNameValuePair=paramNameValuePairs[i].split("=");
		params[paramNameValuePair[0]]=paramNameValuePair[1];
	}
	return params;
}
