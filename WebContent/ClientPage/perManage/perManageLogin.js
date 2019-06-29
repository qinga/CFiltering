/**
 * 
 */
			$.ajax({
				url: "LoginGeneralAction_findUserByName",
				type: "post",
				dataType : "json"
			}).then( (res) => {
				if(res.code === 200 && res.msg === "USEREXIST"){
					var $imgEle = $("ul li#header-user-card").find("img");
					//设置图片
					$.each($imgEle, (k,v) => {
						$(v).attr("src",res.data.userProfile);
						$(v).attr("alt",res.data.userProfile);
					})
					//大图片，大名字
					$(".user-info .user-pic-bg").find("img").attr("src",res.data.userProfile);
					$(".user-info .user-info-right").find("span").text(res.data.userName);
					//设置用户名字
					$(".g-user-card").find("span.text-ellipsis").text(res.data.userName);
					$("#login-area").css("display","block");
				}else{
					errorTip("您还未登录 ");
				}
		    }, () => {
		    	errorTip("网络请求异常 请稍后重试");
		    }).then( () => {
				$("#login-area").on("mouseover", function(){
					$(this).find(".g-user-card").css("visibility", "visible");
				}).on("mouseleave", function(){
					$(this).find(".g-user-card").css("visibility", "hidden");
				})
			}, () => {
				errorTip("请求失败 请稍后重试");
			})