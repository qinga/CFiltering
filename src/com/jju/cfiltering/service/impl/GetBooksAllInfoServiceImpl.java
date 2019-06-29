package com.jju.cfiltering.service.impl;

import java.util.List;
import java.util.Set;

import com.jju.cfiltering.dao.GetBooksAllInfoDao;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.BooksType;
import com.jju.cfiltering.entity.Pagination;
import com.jju.cfiltering.service.GetBooksAllInfoService;

public class GetBooksAllInfoServiceImpl implements GetBooksAllInfoService {

	
	private GetBooksAllInfoDao gbd;
	public List<Books> getAllBooks(Integer typeId) {
		return gbd.getAllBooks(typeId);
	}

	public void setGbd(GetBooksAllInfoDao gbd) {
		this.gbd = gbd;
	}

	@Override
	public List<Books> getOnePageBookInfo(Integer pageNumber, Integer pageSize, Integer typeId) {
		return gbd.getOnePageBookInfo( pageNumber, pageSize, typeId);
	}

	@Override
	public void removeOneBook(Integer bookId) {
		Books book = gbd.getById(bookId);
		
		Integer bookDataId = book.getId();
		Integer authorId = book.getAuthor().getId();
		//书籍和出版社解除关系
		book.setPublish(null);
		//和书籍作者断开关系
		book.setAuthor(null);
		
		//图片和书籍解除关系
		Set<BooksImg> booksImgSet = book.getBooksImg();
		for(BooksImg s: booksImgSet) {
			s.setBooks(null);
		}
		
		//书籍和书的类型关系解除
		book.setBooksType(null);
		//params: 书籍id,作者id,书籍图片id
		gbd.saveOrUpdate(book);
		//删除
		gbd.removeOneBook(book, authorId, booksImgSet);
	}

	@Override
	public List<Books> getBooksByCondition(Pagination pg,String getBookByConditionPagination) {
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct book from com.jju.cfiltering.entity.Books book left join fetch book.booksType as type");
		Integer i =0,j = 0;
		
		if(pg.getAuthorName() != null) {
			hql.append(" left join fetch book.author as boAuthor where boAuthor.name like '%"+pg.getAuthorName()+"%' ");
			j++;
		}
		if(pg.getPublishName() != null) {
			if(j==1){
				hql.delete(hql.indexOf("left join fetch book.author"), hql.length());
				hql.append(" left join fetch book.author as boAuthor left join fetch book.publish as boPublish where boAuthor.name like '%"+pg.getAuthorName()+"%' and boPublish.name = '"+pg.getPublishName()+"'");
				j++;
			}else {
				hql.append(" left join fetch book.publish as boPublish where boPublish.name = '"+pg.getPublishName()+"' ");
				j++;
			}
		}
		if(pg.getBookName() != null) {
			if(j != 0) {
				hql.append(" and book.name like '%"+pg.getBookName()+"%' ");
			}else {
				hql.append(" where book.name like '%"+pg.getBookName()+"%' ");
				i++;
			}
		}
		if(pg.getNewBookFlag() != null) {
			if(i <=1 && j >= 0) {
				hql.append(" and book.newBookFlag = '"+pg.getNewBookFlag()+"' ");
			}else 
				hql.append(" where book.newBookFlag = '"+pg.getNewBookFlag()+"' ");
		}
		if(pg.getSkillBookFlag() != null) {
			if(i <= 1 && j >= 0) {
				hql.append(" and book.skillBoostFlag = '"+pg.getSkillBookFlag()+"' ");
			}else 
				hql.append(" where book.skillBoostFlag = '"+pg.getSkillBookFlag()+"' ");
		}
		if(pg.getBeginBookFlag() != null) {
			if(i <= 1 && j >= 0) {
				hql.append(" and book.beginBookFlag = '"+pg.getBeginBookFlag()+"' ");
			}else 
				hql.append(" where book.beginBookFlag = '"+pg.getBeginBookFlag()+"' ");
		}
		if(pg.getOrginalBookFlag() != null) {
			if(i <= 1 && j >= 0) {
				hql.append(" and book.beginBookFlag = 0 and book.skillBoostFlag = 0 and book.newBookFlag = 0 ");
			}else 
				hql.append(" where book.beginBookFlag = 0 and book.skillBoostFlag = 0 and book.newBookFlag = 0");
		}
		if(i == 0 && j==0 ) {
			hql.insert(hql.indexOf("and"), " where type.id = '"+pg.getTypeId()+"' ");
		}else {
			hql.append("  and type.id = '"+pg.getTypeId()+"' ");
		}
		
		System.out.println(hql);
		return gbd.getAllbooksByCondition(hql, getBookByConditionPagination, pg);
	}

	@Override
	public Books getOneBook(Integer bookDatabaseId) {
		
		return gbd.getOneBook(bookDatabaseId);
	}
	
}
