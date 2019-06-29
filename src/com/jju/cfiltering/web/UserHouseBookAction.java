package com.jju.cfiltering.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.service.UserHouseBookService;
import com.jju.cfiltering.util.HouseBookUtil;
import com.opensymphony.xwork2.ActionSupport;

public class UserHouseBookAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private Integer bookID;
	private Integer dbID;
	//用户收藏
	public String houseBooks() throws IOException {
		JSONObject jo = new JSONObject();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		try {
			Boolean flag = userHouBook.saveUserHouBook(bookID, dbID);
			if(flag == true) {
				//收藏成功
				jo.put("code", 200);
				jo.put("msg", "SUCCESS");
				jo.put("data", new JSONArray());
			}else {
				jo.put("code", 200);
				jo.put("msg", "EXIST");
				jo.put("data", new JSONArray());
			}
		}catch(Exception e) {
			jo.put("code", 500);
			jo.put("msg", "ERROR");
			jo.put("data", new JSONArray());
			response.getWriter().println(jo);
			e.printStackTrace();
		}
		response.getWriter().println(jo);
		return null;
	}
	
	//查找用户的收藏
	private Integer start;
	private Integer size;
	private String type;
	public String findHouseBook() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject result = userHouBook.findHouseBook(dbID, start, size, type);
		if(result != null) {
			response.getWriter().println(result);
		}else {
			JSONObject js = new JSONObject();
			js.put("code", 404);
			js.put("msg", "EMPTY");
			js.put("data", new JSONArray());
			response.getWriter().println(js);
		}
		return null;
	}
	
	//后台移除用户的收藏
	public String removeHouseBook() throws IOException {
		JSONObject jo = new JSONObject();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		try{
			Boolean flag = userHouBook.removeHouseBook(dbID, bookID);
			if(flag == true) {
				//收藏成功
				jo.put("code", 200);
				jo.put("msg", "SUCCESS");
				jo.put("data", new JSONArray());
			}else {
				jo.put("code", 200);
				jo.put("msg", "UNEXIST");
				jo.put("count", 1);
				jo.put("data", new JSONArray());
			}
		}catch(Exception e) {
			jo.put("code", 500);
			jo.put("msg", "ERROR");
			jo.put("data", new JSONArray());
			jo.put("count", 1);
			e.printStackTrace();
			response.getWriter().println(jo);
		}
		response.getWriter().println(jo);
		return null;
	}
	
	
	//用户评分
	private Integer times;//特别用于标记用户是在停留时间超过30秒时，评分
	public String pinScore() throws IOException {
		System.out.println(" ++++++++++++++++++++++++++++++++++++");
		System.out.println( times + " time time time " + times);
		System.out.println( times + " time time time " + times);
		System.out.println( times + " time time time " + times);
		System.out.println(" ++++++++++++++++++++++++++++++++++++");
		JSONObject jo = new JSONObject();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		try{
			Boolean flag = userHouBook.updateScore(dbID, bookID, times);
			if(flag == true) {
				//评分成功
				jo.put("code", 200);
				jo.put("msg", "SUCCESS");
				jo.put("data", new JSONArray());
			}
		}catch(Exception e) {
			jo.put("code", 500);
			jo.put("msg", "ERROR");
			jo.put("data", new JSONArray());
			e.printStackTrace();
			response.getWriter().println(jo);
		}
		response.getWriter().println(jo);
		return null;
	}
	
	//用户星级评分
	//几个星
	private Integer starCount;
	private String userComment;
	public String starPinScore() throws IOException {
		JSONObject jo = new JSONObject();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		try{
			Boolean flag = userHouBook.updateStarPinScore(dbID, bookID, starCount, userComment);
			if(flag == true) {
				//评分成功
				jo.put("code", 200);
				jo.put("msg", "SUCCESS");
				jo.put("data", new JSONArray());
			}
		}catch(Exception e) {
			jo.put("code", 500);
			jo.put("msg", "ERROR");
			jo.put("data", new JSONArray());
			e.printStackTrace();
			response.getWriter().println(jo);
		}
		response.getWriter().println(jo);
		return null;
	}
	
	private UserHouseBookService userHouBook;

	public UserHouseBookService getUserHouBook() {
		return userHouBook;
	}

	public void setUserHouBook(UserHouseBookService userHouBook) {
		this.userHouBook = userHouBook;
	}
	public Integer getBookID() {
		return bookID;
	}
	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}
	public Integer getDbID() {
		return dbID;
	}
	public void setDbID(Integer dbID) {
		this.dbID = dbID;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStarCount() {
		return starCount;
	}

	public void setStarCount(Integer starCount) {
		this.starCount = starCount;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}

}
