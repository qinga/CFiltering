package com.jju.cfiltering.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateCallback;

import com.jju.cfiltering.dao.GetBooksAllInfoDao;
import com.jju.cfiltering.entity.Author;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.BooksType;
import com.jju.cfiltering.entity.Pagination;

public class GetBooksAllInfoDaoImpl extends BaseDaoImpl<Books> implements GetBooksAllInfoDao {

	
	@Override
	public List<Books> getAllBooks(Integer typeId) {
		List<Books> resultBooks = new ArrayList<Books>();
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Books>>() {
			@Override
			public List<Books> doInHibernate(Session session) throws HibernateException {
				
				BooksType booksType = session.get(BooksType.class, typeId);
				for ( Books book : booksType.getBooks()) {
					resultBooks.add(book);
				}
				return resultBooks;
			}
		});
	}

	public List<Books> getOnePageBookInfo(Integer pageNumber, Integer pageSize, Integer typeId) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Books>>() {
			public List<Books> doInHibernate(Session session) throws HibernateException {
				String hql="select distinct book from com.jju.cfiltering.entity.Books book  left join fetch book.booksType as type where type.id=?";
				
				Query query = session.createQuery(hql);
				
				query.setParameter(0, typeId);
				query.setFirstResult((pageNumber-1)*pageSize);
				query.setMaxResults(pageSize);
				
				List<Books> resultBooks =(List<Books>) query.list();
				
				return resultBooks;
			}
			
		});
	}
	@Override
	public Books getById(Serializable id) {
		return super.getById(id);
	}
	
	@Override
	public void saveOrUpdate(Books t) {
		super.saveOrUpdate(t);
	}
	@Override
	public void removeOneBook(Books book, Integer authorId, Set<BooksImg> booksImgSet) {
		// TODO Auto-generated method stub
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		
		session.delete(session.load(Author.class, authorId));
		
		for(BooksImg bk : booksImgSet) {
			session.delete( session.load(BooksImg.class, bk.getId()) );
		}
		super.delete(book);
	}
	

	@Override
	public List<Books> getAllbooksByCondition(StringBuffer hql,String getBookByConditionPagination,Pagination pg) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Books>>() {
			public List<Books> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(new String(hql));
				if(!getBookByConditionPagination.trim().equals("")) {
					query.setFirstResult((pg.getPageNumber()-1)*pg.getPageSize());
					query.setMaxResults(pg.getPageSize());
				}
				List<Books> resultData= (List<Books> )query.list();
				return resultData;
			}
		});
	}
	@Override
	public Books getOneBook(Integer bookDatabaseId) {
		return super.getById(bookDatabaseId);
	}

}
