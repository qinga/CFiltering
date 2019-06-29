// 登录模块
import "./checkCode.js";
import "./valiform.js";
window.onload = function(){
	
  //是否选中
	var iconChecked = document.getElementsByTagName("input")[2];
	
	var loginBtn = document.getElementById("submitLoginForm");

  // 初始化用户名和密码选项
  var rememberUsername = localStorage.getItem('username');
  var rememberPassword = localStorage.getItem('password');
  
  if (rememberUsername && rememberPassword) {
    document.getElementsByTagName('input')[0].value = rememberUsername;
    document.getElementsByTagName('input')[1].value = rememberPassword;
    localStorage.setItem('rememberUserInfo', true);
    //自动勾选记住密码
    iconChecked.checked = true;
  } else {
    // 设置默认的记住密码状态为false
    iconChecked.checked = false;
    localStorage.setItem('rememberUserInfo', false);
  }


  // 记住密码的点击
  loginBtn.onclick = function() {
    // 用一个变量记录记住密码的状态
    var rememberStatus = true;
    
    //如果未选中记住密码,则移除已保存的用户名和密码等信息
    if (iconChecked.checked === false) {
    	 localStorage.removeItem('username');
      localStorage.removeItem('password');
      localStorage.removeItem('userInfo');
      rememberStatus = false;
    }else{
    	 var userName =document.getElementsByTagName('input')[0].value; // 用户名
   		 var passWord =  document.getElementsByTagName('input')[1].value; // 密码
   		  localStorage.setItem('username',userName);
      	localStorage.setItem('password',passWord);
      	//其它信息
      	setUserInfo();
    }
    localStorage.setItem('rememberUserInfo', rememberStatus);

  };
//  // 得到当前时间信息
//  function getDate () {
//    var date = new Date();
//    var year = date.getFullYear();
//    var month = date.getMonth(); // 月份为0-11
//    var day = date.getDate();
//    var hour = date.getHours();
//    var minute = date.getMinutes();
//    var second = date.getSeconds();
//
//    // 返回当前的时间字符串
//    return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
//  }
//
//  // 存下用户注册的信息
//  function setUserInfo (userName, passWord) {
//    // 存下用户信息
//    var data = {
//      username: userName, // 用户名
//      password: passWord, // 密码
//      sex: 'M', // 性别
//      signature: '这个人很懒，什么都没有留下', // 签名
//      createTime: getDate()// 注册时间
//    };
//
//    // 得到本地存储
//    var userInfo = localStorage.getItem('userInfo');
//    // 如果本地存储存在，则继续向里面添加用户数据，否则创建本地存储
//    if (userInfo) {
//      userInfo = JSON.parse(userInfo);
//      userInfo.push(data);
//      userInfo = JSON.stringify(userInfo);
//    } else {
//      userInfo = [];
//      userInfo.push(data);
//      userInfo = JSON.stringify(userInfo);
//    }
//    // 将用户的信息存储到本地
//    localStorage.setItem('userInfo',userInfo);
//  }
};




