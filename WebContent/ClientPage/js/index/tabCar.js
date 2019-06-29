/*选项卡*/
import opts from "./config.js";
$(function () {
	let itemElements = $(".menuContent").find(".item")
	let submenuElements = $(".g-banner , .pr").find(".submenu")
	for (let i = 0; i < itemElements.length; i++) {
		$(itemElements[i]).on("mouseover", function () {
			$(submenuElements[i]).removeClass("hide")
			opts.autoPlay = false
		})
		$(submenuElements[i]).on("mouseover", function () {
			$(submenuElements[i]).removeClass("hide")
			opts.autoPlay = false
		})

		$(itemElements[i]).on("mouseleave", function () {
			$(submenuElements[i]).addClass("hide")
			opts.autoPlay = true
		})

		$(submenuElements[i]).on("mouseleave", function () {
			$(submenuElements[i]).addClass("hide")
			opts.autoPlay = true
		})
	}
})
