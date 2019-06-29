package com.jju.cfiltering.dao;

import java.util.List;
import java.util.Set;

import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.Pagination;

public interface GetBooksAllInfoDao extends BaseDao<Books> {

	public List<Books> getAllBooks(Integer typeId);

	public List<Books> getOnePageBookInfo(Integer pageNumber, Integer pageSize, Integer typeId);

	//public void removeOneBook(Integer bookId,);

	public List<Books> getAllbooksByCondition(StringBuffer hql,String getBookByConditionPagination,Pagination pg);

	public Books getOneBook(Integer bookDatabaseId);

	public void removeOneBook(Books book, Integer authorId, Set<BooksImg> booksImgSet);

}
