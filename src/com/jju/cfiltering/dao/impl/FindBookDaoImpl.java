package com.jju.cfiltering.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.jju.cfiltering.dao.FindBookDao;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.User;

public class FindBookDaoImpl extends HibernateDaoSupport implements FindBookDao {
	
	
	public void putData(Map<String,String> listBooksResult, List<Object[]> tempResult, String strFlag) {
		for(int j = 0;j<tempResult.size();j++) {
			for(int k = 0;k<tempResult.get(j).length;k++) {
				Object[] objs = tempResult.get(j);
				if( k == 0 ) 
					 listBooksResult.put(strFlag + "Id" + j, objs[k].toString());
				else if( k == 1)
					 listBooksResult.put(strFlag + "Name" + j, objs[k].toString());
				else if( k == 2)
					 listBooksResult.put(strFlag + "Addr" + j, objs[k].toString());
				else if( k == 3)
					 listBooksResult.put(strFlag + "Mark" + j, objs[k].toString());
			}						
		}
		listBooksResult.put(strFlag + "Count", tempResult.size()+"");
	}

	/**
	 * 主页书籍查询，三大块的书籍，技能提升，小白入门，新上线书籍
	 */
	@Override
	public Map<String,String> findBook(List<Books> booksCondition,Map<String,String> listBooksResult) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Query query = null;
		List<Object[]> tempResult = null;
		String sqlQuery =  " SELECT b.id,b.name,bmg.img_addr,bty.name as btyName " + 
				"            FROM books AS b " + 
				"            LEFT JOIN books_img AS bmg ON b.id = bmg.books_id  " + 
				"            LEFT JOIN books_bookstype_map as btmap ON btmap.books_id = b.id  " + 
				"            LEFT JOIN books_type as bty on bty.id = btmap.bookstype_id  " + 
				"            WHERE b.begin_book_flag = ?     " + 
				"            AND bmg.priority = 1 ORDER BY create_time<now(),if( create_time<now(), 0,create_time ),create_time desc LIMIT 0,5";
		for(int i = 0;i<booksCondition.size();i++) {
			switch(i) {
				case 0:
					query = session.createSQLQuery(sqlQuery);
					query.setParameter(0, booksCondition.get(i).getBeginBookFlag());
					tempResult = query.list();
					if(tempResult.size()>0) {
						putData(listBooksResult, tempResult, "bookBegin");
					};
					break;
				case 1:
					sqlQuery = sqlQuery.replace("b.begin_book_flag", "b.new_book_flag");
					sqlQuery = sqlQuery.replace("0,5", "0,10");
					query = session.createSQLQuery(sqlQuery);
					query.setParameter(0, booksCondition.get(i).getNewBookFlag());	
					tempResult = query.list();
					if(tempResult.size()>0) {
						putData(listBooksResult, tempResult, "bookNew");
					};
					break;
					
				case 2:
					sqlQuery = sqlQuery.replace("b.new_book_flag", "b.skill_boost_flag");
					sqlQuery = sqlQuery.replace("0,5", "0,10");
					query = session.createSQLQuery(sqlQuery);
					query.setParameter(0, booksCondition.get(i).getSkillBoostFlag());
					tempResult = query.list();
					if(tempResult.size()>0) {
						putData(listBooksResult, tempResult, "bookSkill");
					};
				break;
			}
		}
		return listBooksResult.size()>0 ? listBooksResult : null;
	}


	/*
	 * 获取，首页菜单栏处的书籍、、、当鼠标滑动过时，获取四相应的四个个书籍
	 * @see com.jju.cfiltering.dao.FindBookDao#getPageBooks(java.lang.String[])
	 */
	@Override
	public Map<String, String> getPageBooks(String ...dataId) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Map<String, String> listBooksResult = new HashMap<String, String>();
		
		String sqlQuery = "SELECT DISTINCT b.id,b.name AS bookName,b.book_version AS bversion,bmg.img_addr,au.name  " + 
				"FROM books AS b  " + 
				"LEFT JOIN books_bookstype_map as btmap ON btmap.books_id = b.id  " + 
				"left JOIN books_type AS bty ON bty.id = btmap.bookstype_id  " + 
				"LEFT JOIN book_direction bdre ON bdre.id = bty.book_direction  " + 
				"LEFT JOIN books_img AS bmg ON bmg.books_id = b.id  " + 
				"LEFT JOIN author AS au ON au.id= b.author_id  " + 
				"WHERE bmg.priority = 1 AND ( bdre.id = ? ) ORDER BY b.id DESC ";
				if(dataId.length == 2) {
					sqlQuery = sqlQuery.replace("?", " ? OR bdre.id = ? ");
				}
				Query query = session.createSQLQuery(sqlQuery);
				
				if(dataId.length == 1) {
					query.setParameter(0, dataId[0]);
				}
				if(dataId.length == 2) {
					query.setParameter(0, dataId[0]);
					query.setParameter(1, dataId[1]);
				}
				List<Object[]> tempResult = new ArrayList<Object[]>();
				tempResult = query.list();
				if(tempResult.size() > 4) {
					tempResult.set(1, tempResult.get(tempResult.size()-1));
					tempResult.set(2, tempResult.get((tempResult.size()/2)));
					tempResult.set(3, tempResult.get(tempResult.size()-2));
					Integer i = 0;
					for (Iterator<Object[]> iterator = tempResult.iterator(); iterator.hasNext();) {
						iterator.next();
						i++;
						if(i>4) {
							iterator.remove();
						}
					}
					
				}
				if(tempResult.size()>0) {
					for(int j = 0;j<tempResult.size();j++) {
						for(int k = 0;k<tempResult.get(j).length;k++) {
							Object[] objs = tempResult.get(j);
							if( k == 0 ) 
								 listBooksResult.put("bookId"+j, objs[k].toString());
							else if( k == 1)
								 listBooksResult.put("bookName"+j, objs[k].toString());
							else if( k == 2)
								 listBooksResult.put("bookVersion"+j, objs[k].toString());
							else if( k == 3)
								 listBooksResult.put("bookAddr"+j, objs[k].toString());
							else if( k == 4)
								 listBooksResult.put("bookAuthor"+j, objs[k].toString());
						}						
					}
				};
				
		return listBooksResult.size() > 0 ? listBooksResult: null;
	}


	/*
	 * 搜索框检索书籍
	 * @see com.jju.cfiltering.dao.FindBookDao#getSearchValue(java.lang.String[])
	 */
	@Override
	public Map<String, String> getSearchValue(String ...strValue) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Map<String, String> listBooksResult = new HashMap<String, String>();
		
		Query query = session.createSQLQuery(strValue[0]);
		List<Object[]> tempResult = new ArrayList<Object[]>();
		tempResult = query.list();
		if(tempResult.size()>0) {
			for(int j = 0;j<tempResult.size();j++) {
				for(int k = 0;k<tempResult.get(j).length;k++) {
					Object[] objs = tempResult.get(j);
					if( k == 0 ) 
						 listBooksResult.put("bookId"+j, objs[k].toString());
					else if( k == 1)
						 listBooksResult.put("bookName"+j, objs[k].toString());
					else if( k == 2)
						 listBooksResult.put("bookVersion"+j, objs[k].toString());
					else if( k == 3)
						listBooksResult.put("bookAuthor"+j, objs[k].toString());
					else if( k == 4)
						 listBooksResult.put("bookBtyName"+j, objs[k].toString());
				}						
			}
		}
		return listBooksResult;
	}

	
	//booksInfor.html找到相应的书籍
	@Override
	public List<Object[]> findBookByID(Integer bookID) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		String sql = "SELECT DISTINCT books.id,books.name AS bookName,books.begin_book_flag as beginFlag, books.new_book_flag as newFlag, books.skill_boost_flag as skillFlg, books.book_desc as bookDesc,bmg.img_addr, " + 
				"				books.content as content,bty.name as typeName,bdre.name as bdreName, " + 
				"						(SELECT COUNT(id) FROM user_bookhouse_map WHERE user_bookhouse_map.books_id = books.id) " + 
				"								as houseCount, " +
				"						(SELECT COUNT(comments) FROM user_book_comments_map WHERE user_book_comments_map.books_id = books.id) " + 
				"							    as comments, "+ 
				"						(SELECT ROUND(SUM(score)/COUNT(score), 0) FROM user_book_score_map WHERE user_book_score_map.books_id = books.id) " + 
				"								as scoreCount " + 
				"							    FROM books " + 
				"	 LEFT JOIN books_bookstype_map as btmap ON btmap.books_id = books.id " + 
				"	 LEFT JOIN books_type AS bty ON bty.id = btmap.bookstype_id " + 
				"	 LEFT JOIN book_direction bdre ON bdre.id = bty.book_direction " +
				" 	 LEFT JOIN books_img AS bmg ON bmg.books_id = books.id " + 
				"	 WHERE books.id = ? AND bmg.priority = 1 ";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, bookID);
		List<Object[]> data = query.list();
		return data.size() > 0 ? data : null;
	}

	//用户对于书籍的评论，bookInfor.html星级评分处。
	@Override
	public List<Object[]> findPinLunByID(Integer bookID) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		String sql = "SELECT ubcm.comments, u.name " + 
					"  FROM user_book_comments_map AS ubcm  " + 
					"  LEFT JOIN user AS u ON u.id = ubcm.user_id " + 
					"  WHERE books_id = ? ";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, bookID);
		List<Object[]>result = query.list();
		return result.size() > 0 ? result : null;
	}

}
