package com.jju.cfiltering.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.UserHouseBookService;
import com.jju.cfiltering.service.UserRegisterService;
import com.jju.cfiltering.util.CFileUtil;
import com.jju.cfiltering.util.MD5;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserRegisterAction extends ActionSupport implements ModelDriven<User>{

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_HEAD_IMAGES = "/CFilteringUpload/headImages/default_head_images.jpg";
	
	//依赖service
	private UserRegisterService userService;
	private UserHouseBookService userFirstAutoPin;
	private User user = new User();
	
	public String saveUser() {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=utf-8");
			Map<String,String> resultMap = new HashMap<String,String>();
		//验证是否存在该name
		if(user.getName() != null && user.getEmail()==null &&user.getPassword()==null && user.getPhone()==null) {
			user.setName(user.getName());
			User uName = userService.findUser(user);
			//用户名不存在
		    if(uName == null) {
		    	resultMap.put("stats", "SUCCESS");
				resultMap.put("info", "ISEXIST");
				JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(resultMap));
				try {
					response.getWriter().println(JSONRESULT.toJSONString());
				} catch (IOException e) {
				}
			}else {
				//用户名存在
				resultMap.put("stats", "SUCCESS");
				resultMap.put("info", "EXIST");
				JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(resultMap));
				try {
					response.getWriter().println(JSONRESULT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(user != null && user.getEmail()!=null && user.getPassword()!=null && user.getPhone()!=null) {
			user.setPassword(MD5.getMd5(user.getPassword()));
			user.setRegister_time(CFileUtil.formateDate(new Date()));
			user.setProfile_img(DEFAULT_HEAD_IMAGES);
			try {
				//用户注册
				userService.saveUser(user);
				//用户注册后，找出该注册用户
				User u = userService.findUser(user);
				System.out.println(u.toString() + " u.toString +++sf dsafjasdfo " + u.getId());
				//给所有的书籍默认评分
				userFirstAutoPin.insertAutoPin(u.getId());
				System.out.println( " pu tong zu ce gei xing yong hu ping fen");
				resultMap.put("stats", "OK");
				resultMap.put("info", "SUCCESS");
				JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(resultMap));
				response.getWriter().println(JSONRESULT);
			}catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("注册失败啦");
			}
			 
		}else {
			System.out.println("user is null" + user);
		}
		return null;
    }
	
	//保存github用户
	public String saveGithubUser() {
		User user = (User) ActionContext.getContext().get("user");
		//判断用户是否原先已经保存到数据库。
		User uExist = userService.findUser(user);
		//用户未通过github授权登录，需添加至数据库
		if(uExist == null) {
			userService.saveUser(user);
		}
		//用户不管是否保存，查出用户ID,存放至session中，方便后续操作。
		User u = userService.findUser(user);
		JSONObject git = (JSONObject) ActionContext.getContext().getSession().get("GitHubUserInfo");
		//存入用户ID
		git.put("userId", u.getId());
		ActionContext.getContext().getSession().put("GitHubUserInfo", git);
		
		//用户首次通过github登录时，给所有书籍默认评分
		if(uExist == null) {
			System.out.println( " 给 新 注册 的 github 评 分  ！！！！！");
			userFirstAutoPin.insertAutoPin(u.getId());
		}
		
		System.out.println(ActionContext.getContext().getSession().get("GitHubUserInfo") + "gitHubUserInfo");
		return "SUCCESS";
	}

	
	public void setUserService(UserRegisterService userService) {
		this.userService = userService;
	}
	@Override
	public User getModel() {
		return user;
	}
	public UserHouseBookService getUserFirstAutoPin() {
		return userFirstAutoPin;
	}
	public void setUserFirstAutoPin(UserHouseBookService userFirstAutoPin) {
		this.userFirstAutoPin = userFirstAutoPin;
	}

}
