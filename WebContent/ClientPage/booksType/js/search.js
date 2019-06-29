function sortData(data) {
	let arr = [];
	let reData = {};
	$.each(data, (k, v) => {
		if( k !== "bookCount"){
			var t = k.replace(/[^0-9]/ig,"");
			if( arr.indexOf(t) < 0){
				arr.push(t);
			}
		}
	})
	arr.sort( (a, b) => {
		return a-b;
	})
	for(let i = 0; i < arr.length; i++){
		$.each(data, (k, v) => {
			let pushk = k.replace(/[^0-9]/ig,"");
			if( pushk === arr[i]){
				//如果相等，则匹配
				k = k.replace(pushk,i);
				reData[k] = v;
			}
		})
	}
	reData.bookCount = arr.length;
	data = null;
	return reData;
}
/*-----------------重复数据删除----------------*/
function fRepeat(data){
	var arr = [];
	var del = [];
	$.each(data, function(k, v){
		if(k.startsWith("bookId")){
			if( arr.indexOf(v) < 0){
				arr.push(v);
			}else{
				//记录k后面的数字，表示重复的，要删除。
				var index = k.replace(/[^0-9]/ig,"");
				del.push(index);	
			}
		}
	})
	for(var i = 0; i < del.length; i++){
		$.each(data, function(k, v){
				if(k.endsWith(del[i])){
					delete data[k]
				}
		})
	}
	data["bookCount"] = data["bookCount"] - del.length;
	return data;
}
$(".course-top-search .search-input").on("input",function(){
					var $searchValue = $(this).val().trim(); 
					 if( $(this).val().trim() !== ""){
						 $.ajax({
							url: 'FindBooksAction_findBooksBySearch',
							type: 'post',
							dataType : 'json',
							data: {
								'direcId': 0,
								'searchValue': $searchValue
							}
						}).then( (data) => {
								if(data.code === 200){
									//先规则化处理数据
									data.data = sortData(data.data);
									//删除重复数据
									data.data = fRepeat(data.data);
									//规则化处理数据
									data.data = sortData(data.data);
									//清空
									$(".search-area-result").html("");
									
									  //将提示框渲染进dom树
									var str = ``;
								str += `<div id="nav-searchAjax" class="nav-flyout" style=" width: 484px;">
											<div id="suggestions-template">
												<div id="suggestions">`
													for(var i = 0; i<data.data.bookCount; i++){
									str           +=`<div class="s-suggestion">
														<span class="s-heavy" data-id=${data.data["bookId"+i]}>${data.data["bookName"+i]}</span>
														<span class="s-heavy"></span>
													</div>`;
												}
									str     += `</div>
										  </div>
									  </div>`;
										$(".search-area-result").append( str );			
								}else if(data.code === 404){
										$(".search-area-result").html("");
								}
						}, () => {
								errorTip("请求数据失败 请稍后重试");
						}).then( () => {
								var $suggDiv = $("#suggestions").find(".s-suggestion");
								$suggDiv.each( (k, v) => {
									$(v).hover( () => {
										//注意this指向问题，在箭头函数中
										//$(this)指向根元素HTMLDocument
										$(v).addClass("activeDivSu");
									}, () => {
										$(v).removeClass("activeDivSu");
									})
								})
								//如果点击提示面板的书籍，则跳转至所有书籍分类的界面
							 	var $allSuggest = $("#nav-searchAjax").find(".s-suggestion");
								//为每个suggestion绑定点击事件，当点击时，跳转在所有分类页面
								$.each($allSuggest, function(k, v) {
									$(v).on("click", function() {
										var bookId = $(this).find("span:eq(0)").attr("data-id");
										document.location.href = "../booksInfor/booksInfo.html?bookId="+bookId;
									})
								})
						}, () => {
							errorTip("请求数据渲染失败 请稍后重试");
						}) 
					 }else{
						 $(".search-area-result").html("");
					 }
				});
