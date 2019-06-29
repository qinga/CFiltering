/*
 * 轮播图
 */
import opts from "./config.js";
import "./tabCar.js";
/*渐变轮播图*/
$(function () {
	/*$(".path-banner").find("a").each(function(k,v){
		$(v).on("mouseover",function(event){
			 event.stopPropagation();
		})
	});*/
	$('.g-banner-content').each(function () {
		//div元素
		var slidewrap = $(this).find('.g-banner-box');
		//div.banner-slide元素
		var slide = slidewrap.find('a div');
		//div.banner-slide元素长度
		var count = slide.length;
		var that = this;
		var index = 0;
		var time = null;

		$(this).data('opts', opts);
		// next
		$(this).find('.next').on('click', function () {
			if (opts['isAnimate'] == true) {
				return;
			}
			var old = index;
			if (index >= count - 1) {
				index = 0;
			} else {
				index++;
			}
			change.call(that, index, old);
		});
		// prev
		$(this).find('.prev').on('click', function () {
			if (opts['isAnimate'] == true) {
				return;
			}

			var old = index;
			if (index <= 0) {
				index = count - 1;
			} else {
				index--;
			}
			change.call(that, index, old);
		});
		$(this).find('.banner-dots span').each(function (cindex) {
			$(this).on('click', function () {
				change.call(that, cindex, index);
				index = cindex;
			});
		});
		let itemElements = $(".menuContent").find(".item")
		let submenuElements = $(".g-banner , .pr").find(".submenu")
		for (let i = 0; i < itemElements.length; i++) {
			$(itemElements[i]).on("mouseover", function () {
				clearInterval(time);
				$(this).find('.banner-anchor').css({
					opacity: 0.6
				});
			})
			$(submenuElements[i]).on("mouseover", function () {
				clearInterval(time);
				$(this).find('.banner-anchor').css({
					opacity: 0.6
				});
			})

			$(itemElements[i]).on("mouseleave", function () {
				clearInterval(time);
				startAtuoPlay();
				$(this).find('.banner-anchor').css({
					opacity: 0.15
				});
			})

			$(submenuElements[i]).on("mouseleave", function () {
				clearInterval(time);
				startAtuoPlay();
				$(this).find('.banner-anchor').css({
					opacity: 0.15
				});
			})
		}
		// focus clean auto play
		$(this).on('mouseover', function () {
			if (opts.autoPlay) {
				clearInterval(time);
			}
			$(this).find('.banner-anchor').css({
				opacity: 0.6
			});
		});
		//  leave
		$(this).on('mouseleave', function () {
			if (opts.autoPlay) {
				clearInterval(time);
				startAtuoPlay();
			}
			$(this).find('.banner-anchor').css({
				opacity: 0.15
			});
		});
		startAtuoPlay();
		// auto play
		function startAtuoPlay() {
			if (opts.autoPlay) {
				time = setInterval(function () {
					var old = index;
					if (index >= count - 1) {
						index = 0;
					} else {
						index++;
					}
					change.call(that, index, old);
				}, 3000);
			}
		}
	})

	function change(show, hide) {
		var opts = $(this).data('opts');

		if (opts.autoPlay == true) {

			$(this).find('.g-banner-box div').eq(hide).stop().animate({
				opacity: 0
			}, 2000);
			
			//设置背景部分随背景图片而改变
			$(".bk").css("background-image","url("+$(this).find('.g-banner-box div').eq(show).find('img').attr('src')+")");
			
			$(this).find('.g-banner-box div').eq(show).show().css({
				opacity: 0
			}).stop().animate({
				opacity: 1
			}, 1000);

			$(this).find('.banner-dots span').removeClass('active');
			$(this).find('.banner-dots span').eq(show).addClass('active');
		}
	}
})
