package com.jju.cfiltering.service;

import java.util.List;
import java.util.Map;

import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.User;

public interface FindBookService {

	public Map<String,String> findBook(List<Books> booksCondition,Map<String,String> listBookResult);

	public Map<String, String> getPageBooks(String dataId);

	public Map<String, String> findBooksBySearch(String typeId, String searchValue, String direcId);

	public List<Object[]> findBookByID(Integer bookID);

	//bookInfor.html用户评论
	public List<Object[]> findPinLunByID(Integer bookID);

}
