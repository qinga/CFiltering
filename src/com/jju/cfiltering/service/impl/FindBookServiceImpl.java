package com.jju.cfiltering.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.jju.cfiltering.dao.FindBookDao;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.service.FindBookService;
import com.jju.cfiltering.util.EnCode;
import com.jju.cfiltering.util.PinyinUtil;

public class FindBookServiceImpl implements FindBookService{

	private FindBookDao findBookDao;
	@Override
	public Map<String,String> findBook(List<Books> booksCondition,Map<String,String> listBooksResult) {
		
		return findBookDao.findBook(booksCondition,listBooksResult);
	}
	public void setFindBookDao(FindBookDao findBookDao) {
		this.findBookDao = findBookDao;
	}
	
	@Override
	public Map<String, String> getPageBooks(String dataId) {
		String[] dataArrId = dataId.split("&");
		if(dataArrId.length == 1) {
			return findBookDao.getPageBooks(dataArrId[0]);
		}else {
			return findBookDao.getPageBooks(dataArrId[0],dataArrId[1]);
		}
	}
	/*
	 * 根据输入的条件查询书籍结果
	*/
	@Override
	public Map<String, String> findBooksBySearch(String typeId, String searchValue, String direcId) {
		Integer result = EnCode.isCnorEn(searchValue);
		StringBuilder sb = new StringBuilder();
		Map<String, String> listBooksBySearch = new HashMap<String, String>();
		Map<String, String> secondBooksBySearch = new HashMap<String, String>();
		StringBuilder strAll = new StringBuilder();
		strAll.append(" SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
				" FROM books AS bo  " + 
				" LEFT JOIN author AS au  " + 
				" ON bo.author_id = au.id  " + 
				" LEFT JOIN books_bookstype_map as btmp" +
				" ON btmp.books_id = bo.id  " + 
				" LEFT JOIN books_type as bty " + 
				" ON bty.id = btmp.bookstype_id " + 
				" ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
		switch (result) {
			case EnCode.ENGLISH:
				if( Integer.parseInt(direcId)==0 && typeId == null) {
					sb.append(" SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
							" FROM books AS bo  " + 
							" LEFT JOIN author AS au  " + 
							" ON bo.author_id = au.id  " + 
							" LEFT JOIN books_bookstype_map as btmp" +
							" ON btmp.books_id = bo.id  " + 
							" LEFT JOIN books_type as bty " + 
							" ON bty.id = btmp.bookstype_id " + 
							" WHERE  " + 
							" bo.name LIKE \"%"+searchValue+"%\" " + 
							" ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
					//只检索英文，且没有方向的(包含检索词的)。
					listBooksBySearch = findBookDao.getSearchValue(sb.toString() , searchValue);
					//查出所有书籍，转换成拼音
					sb.delete(0, sb.length());
					//拿到所有书籍
					secondBooksBySearch = findBookDao.getSearchValue(strAll.toString());
					//中文匹配处理
					secondBooksBySearch = PinyinUtil.pinyin(secondBooksBySearch, searchValue);
					//设置查询书籍的数量
					listBooksBySearch.putAll(secondBooksBySearch);
				}else if(Integer.parseInt(direcId) != 0 && typeId == null) {
					sb.append("SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
							"FROM books AS bo  " + 
							"LEFT JOIN author AS au  " + 
							"ON bo.author_id = au.id  " + 
							"LEFT JOIN books_bookstype_map as btmp  " + 
							"ON btmp.books_id = bo.id  " + 
							"LEFT JOIN books_type as bty  " + 
							"ON bty.id = btmp.bookstype_id  " + 
							"LEFT JOIN book_direction as bdre  " + 
							"ON bdre.id = bty.book_direction  " + 
							"WHERE  " + 
							"bdre.id = "+ Integer.parseInt( direcId ) +" AND  " + 
							"bo.name LIKE \"%"+searchValue+"%\"  " + 
							"ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
					//只检索英文，带有方向的书籍。
					listBooksBySearch = findBookDao.getSearchValue(sb.toString(), searchValue);
					//查出所有书籍，转换成拼音
					sb.delete(0, sb.length());
					
					//拿到所有书籍
					secondBooksBySearch = findBookDao.getSearchValue(strAll.toString());
					//中文匹配处理
					secondBooksBySearch = PinyinUtil.pinyin(secondBooksBySearch, searchValue);
					//设置查询书籍的数量
					listBooksBySearch.putAll(secondBooksBySearch);
				}else if(Integer.parseInt(direcId) == 0 && typeId != null) {
					sb.append("SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
							"FROM books AS bo  " + 
							"LEFT JOIN author AS au  " + 
							"ON bo.author_id = au.id  " + 
							"LEFT JOIN books_bookstype_map as btmp  " + 
							"ON btmp.books_id = bo.id  " + 
							"LEFT JOIN books_type as bty  " + 
							"ON bty.id = btmp.bookstype_id  " + 
							"WHERE bty.id = "+Integer.parseInt(typeId)+" AND bo.name LIKE \"%"+searchValue+"%\"  " + 
							"ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
					//只检索英文，带有书籍方向类别的那种。
					listBooksBySearch =  findBookDao.getSearchValue(sb.toString(), searchValue);
					//查出所有书籍，转换成拼音
					sb.delete(0, sb.length());
					//拿到所有书籍
					secondBooksBySearch = findBookDao.getSearchValue(strAll.toString());
					//中文匹配处理
					secondBooksBySearch = PinyinUtil.pinyin(secondBooksBySearch, searchValue);
					//设置查询书籍的数量
					listBooksBySearch.putAll(secondBooksBySearch);
				}
				break;
			case EnCode.CHINESE:
			case EnCode.FIX_EN_ZH:	
					if( Integer.parseInt(direcId)==0 && typeId == null) {
						sb.append(" SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
								" FROM books AS bo  " + 
								" LEFT JOIN author AS au  " + 
								" ON bo.author_id = au.id  " + 
								" LEFT JOIN books_bookstype_map as btmp" +
								" ON btmp.books_id = bo.id  " + 
								" LEFT JOIN books_type as bty " + 
								" ON bty.id = btmp.bookstype_id " + 
								" WHERE  " + 
								" bo.name LIKE \"%"+searchValue+"%\" " + 
								" ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
						//只检索中文，且没有方向的。
						listBooksBySearch = findBookDao.getSearchValue(sb.toString() , searchValue);
						
					}else if(Integer.parseInt(direcId) != 0 && typeId == null) {
						sb.append("SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
								"FROM books AS bo  " + 
								"LEFT JOIN author AS au  " + 
								"ON bo.author_id = au.id  " + 
								"LEFT JOIN books_bookstype_map as btmp  " + 
								"ON btmp.books_id = bo.id  " + 
								"LEFT JOIN books_type as bty  " + 
								"ON bty.id = btmp.bookstype_id  " + 
								"LEFT JOIN book_direction as bdre  " + 
								"ON bdre.id = bty.book_direction  " + 
								"WHERE  " + 
								"bdre.id = "+ Integer.parseInt( direcId ) +" AND  " + 
								"bo.name LIKE \"%"+searchValue+"%\"  " + 
								"ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
						//只检索中文，带有方向的书籍。
						listBooksBySearch = findBookDao.getSearchValue(sb.toString(), searchValue);
					}else if(Integer.parseInt(direcId) == 0 && typeId != null) {
						sb.append("SELECT bo.id, bo.name, bo.book_version, au.name AS authorName, bty.name AS btyName " + 
								"FROM books AS bo  " + 
								"LEFT JOIN author AS au  " + 
								"ON bo.author_id = au.id  " + 
								"LEFT JOIN books_bookstype_map as btmp  " + 
								"ON btmp.books_id = bo.id  " + 
								"LEFT JOIN books_type as bty  " + 
								"ON bty.id = btmp.bookstype_id  " + 
								"WHERE bty.id = "+Integer.parseInt(typeId)+" AND bo.name LIKE \"%"+searchValue+"%\"  " + 
								"ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time");
						//只检索中文，带有书籍方向类别的那种。
						listBooksBySearch =  findBookDao.getSearchValue(sb.toString(), searchValue);
					}
				break;
			}
		return listBooksBySearch;
	}
	@Override
	public List<Object[]> findBookByID(Integer bookID) {
		
		return findBookDao.findBookByID(bookID);
	}
	@Override
	public List<Object[]> findPinLunByID(Integer bookID) {
		return findBookDao.findPinLunByID(bookID);
	}
}
