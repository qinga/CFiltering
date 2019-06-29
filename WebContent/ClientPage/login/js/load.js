/**
 * 
 */
// 加载动画 接收一个布尔值，用来判断创建动画还是删除动画
export function globalLoading (Boole) {
  if (Boole) {
    // 创建一个元素
    var dom = document.createElement('div');
    dom.className = 'global-loading';
    dom.innerHTML = '<div></div>';
    // 创建一个遮罩层
    var mask = document.createElement('div');
    mask.className = 'global-mask';
    // 插入到页面中
    document.body.appendChild(mask);
    document.body.appendChild(dom);
  } else {
    // 从页面中删除
    var mask = document.getElementsByClassName('global-mask')[0];
    var dom = document.getElementsByClassName('global-loading')[0];
    document.body.removeChild(mask);
    document.body.removeChild(dom);
  }
}

export function globalTips(text) {
	  // 创建一个元素
	  var dom = document.createElement("span");
	  dom.className = 'global-tips';
	  dom.innerHTML = text;
	  // 创建一个遮罩层
	  var mask = document.createElement('div');
	  mask.className = 'global-mask';
	  // 插入到页面中
	  document.body.appendChild(dom);
	  document.body.appendChild(mask);

	  // 计时器（两秒后清除dom）
	  setTimeout(function() {
	    document.body.removeChild(dom);
	    document.body.removeChild(mask);
	  }, 2000);
}

