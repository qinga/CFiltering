package com.jju.cfiltering.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.service.RecommenderService;
import com.opensymphony.xwork2.ActionSupport;

public class RecommenderAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	//基于项目的过滤推荐
	private RecommenderService recommder;
	public String findItemRecommer() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject result = recommder.findItem(userID);
		response.getWriter().println(result);
		return null;
	}
	public RecommenderService getRecommder() {
		return recommder;
	}
	public void setRecommder(RecommenderService recommder) {
		this.recommder = recommder;
	}
	
	//基于用户的协同推荐
	public String findUserRecommer() throws IOException {
		//基于用户的协同过滤
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		//传入用户ID
		JSONObject result = recommder.findUserItem(userID);
		response.getWriter().println(result);
		return null;
	}
	
	private Long userID;
	//top-N推荐（用户没有登录时）
	public String findTopNRecommer() throws IOException {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		//用户没有登录，则查询数据库中评分较高的数据
		JSONObject result = recommder.findTopNRecommer();
		//
		response.getWriter().println(result);
		return null;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	
}
