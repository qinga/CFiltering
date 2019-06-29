import "./sowingMap.js";

/*返回顶部*/
$(function(){
	//页面重新加载后，清空input框
	$("input").val("");
	$(window).on("scroll",function(){
		 var ItemHeight = $(".bg000:eq(0)").offset().top;
		 var scrollHeight = document.documentElement.scrollTop;
		 if(scrollHeight >= ItemHeight-$(".bg000:eq(0)")[0].offsetHeight/2){
		 	$("#J_GotoTop").fadeIn();
		 }else
		 	$("#J_GotoTop").fadeOut();
	});
	$("#J_GotoTop").bind("click",function(){
		$("html,body").animate({scrollTop:0},500);
	});
});
  
//首页搜索边框处理
$(function(){
	$("input.form-control").on({
		"focus" :focusHandler,
		"blur" :blurHandler
	});
	//选择框聚焦处理
	function focusHandler(){
		//input框不需要边框
		$(this).addClass(" noneBorder");
		//整个搜索框
		$(this).parent().addClass(" lightSunshine");
		//（0）全部分类框 ，（1）搜索按钮框
		$(this).siblings().eq(0).addClass(" noneBorder")
			.end().eq(1).addClass(" noneBorder");
	}
	//选择框失焦处理
	function blurHandler(){
		$(this).removeClass(" noneBorder");
		$(this).parent().removeClass(" lightSunshine");
		$(this).siblings().eq(0).removeClass(" noneBorder")
			.end().eq(1).removeClass(" noneBorder");
	}
	
	//点击全部分类按钮时，也作聚焦处理
	$(".input-group-addon:eq(0)").bind("click",focusHandler);

	
	//点击使ul中li显示或隐藏。
	let $dropdown = $("ul.dropdown-menu");
	let $backmask = $("#backmask");
	
	$backmask.bind("click",()=>{
		$dropdown.fadeOut();
		$backmask.css("display","none");
	});
	
	$(".input-group").children().filter(":eq(0)").bind("click",liHandler);
	function liHandler(){
		if($dropdown.css("display") === "block"){
			$dropdown.css("display","none");
			
		}else{
			$dropdown.css("display","block");
			$backmask.css("display","block");
		}
	};
	
	
	//选择li中的value，使其置于选项中。
	let $liElements = $("ul.dropdown-menu").find("li").not($(".divider"));
	$.each($liElements,function(k,v){
		$(v).bind("mouseup",function(event){
			let selectValue = document.getElementsByClassName("input-group-addon")[0].firstChild;
			//删除文本节点
			selectValue.parentNode.removeChild(selectValue);
			//添加选择的li值为新的文本节点
			$(".input-group-addon")[0].prepend( $.trim($(this).text()));
			$dropdown.fadeOut(500);
			//选择值完毕后，input框重新获取焦点
			$("input.form-control").focus();
			setTimeout(() => {
				$backmask.css("display","none");
			}, 500);
		})
	})
});
