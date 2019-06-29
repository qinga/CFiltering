package com.jju.cfiltering.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.jju.cfiltering.dao.UploadFileDao;
import com.jju.cfiltering.entity.Author;
import com.jju.cfiltering.entity.BookDirection;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.BooksType;
import com.jju.cfiltering.entity.Publish;



public class UploadFileDaoImpl extends HibernateDaoSupport implements UploadFileDao {

	@Override
	public Boolean insertUploadFiles(Books book, Author author, Publish publish, BooksType booksType,
			BookDirection bookDirection, List<BooksImg> booksImg) {
		return getHibernateTemplate().execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session) throws HibernateException {
				  String sqlQuery = "SELECT bo.name AS bo_name, au.name as au_name FROM books AS bo " + 
						"left JOIN author as au " + 
						"ON bo.author_id=au.id " + 
						"WHERE bo.name LIKE ? AND au.name LIKE ?";
				  Query sQuery = session.createSQLQuery(sqlQuery);
				  sQuery.setParameter(0, "%"+book.getName().trim()+"%");
				  sQuery.setParameter(1, "%"+author.getName().trim()+"%");
				  Object book_db =  sQuery.uniqueResult();
			
				  if(book_db==null) {
					  	//保存作者和书籍的表达
					  	  author.getBooks().add(book);
						  book.setAuthor(author);
						  session.save(author);
						  session.save(book);
						  
					 String booksTypeSql = "SELECT bt.id from books_type AS bt WHERE name=? AND book_direction =?";
					 Query query = session.createSQLQuery(booksTypeSql);
					  query.setParameter(0, booksType.getName());
					  query.setParameter(1, bookDirection.getId());
					  //是否存在该书的类别，如果存在，则类别不需要添加，否则添加书籍类别
					  List<Integer> booksType_db  = (List<Integer>)query.list();
					  
					  if(booksType_db.size() > 0) {
						  //书籍类别在书籍库的id
						  booksType.setId(booksType_db.get(0));
						  book.getBooksType().add(booksType);
						  booksType.getBooks().add(book);
						  
						  session.save(book);
						  session.saveOrUpdate(booksType);
					  }else {
						  book.getBooksType().add(booksType);
						  booksType.getBooks().add(book);
						  session.save(book);
						  session.saveOrUpdate(booksType);
					  }
					  
					  for(int i = 0;i<booksImg.size();i++) {
						  book.getBooksImg().add(booksImg.get(i));
						  booksImg.get(i).setBooks(book);
						  session.save(booksImg.get(i));
						  session.save(book);
					  }
					  //书籍和出版社保存，N-1关系
					  //出版社从数据库读出
					  book.setPublish(publish);
					  publish.getBooks().add(book);
					  session.save(book);
					  session.load(publish, publish.getId());
					  session.saveOrUpdate(publish);
					  //书籍类别和方向表达，一对多
					  bookDirection.getBooksType().add(booksType);
					  booksType.setBookDirection(bookDirection);
					  session.save(booksType);
					  session.load(bookDirection, bookDirection.getId());
					  session.save(bookDirection);
				  }else {
					  return false;
				  }
				return true;
			}
		});
	}
}
