package com.jju.cfiltering.service;

import java.util.List;

import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.Pagination;

public interface GetBooksAllInfoService {

	public List<Books> getAllBooks(Integer typeId);


	public List<Books> getOnePageBookInfo(Integer pageNumber, Integer pageSize,Integer typeId);

	public void removeOneBook(Integer bookId);


	public List<Books> getBooksByCondition(Pagination pg, String getBookByConditionPagination);


	public Books getOneBook(Integer bookDatabaseId);

}
