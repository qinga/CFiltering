package com.jju.cfiltering.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.Manager;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.LoginService;
import com.jju.cfiltering.util.MD5;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class LoginGeneralAction extends ActionSupport implements ModelDriven<User>{
	/**
	 * 用户登录
	 */
	private static final long serialVersionUID = 1L;
	private User user = new User();
	
	private Integer managerFlag;

	private LoginService loginService;
	
	public String login() throws IOException {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject loginResult = new JSONObject();
		//管理员登录
		if(managerFlag == 1) {
			Manager manager = new Manager();
			manager.setEmail(user.getEmail());
			manager.setManagerFlag(managerFlag);
			manager.setPassword(MD5.getMd5(user.getPassword()));
			Manager managerInfo = loginService.persistLogin(manager);
			
			//用户存在
			if(managerInfo != null) {
				//保存用户，作登录权限验证
				ActionContext.getContext().getSession().put("managerInfo", managerInfo);
				loginResult.put("stats", "SUCCESS");
				loginResult.put("info", "MANAGER");
				loginResult.put("usreName", managerInfo.getManagerName());
				try {
					response.getWriter().println(loginResult);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				loginResult.put("stats", "ERROR");
				loginResult.put("info", "MANAGER");
				try {
					response.getWriter().println(loginResult);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//普通用户登录
		}else if(managerFlag == 0){
				//普通用户登录
				//MD5加密用户密码
				user.setEmail(user.getEmail());
				user.setPassword(MD5.getMd5(user.getPassword()));
				//登录
				User userInfo = loginService.persistLogin(user);
				//用户存在
				if(userInfo != null) {
					//保存用户，作登录权限验证
					ActionContext.getContext().getSession().put("userInfo", userInfo);
					loginResult.put("stats", "SUCCESS");
					loginResult.put("info", "USER");
					loginResult.put("usreName", userInfo.getName());
					response.getWriter().println(loginResult);
				}else {
					loginResult.put("stats", "ERROR");
					loginResult.put("info", "USER");
					response.getWriter().println(loginResult);
				}
			}
		return null;
	}

	@Override
	public User getModel() {
		return user;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public Integer getManagerFlag() {
		return managerFlag;
	}

	public void setManagerFlag(Integer managerFlag) {
		this.managerFlag = managerFlag;
	}
	
	
	public String findUserByName() throws IOException {
		JSONObject user = new JSONObject();
		JSONObject data = new JSONObject();
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		User generalUser = (User) ActionContext.getContext().getSession().get("userInfo");
		JSONObject githubUser = (JSONObject) ActionContext.getContext().getSession().get("GitHubUserInfo");
		if(generalUser != null) {
			data.put("userName", generalUser.getName());
			data.put("userProfile", generalUser.getProfile_img());
			data.put("userId", generalUser.getId());
			user.put("data", data);
			user.put("code", 200);
			user.put("msg", "USEREXIST");
		}else if(githubUser != null) {
			data.put("userName", githubUser.getString("login"));
			data.put("userProfile", githubUser.getString("avatar_url"));
			data.put("userId", githubUser.getString("userId"));
			user.put("data", data);
			user.put("code", 200);
			user.put("msg", "USEREXIST");
		}else {
			user.put("code", 404);
			user.put("msg", "UNEXIST");
			user.put("data", data);
		}
		res.getWriter().print(user);
		return null;
	}
	
	
	public String logout() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject res = new JSONObject();
		try {
			//普通用户登录
			User generalUser = (User)ActionContext.getContext().getSession().get("userInfo");
			//如果是github用户登录
			if(generalUser == null) {
				//移除github用户
				ActionContext.getContext().getSession().remove("GitHubUserInfo");
			}else {
				//否则的话，移除普通用户。
				ActionContext.getContext().getSession().remove("userInfo");
			}
			res.put("code", 200);
			res.put("msg", "SUCCESS");
			response.getWriter().println(res);
		}catch(Exception e) {
			res.put("code", 404);
			res.put("msg", "ERROR");
			response.getWriter().println(res);
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
