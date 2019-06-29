package com.jju.cfiltering.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface GetBooksTypeInfoService {

	public List<String> getBooksTypeInfo(Integer dataKey);

	public JSONObject getAllBookById(String searchData, String btyName,String searchValue, Integer searchStart, Integer searchSize);

	public Integer getBooksCount();

	public JSONObject getBooksAndType(Integer pageStrat, Integer pageSize,String type);

	public JSONObject getBooksByDir(Integer direct, Integer suffix, String degree, Integer start, Integer size,String specailX);

}
