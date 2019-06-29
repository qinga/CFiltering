package com.jju.cfiltering.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.service.LearnPathService;
import com.jju.cfiltering.util.LePathUtil;
import com.opensymphony.xwork2.ActionSupport;

public class LearnPathAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//首页找学习路线
	//轮播图
	private LearnPathService learnPa;
	private String topDirect;
	public String findType() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		List<Object[]> learnPath = learnPa.findLearnPath(topDirect);
		JSONObject result = LePathUtil.parseLearn(learnPath);
		res.getWriter().println(result);
		return null;
	}
	
	public LearnPathService getLearnPa() {
		return learnPa;
	}
	public void setLearnPa(LearnPathService learnPa) {
		this.learnPa = learnPa;
	}
	public String getTopDirect() {
		return topDirect;
	}
	public void setTopDirect(String topDirect) {
		this.topDirect = topDirect;
	}


	private Integer directID;
	public String findDirect() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		List<Object[]> dataResult = learnPa.findDirect(directID);
		JSONObject result = LePathUtil.parseDirect(dataResult);
		res.getWriter().println(result);
		return null;
	}
	public Integer getDirectID() {
		return directID;
	}
	public void setDirectID(Integer directID) {
		this.directID = directID;
	}
	
}
