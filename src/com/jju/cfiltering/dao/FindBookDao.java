package com.jju.cfiltering.dao;

import java.util.List;
import java.util.Map;

import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.User;

public interface FindBookDao {

	public Map<String,String> findBook(List<Books> booksCondition,Map<String,String> listBooksResult);


	public Map<String, String> getPageBooks(String ...dataId);


	public Map<String,String> getSearchValue(String... strValue);


	public List<Object[]> findBookByID(Integer bookID);


	public List<Object[]> findPinLunByID(Integer bookID);

}
