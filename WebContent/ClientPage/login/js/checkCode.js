/**
 * 
 */
//导入动画加载函数
import { globalLoading } from "./load.js";
$(function(){
	//页面重新加载时，提交表单可不用
	$("#submitLoginForm").attr("disabled",true);
	$(window).on('scroll',function(){
		if($('html').scrollTop() !== 0 )
			$("body").addClass(" body-scrolled")
		else
			$("body").removeClass(" body-scrolled")
	});
	

	$("div.geetest-captcha").bind("click",function(){
		//点击开启验证码按钮时，验证码面板出现。
		$(".checkpanel").fadeIn(500);

	});
	
	$("section .faClose").on("click",function(e){

		//验证码面板关闭,当点击关闭面板按钮时
		$(".checkpanel").fadeOut(500);
		//防止事件冒泡
		e.stopPropagation();
	});
	/*注册中的圆的渐大渐小效果*/
	
	$(".geetest_radar_tip").hover(function(){
		$(".geetest_holder:eq(1)").removeClass("geetest_detect").addClass(" geetest_wait_compute");
	},function(){
		$(".geetest_holder:eq(1)").removeClass("geetest_wait_compute").addClass("geetest_detect");
	});
	
	$(".btn-dark").on("click",function(){
		 if(document.URL.endsWith("login.html")){
			 globalLoading(true);
		}
	})
})
$(".geetest_check").slideVerify({
	type : 2,		//类型
	vOffset : 5,	//误差量，根据需求自行调整
    vSpace : 5,	//间隔
    mode: 'pop',
    imgName : ['1.jpg', '2.jpg'],

     imgSize : {
    	width: '275px',
    	height: '200px',
    },
    blockSize : {
    	width: '40px',
    	height: '40px',
    },
    barSize : {
    	width : '275px',
    	height : '40px',
    },
    ready : function() {
	},
    success : function() {
    
    	//验证成功后，验证面板消失
    	$(".checkpanel").css("display","none");
    	//验证成功的按钮出现
    	$("#geetest-captcha").css("display","block");
    	//开启验证码的按钮实效
    	$(".geetest-captcha").css("display","none");
    	//提交表单按钮可用
    	$("#submitLoginForm").attr("disabled",false);
    },
    error : function() {
    }
    
});
		