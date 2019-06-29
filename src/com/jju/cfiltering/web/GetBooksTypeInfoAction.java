package com.jju.cfiltering.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.service.GetBooksTypeInfoService;
import com.jju.cfiltering.util.ParseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Administrator
 * 获取该专业方向下的所有书籍类型
 * 用于前台添加书籍处的，二级联动。
 */
public class GetBooksTypeInfoAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private Integer dataKey;
	
	private GetBooksTypeInfoService getBookInfo;
	public String getBooksTypeInfo() {
		List<String> booksInfo = null;
		HttpServletResponse rs = ServletActionContext.getResponse();
		if(dataKey != null) 
			 booksInfo = getBookInfo.getBooksTypeInfo(dataKey);
		if(booksInfo != null) {
			String JSONRESULT = JSON.toJSONString(booksInfo);
			try {
				rs.setContentType("application/json;charset=utf-8");
				rs.getWriter().println(JSONRESULT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				Map<String,String> result = new HashMap<String,String>();
				result.put("stats", "ERROR");
				result.put("info", "500");
				JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(result));
				rs.getWriter().println(JSONRESULT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public Integer getDataKey() {
		return dataKey;
	}
	public void setDataKey(Integer dataKey) {
		this.dataKey = dataKey;
	}
	public void setGetBookInfo(GetBooksTypeInfoService getBookInfo) {
		this.getBookInfo = getBookInfo;
	}

	//主页书籍检索时，跳转至书籍分类页面，或直接加载分类页面时。
	//params : typeName || bookId;
	//1 根据typeName 查出属于该typeName的方向，和相关书籍
	//2 根据bookdID 查出该书籍的书籍方向和类别，同时查出该书籍
	public String getBookOrBookDir() {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		if(searchData != null) {
			JSONObject result = new JSONObject();
			//表示查询书籍，
			result = getBookInfo.getAllBookById(searchData, btyName, searchValue, searchStart, searchSize);
			try {
				res.getWriter().println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//searchData表示书籍类型的名字或者书籍的id
	private String searchData;
	private String searchValue;
	//查询总数时,btyName用于表示哪个分类
	//分页查询时，不使用该变量
	private String btyName;
	private Integer searchStart;
	private Integer searchSize;
	public String getSearchData() {
		return searchData;
	}
	public String getBtyName() {
		return btyName;
	}
	public void setBtyName(String btyName) {
		this.btyName = btyName;
	}
	public Integer getSearchStart() {
		return searchStart;
	}
	public void setSearchStart(Integer searchStart) {
		this.searchStart = searchStart;
	}
	public Integer getSearchSize() {
		return searchSize;
	}
	public void setSearchSize(Integer searchSize) {
		this.searchSize = searchSize;
	}
	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	/*
	 * booksType.html页面的操作。。（主要是加载该页面时的初始操作 和 从主页跳转过来的操作）
	 */
	public String getBooksAndType() {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		JSONObject bookSize = new JSONObject();
		if(dataCount != null) {
			Integer bookCount = getBookInfo.getBooksCount();
			JSONObject book = new JSONObject();
			book.put("bookCount", bookCount);
			bookSize.put("data", book);
			bookSize.put("code", 200);
			bookSize.put("msg", "AMOUNT SUCCESS");
			try {
				res.getWriter().println(bookSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			JSONObject reData = getBookInfo.getBooksAndType(pageStart, pageSize, type);
			try {
				res.getWriter().println(reData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Integer pageStart;
	private Integer pageSize;
	private String type;
	private Integer dataCount;
	public Integer getDataCount() {
		return dataCount;
	}
	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}
	public Integer getPageStart() {
		return pageStart;
	}
	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	/**
	 * booksType.html页面的操作，主要是，点击三级联动查询书籍。
	 * @return JSONObject
	 */
	public String getDirTypeBooks() {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
			JSONObject book = getBookInfo.getBooksByDir(direct, suffix, degree, start, size, specailX);
			try {
				res.getWriter().println(book);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}
	//方向
	private Integer direct;
	//类别
	private Integer suffix;
	//难度系数
	private String degree;
	private Integer start;
	private Integer size;
	private String specailX;
	public String getSpecailX() {
		return specailX;
	}
	public void setSpecailX(String specailX) {
		this.specailX = specailX;
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
	public Integer getDirect() {
		return direct;
	}
	public void setDirect(Integer direct) {
		this.direct = direct;
	}
	public Integer getSuffix() {
		return suffix;
	}
	public void setSuffix(Integer suffix) {
		this.suffix = suffix;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	
}
