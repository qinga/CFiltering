package com.jju.cfiltering.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.dao.GetBooksTypeInfoDao;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.service.GetBooksTypeInfoService;
import com.jju.cfiltering.util.EnCode;

public class GetBooksTypeInfoServiceImpl implements GetBooksTypeInfoService {

	private GetBooksTypeInfoDao getBookInfo;
	@Override
	public List<String> getBooksTypeInfo(Integer dataKey) {
		return getBookInfo.getBooksTypeInfo(dataKey);
	}
	
	
	public void setGetBookInfo(GetBooksTypeInfoDao getBookInfo) {
		this.getBookInfo = getBookInfo;
	}

	@Override
	public Integer getBooksCount() {
		return getBookInfo.getBooksCount();
	}
	@Override
	public JSONObject getBooksAndType(Integer pageStart, Integer pageSize, String type) {
		return getBookInfo.getBooksAndType(pageStart, pageSize, type);
	}

	@Override
	public JSONObject getBooksByDir(Integer direct, Integer suffix, String degree, Integer start, Integer size, String specailX) {
		if( degree != null) {
			switch (degree) {
			case "begin":
				degree = "begin_book_flag";
				break;
			case "new":
				degree = "new_book_flag";
				break;
			case "skill":
				degree = "skill_boost_flag";
				break;
			default:
				break;
			}
		}
		return getBookInfo.getBooksByDir(direct, suffix,degree, start, size,specailX);
	}


	@Override
	public JSONObject getAllBookById(String searchData, String btyName, String searchValue, Integer searchStart,
			Integer searchSize) {
		//第一次调用时searchData 为 200
		//第二次时为 类型
		boolean flag = EnCode.isInteger(searchData);
		if(flag == true && searchStart == null) {
			//前端主页查询跳转至booksType.html页面时，按条件查询书籍数量
			return getBookInfo.getSearchBookCount(btyName, searchValue);
		}else {
			//根据书籍类型查找所有。dir book 等
			return getBookInfo.getAllBookByTypeName(searchData, searchValue, searchStart, searchSize);
		}
	}
}
