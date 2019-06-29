package com.jju.cfiltering.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.UserOperatorService;
import com.opensymphony.xwork2.ActionSupport;

public class UserOperatorAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	private UserOperatorService userOperator;
	
	public String getAllUser() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		List<User> allUserInfo = userOperator.getAllUserInfo();
		JSONArray userAllInfo = new JSONArray();
		/*
		 * [{},{},{},{}]
		 */
		if(allUserInfo.size() > 0) {
			for(User u : allUserInfo) {
				JSONObject oneUserData = new JSONObject();
				oneUserData.put("userID", u.getId());
				oneUserData.put("userName", u.getName());
				oneUserData.put("userPassword", u.getPassword() );
				oneUserData.put("userPhone", u.getPhone());
				oneUserData.put("userSex", u.getGender());
				oneUserData.put("userEmail", u.getEmail());
				oneUserData.put("userRegisterTime", u.getRegister_time());
				oneUserData.put("userProfileImg", u.getProfile_img());
				userAllInfo.add(oneUserData);
			}
			try {
				response.getWriter().println(userAllInfo);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			userAllInfo.add(new HashMap<String,String>().put("stats", "EMPTY"));
			try {
				response.getWriter().println(userAllInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//获取一个页面的用户信息
	
	private Integer pageNumber;
	private Integer pageSize;
	public String getOnePageUser() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		
		DetachedCriteria dt = DetachedCriteria.forClass(User.class);
		List<User> onePageUser = userOperator.getOnePageUser(dt, pageNumber, pageSize);
		
		JSONArray userOnePageInfo = new JSONArray();
		if(onePageUser.size() > 0) {
			for(User u : onePageUser) {
				JSONObject oneUserData = new JSONObject();
				oneUserData.put("userID", u.getId());
				oneUserData.put("userName", u.getName());
				oneUserData.put("userPassword", u.getPassword());
				oneUserData.put("userPhone", u.getPhone());
				oneUserData.put("userSex", u.getGender());
				oneUserData.put("userEmail", u.getEmail());
				oneUserData.put("userRegisterTime", u.getRegister_time());
				oneUserData.put("userProfileImg", u.getProfile_img());
				userOnePageInfo.add(oneUserData);
			}
			try {
				response.getWriter().print(userOnePageInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			userOnePageInfo.add(new HashMap<String,String>().put("stats", "EMPTY"));
			try {
				response.getWriter().println(userOnePageInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public void setUserOperator(UserOperatorService userOperator) {
		this.userOperator = userOperator;
	}
	
	

}
