/**
 * 
 */
/*--------1-----用户登录，则显示用户登录的信息，否则，显示登录注册的功能模块--------*/
			var str = `<li class="liSecond" style="float:right">
		                    <a href="../login/login.html" id="js-signin-btn">登录</a> |
		                    <a href="../register/register.html" id="js-signup-btn">注册</a>
		               </li>`;
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
						//设置用户名字
						$(".g-user-card").find("span.text-ellipsis").text(res.data.userName);
						$("#login-area").css("display","block");
					}else{
						//如果用户不存在，则显示登录
						$("ul.nav-item").find("li.moreBook").after(str);
						$("ul.logined").css("display","none");
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