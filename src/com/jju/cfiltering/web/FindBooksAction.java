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
import com.jju.cfiltering.service.FindBookService;
import com.jju.cfiltering.util.BooksUtil;
import com.opensymphony.xwork2.ActionSupport;

public class FindBooksAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static final Integer BOOK_FLAG_COUNT = 3;
	
	private FindBookService findBookService;
	public String findBook() {
		List<Books> booksCondition = new ArrayList<Books>();
		Map<String,String> listBookResult = new HashMap<String,String>();
		for(int i = 0; i<BOOK_FLAG_COUNT;i++) {
			switch (i) {
				case 0: 
					Books one = new Books();
					one.setBeginBookFlag(1);
					booksCondition.add(one);
						break;
				case 1:
					Books two = new Books();
					two.setNewBookFlag(1);
					booksCondition.add(two);
						break;
				case 2:
					Books three = new Books();
					three.setSkillBoostFlag(1);
					booksCondition.add(three);
						break;
			}
		}
		//调用service查询主页书籍列表
		Map<String,String> resultBooks = findBookService.findBook(booksCondition,listBookResult);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		
		if( resultBooks!=null ) {
			resultBooks.put("stats", "SUCCESS");
			JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(resultBooks));
			try {
				response.getWriter().print(JSONRESULT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			Map<String,String> resultEmptyBooks = new HashMap<String,String>();
			resultEmptyBooks.put("stats", "EMPTY");
			JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(resultBooks));
			try {
				response.getWriter().print(JSONRESULT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public void setFindBookService(FindBookService findBookService) {
		this.findBookService = findBookService;
	}
	
	private String dataId;
	public String getIndexPageBooks() {
		Map<String,String> resultBooks = new HashMap<String,String>();
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		resultBooks = findBookService.getPageBooks(dataId);
		JSONObject result = new JSONObject();
		if(resultBooks.size() > 0) {
			result.put("code", 200);
			result.put("msg", "REQUESTSUCCESS");
			result.put("data", resultBooks);
			try {
				res.getWriter().println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			result.put("code", 500);
			result.put("msg", "REQUESTFAILS");
			result.put("data", "FAIL");
			try {
				res.getWriter().println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	
	/*
	 * 搜索框获取书籍
	 */
	private String searchValue;
	private String direcId;
	private String typeId;
	public String findBooksBySearch() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		JSONObject result = new JSONObject();
		Map<String,String> searchBooks = new HashMap<String, String>();
		searchBooks = findBookService.findBooksBySearch(typeId, searchValue, direcId);
		if(searchBooks.size() > 0) {
			result.put("code", 200);
			result.put("msg", "SUCCESSREQUEST");
			result.put("data", searchBooks);
			res.getWriter().print(result);
		}else {
			result.put("code", 404);
			result.put("msg", "REQUESTFAIL");
			res.getWriter().println(result);
		}
		return null;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getDirecId() {
		return direcId;
	}
	public void setDirecId(String direcId) {
		this.direcId = direcId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
	//bookInfo.html中获取书籍，根据ID.
	private Integer bookID;
	public String findBookByID() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		List<Object[]> data = findBookService.findBookByID(bookID);
		JSONObject resData = BooksUtil.parseBookInfo(data);
		res.getWriter().print(resData);
		return null;
	}
	public Integer getBookID() {
		return bookID;
	}
	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}
	
	
	//获取书籍的评论
	public String findPinLunByID() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json;charset=utf-8");
		List<Object[]> data = findBookService.findPinLunByID(bookID);
		JSONObject resData = BooksUtil.parsePinLunStar(data);
		res.getWriter().print(resData);
		return null;
	}
	
}
