/**
 * 
 */
import { globalLoading, globalTips } from "./load.js";
$(document).ready(function() {
	$("#loginForm").validate({
	  	 focusCleanup:true,
	  	   focusInvalid:false,
	  	   wrapper:"span",
		    rules: {
		      email: {
		        required: true,
		        email: true
		      },
			 password:{
			 	required: true,
				minlength: 6
			 }
	   	 },
	    messages: {
		     email: "请输入有效的电子邮件地址",
		     password:{
		     	required:"请输入密码",
		     	minlength: "最少要输入&nbsp;&nbsp;6&nbsp;&nbsp;位密码"
		     }
	    },
    	submitHandler: function() {
	      $.ajax({
	    	url:"LoginGeneralAction_login",
	      	type:"post",
	      	dataType: "json",
	      	data:{
	      		"email" : $("input[name = 'email']").val(),
	      		"password" : $("input[name = 'password']").val(),
	      		"managerFlag" : $(".ajaxPower option:selected").attr("data-value")
	      	},
	      	beforeSend: function(){
	      		if(document.URL.endsWith("login.html")){
	    			 globalLoading(true);
	    		}
	      	}
	      }).then( (data) => {
	    	  if(data.stats === "SUCCESS" && data["info"] === "USER"){
	      			window.location.href = "../index.html";
	      		}else if(data.stats === "ERROR" && data["info"] === "USER"){
	      			globalLoading(false);
	      			globalTips("用户不存在");
	      		}else if(data.stats === "SUCCESS" && data["info"] === "MANAGER"){
	      			window.location.href = "../../Admin/index.html";
	      		}else{
	      			globalLoading(false);
	      			globalTips("不具备管理员权限");
	      		}
	      }, () => {
	    	  
	      })
	    }
	});
});
