package com.jju.cfiltering.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.Pagination;
import com.jju.cfiltering.service.GetBooksAllInfoService;
import com.jju.cfiltering.util.BooksUtil;
import com.opensymphony.xwork2.ActionSupport;

public class GetBooksAllInfoAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GetBooksAllInfoService gbs;
	
	private Integer typeId;
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	/*
	 * 获取所有该类型的书籍，不为条件查询
	 */
	public String getAllBooks() {
		List<Books> allBookInfo = null;
		//前台传书籍类型编号不为空时
		if(typeId != null) {
			allBookInfo =  gbs.getAllBooks(typeId);
			BooksUtil.returnResultData(allBookInfo);
		}
		return null;
	}
	//--------------------------------------------------------------------------
	/*
	 * 按条件查询所有符合书籍
	 */
	String getBookByConditionPagination = "";
	public String getBookByCondit() {
		Pagination pg = new Pagination();
		//作者名字
		if(authorName.trim().equals("")) {
			pg.setAuthorName(null);
		}else
			pg.setAuthorName(authorName.trim());
		//书籍标志
		switch (bookFlag) {
		case 1:
			pg.setNewBookFlag(1);
			break;
		case 2:
			pg.setSkillBookFlag(1);
			break;
		case 3:
			pg.setBeginBookFlag(1);
			break;
		case 4:
			pg.setOrginalBookFlag(1);
			break;
		default:
			break;
		}
		//书籍名字
		if(bookName.trim().equals("")) {
			pg.setBookName(null);
		}else
			pg.setBookName(bookName.trim());
		
		//出版社名字
		if(publishName.length() <= 3) {
			pg.setPublishName(null);
		}else
			pg.setPublishName(publishName);
		//分页
		pg.setPageNumber(pageNumber);
		pg.setPageSize(pageSize);
		pg.setTypeId(typeId); 
		List<Books> allBooksConditionInfo = gbs.getBooksByCondition(pg, getBookByConditionPagination);
			
		BooksUtil.returnResultData(allBooksConditionInfo);
		return null;
	}
		private String bookName;
		private String authorName;
		private String publishName;
		private Integer bookFlag;
		private String conditFlag;
		
		public String getConditFlag() {
			return conditFlag;
		}
		public void setConditFlag(String conditFlag) {
			this.conditFlag = conditFlag;
		}
		public String getBookName() {
			return bookName;
		}
		public void setBookName(String bookName) {
			this.bookName = bookName;
		}
		public String getAuthorName() {
			return authorName;
		}
		public void setAuthorName(String authorName) {
			this.authorName = authorName;
		}
		public String getPublishName() {
			return publishName;
		}
		public void setPublishName(String publishName) {
			this.publishName = publishName;
		}
		public Integer getBookFlag() {
			return bookFlag;
		}
		public void setBookFlag(Integer bookFlag) {
			this.bookFlag = bookFlag;
		}

	
	//-----------------------------------------------------------------------------------
	public String getOnePageBook() {
		//没有设置查询条件时,走这个方法
		if(pageNumber != null && pageSize != null && typeId != null && conditFlag == null) {
			List<Books>oneBooksInfo = gbs.getOnePageBookInfo(pageNumber, pageSize, typeId);
			BooksUtil.returnResultData(oneBooksInfo);
		}else {
			getBookByConditionPagination = "getBookByConditionPagination";
			getBookByCondit();
		}
		return null;
	}
		private Integer pageNumber;
		private Integer pageSize;
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
		
		private Integer bookDatabaseId;
		//获取某条数据
		public String getOneBook() {
			Books oneBookInfo = gbs.getOneBook(bookDatabaseId);
				BooksUtil.returnOneBookInfo(oneBookInfo);
			return null;
		}
		
		public Integer getBookDatabaseId() {
				return bookDatabaseId;
		}
		public void setBookDatabaseId(Integer bookDatabaseId) {
			this.bookDatabaseId = bookDatabaseId;
		}
	//--------------------------------------------------------------------------------------------
	public String removeOneBook() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject resultData  = new JSONObject();
		try {
			gbs.removeOneBook(bookId);
			resultData.put("stats", "SUCCESS");
			response.getWriter().println(resultData.toJSONString());
		}catch(Exception e) {
			try {
				resultData.put("stats", "FAIL");
				response.getWriter().println(resultData);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}
	private Integer bookId;
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public void setGbs(GetBooksAllInfoService gbs) {
		this.gbs = gbs;
	}

}
