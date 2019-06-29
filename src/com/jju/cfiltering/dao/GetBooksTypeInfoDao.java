package com.jju.cfiltering.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;


public interface GetBooksTypeInfoDao {

	public List<String> getBooksTypeInfo(Integer dataKey);

	public JSONObject getSearchBookCount(String btyName, String searchValue);

	public JSONObject getAllBookByTypeName(String searchData, String searchValue, Integer searchStart, Integer searchSize);

	public Integer getBooksCount();

	public JSONObject getBooksAndType(Integer pageStart, Integer pageSize,String type);


	public JSONObject getBooksByDir(Integer direct, Integer suffix, String degree, Integer start, Integer size,String specailX);

}
