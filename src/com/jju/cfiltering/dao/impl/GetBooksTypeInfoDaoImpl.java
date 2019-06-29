package com.jju.cfiltering.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.dao.GetBooksTypeInfoDao;
import com.jju.cfiltering.entity.BookDirection;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.util.ParseUtil;

public class GetBooksTypeInfoDaoImpl extends HibernateDaoSupport implements GetBooksTypeInfoDao {

	@Override
	public List<String> getBooksTypeInfo(Integer dataKey) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		String sqlQuery = "SELECT name FROM books_type WHERE book_direction = ?";
		
		Query query = session.createSQLQuery(sqlQuery);
		query.setParameter(0, dataKey);
		
		List<String> bookInfo =(List<String>) query.list();
		return bookInfo;		
	}

	@Override
	/*
	 *首页跳转至书籍类型页面
	 * 根据书籍ID，获取书籍所有
	 * @see com.jju.cfiltering.dao.GetBooksTypeInfoDao#getAllBooksById(java.lang.String, java.lang.String)
	 */
	public JSONObject getSearchBookCount(String btyName, String searchValue) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Query query = null;
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT DISTINCT count(*) " + 
				"		FROM books AS bo " + 
				"		LEFT JOIN author AS au " + 
				"		ON bo.author_id = au.id " + 
				"		LEFT JOIN publish AS pu " + 
				"		ON bo.publish_id = pu.id " + 
				"		LEFT JOIN books_img AS bmg " + 
				"		ON bo.id = bmg.books_id " + 
				"		LEFT JOIN books_bookstype_map as bmap " + 
				"		ON bo.id = bmap.books_id " + 
				"		LEFT JOIN books_type AS type " + 
				"		ON bmap.bookstype_id = type.id " + 
				"		LEFT JOIN book_direction as bdr " + 
				"		ON bdr.id = type.book_direction ");
		if(btyName.equals("0")) {
					sqlQuery.append(" where bmg.priority = 1 " + 
				                    "	and bo.name LIKE ? ");
					query = session.createSQLQuery(sqlQuery.toString());
					query.setParameter(0,  "%"+searchValue+"%");
		}else {
			sqlQuery.append(" where type.name = ? and bmg.priority = 1 " + 
				            "	and bo.name LIKE ? ");
			query = session.createSQLQuery(sqlQuery.toString());
			query.setParameter(0, btyName);
			query.setParameter(1, "%"+searchValue+"%");
		}
		
		BigInteger bookCount =  (BigInteger) query.uniqueResult();
		Integer count= new Integer(String.valueOf(bookCount));
		JSONObject js = new JSONObject();
		js.put("code", 200);
		js.put("msg", "COUNT");
		JSONObject j = new JSONObject();
		j.put("count", count);
		js.put("data", j);

		return js;
	}

	@Override
	/*首页跳转至书籍类型页面
	 * 根据书籍类型获取所有
	 * @see com.jju.cfiltering.dao.GetBooksTypeInfoDao#getAllBookByTypeName(java.lang.String, java.lang.String)
	 */
	public JSONObject getAllBookByTypeName(String searchData, String searchValue, Integer start, Integer size) {
		List<Object[]> reBooks = new ArrayList<Object[]>();
		List<Object[]> secondBooks = new ArrayList<Object[]>();
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Query query = null;
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append( "SELECT DISTINCT bo.begin_book_flag as bobegin,bo.skill_boost_flag as boskill,bo.new_book_flag as bonew,bo.id as bookId, bo.name as bookName, bo.book_desc as bookDesc,type.name as btyName,au.name as AuName,pu.name as PuName,bmg.img_addr as bmgAddr,bdr.id as bdrId,type.id as typeId " + 
				"				 FROM books AS bo" + 
				"				 LEFT JOIN author AS au" + 
				"				 ON bo.author_id = au.id" + 
				"				 LEFT JOIN publish AS pu" + 
				"				 ON bo.publish_id = pu.id" + 
				"				 LEFT JOIN books_img AS bmg" + 
				"				 ON bo.id = bmg.books_id" + 
				"				 LEFT JOIN books_bookstype_map as bmap" + 
				"				 ON bo.id = bmap.books_id" + 
				"				 LEFT JOIN books_type AS type" + 
				"				 ON bmap.bookstype_id = type.id" + 
				"				 LEFT JOIN book_direction as bdr" + 
				"				 ON bdr.id = type.book_direction" );
		if(searchData.equals("0")) {
			       sqlQuery.append("	where  bmg.priority = 1" +
					               "    and bo.name LIKE ? LIMIT "+(start-1)*size+","+size+" ");
			        query = session.createSQLQuery(sqlQuery.toString());
					query.setParameter(0, "%"+searchValue+"%" );
		}else {
					sqlQuery.append("	where type.name = ? and bmg.priority = 1" +
							        "   and bo.name LIKE ? LIMIT "+(start-1)*size+","+size+" ");
					query = session.createSQLQuery(sqlQuery.toString());
					query.setParameter(0, searchData );
					query.setParameter(1, "%"+searchValue+"%" );
		}
		
		reBooks = query.list();
		Integer bdrId = null;
		Integer typeId = null;
		for(int j = 0;j<reBooks.size();j++) {
			for(int k = 0;k<reBooks.get(j).length;k++) {
				Object[] objs = reBooks.get(j);
				if( k == 10 ) {
					 bdrId = (Integer) objs[k];
				}
				else if( k == 11) {
					 typeId = (Integer) objs[k];
				}
			}	
		}
		String sql = "SELECT bty.name,bty.id AS btyId  " + 
				"FROM books_type as bty  " + 
				"LEFT JOIN book_direction AS bdr  " + 
				"ON bdr.id = bty.book_direction  " + 
				"WHERE bdr.id=? and bty.id != ?";
		query = session.createSQLQuery(sql);
		query.setParameter(0, bdrId);
		query.setParameter(1, typeId);
		secondBooks = query.list();
		if( reBooks.size() == 0 && secondBooks.size() == 0 ) {
			String hql = " SELECT  bty.id AS btyId, bty.name AS btyName FROM BooksType AS bty ";
			query = session.createQuery(hql);
			List<Object[]> bdList = query.list();
			if(bdList != null) {
				 JSONObject result = ParseUtil.parseHql(bdList);
				 return result;
			}
		}else {
			//对数据进行处理
			JSONObject result = ParseUtil.parseSQL(reBooks, secondBooks);
			return result;
		}
		return null;
	}

	@Override
	public Integer getBooksCount() {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Query query = session.createSQLQuery(" select count(*) from books ");
		BigInteger bookCount =  (BigInteger) query.uniqueResult();
		Integer bookSize= new Integer(String.valueOf(bookCount));
		return bookSize > 0 ? bookSize : 0 ;
	}

	@Override
	public JSONObject getBooksAndType(Integer pageStart, Integer pageSize, String type) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Query query = null;
		StringBuilder hql = new StringBuilder();
		if( type.equals("init")) {
			hql.append(" SELECT  bty.id AS btyId, bty.name AS btyName FROM BooksType AS bty ");
			query = session.createQuery(hql.toString());
			List<Object[]> bdList = query.list();
			//查询所有类型
			if(bdList != null) {
				hql.delete(0, hql.length());
				//查找所有书籍
				hql.append("SELECT DISTINCT bo.id AS bookId, bo.name AS bookName, bo.book_desc AS bookDesc,au.name AS AuName,bmg.img_addr AS bmgAddr,pu.name AS puName,bty.name AS btyName  " + 
						"								   FROM books AS bo, " + 
						"										books_bookstype_map AS bmap, " + 
						"										books_type AS bty, " + 
						"										author AS au, " + 
						"										books_img AS bmg, " + 
						"										publish AS pu " + 
						"								WHERE bo.id = bmap.books_id AND bty.id = bmap.bookstype_id " + 
						"								AND bo.publish_id = pu.id AND bo.id = bmg.books_id " + 
						"								AND bo.author_id = au.id " + 
						"							    AND bmg.priority = 1 LIMIT "+ (pageStart - 1)*pageSize +", "+ pageSize+" ");
				
				query = session.createSQLQuery(hql.toString());
				List<Object[]> bkList =  query.list();
				//查找所有类型
				JSONObject result = ParseUtil.parseHql(bdList, bkList);
				return result;
			}
		}else  {
			hql.append("SELECT DISTINCT bo.id AS bookId, bo.name AS bookName, bo.book_desc AS bookDesc,au.name AS AuName,bmg.img_addr AS bmgAddr,pu.name AS puName,bty.name AS btyName  " + 
					"								   FROM books AS bo, " + 
					"										books_bookstype_map AS bmap, " + 
					"										books_type AS bty, " + 
					"										author AS au, " + 
					"										books_img AS bmg, " + 
					"										publish AS pu " + 
					"								WHERE bo.id = bmap.books_id AND bty.id = bmap.bookstype_id " + 
					"								AND bo.publish_id = pu.id AND bo.id = bmg.books_id " + 
					"								AND bo.author_id = au.id " + 
					"							    AND bmg.priority = 1 LIMIT "+ (pageStart - 1)*pageSize +", "+ pageSize+" ");
			query = session.createSQLQuery(hql.toString());
			List<Object[]> bkSearch =  query.list();
			JSONObject result = ParseUtil.parsePageSearch(bkSearch);
			return result;
		}
		return null;
	}

	@Override
	public JSONObject getBooksByDir(Integer direct, Integer suffix, String degree, Integer start, Integer size, String specail) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Query query = null;
		StringBuilder sb = new StringBuilder();
		if(specail == null) {
			if( (direct != null && suffix == null && degree == null) || suffix == 0) {
				/*if(suffix == 0) {
					
				}*/
				sb.append(" SELECT DISTINCT bo.id as bookId, bo.name as bookName, " + 
						"   bo.book_desc as bookDesc,type.name as btyName, " + 
						"    au.name as AuName,pu.name as PuName,bmg.img_addr as bmgAddr " + 
						"		FROM books AS bo " + 
						"		LEFT JOIN author AS au " + 
						"		ON bo.author_id = au.id  " + 
						"		LEFT JOIN publish AS pu " + 
						"		ON bo.publish_id = pu.id  " + 
						"		LEFT JOIN books_img AS bmg " + 
						"		ON bo.id = bmg.books_id " + 
						"		LEFT JOIN books_bookstype_map as bmap " + 
						"		ON bo.id = bmap.books_id " + 
						"		LEFT JOIN books_type AS type " + 
						"		ON bmap.bookstype_id = type.id " + 
						"		LEFT JOIN book_direction as bdr " + 
						"		ON bdr.id = type.book_direction " + 
						"		WHERE bdr.id = "+direct+" AND bmg.priority = 1 " + 
						"		LIMIT "+(start-1)*size+","+size+" ");
				query = session.createSQLQuery(sb.toString());
				List<Object[]> listBooks = query.list();
				sb.delete(0, sb.length());
				sb.append("SELECT  bty.id AS btyId, bty.name AS btyName " + 
						" FROM books_type AS bty " + 
						" LEFT JOIN book_direction AS bdre " + 
						" ON bdre.id = bty.book_direction WHERE bdre.id = "+direct+" ");
				query = session.createSQLQuery(sb.toString());
				List<Object[]> listType = query.list();
				
				JSONObject boType = ParseUtil.parseBoType(listBooks, listType);
				return boType;
			}else if(suffix != null) {
				sb.append(" SELECT DISTINCT bo.id as bookId, bo.name as bookName, " + 
						"   bo.book_desc as bookDesc,type.name as btyName, " + 
						"   au.name as AuName,pu.name as PuName,bmg.img_addr as bmgAddr " + 
						"	FROM books AS bo " + 
						"	LEFT JOIN author AS au " + 
						"	ON bo.author_id = au.id  " + 
						"	LEFT JOIN publish AS pu " + 
						"	ON bo.publish_id = pu.id  " + 
						"	LEFT JOIN books_img AS bmg " + 
						"	ON bo.id = bmg.books_id " + 
						"	LEFT JOIN books_bookstype_map as bmap " + 
						"	ON bo.id = bmap.books_id " + 
						"	LEFT JOIN books_type AS type " + 
						"	ON bmap.bookstype_id = type.id " + 
						"	LEFT JOIN book_direction as bdr " + 
						"	ON bdr.id = type.book_direction ");
				if( degree.equals("0")) {
					sb.append("		WHERE type.id = "+suffix+" AND bmg.priority = 1 " + 
							  "		LIMIT "+(start-1)*size+","+size+" ");
				}else {
					sb.append("		WHERE type.id = "+suffix+" AND bmg.priority = 1 AND "+degree+" = 1 " + 
							  "		LIMIT "+(start-1)*size+","+size+" ");
				}
				//如果方向不为0,则，根据类型查出所有书籍
				query = session.createSQLQuery(sb.toString());
				List<Object[]> listBooks = query.list();
				if(direct != 0) {
					JSONObject resBo = ParseUtil.parseBo(listBooks);
					return resBo;
				}else if(direct == 0) {
					//如果方向为0,则表明需要根据书籍类型，确定方向，同时查询出该类型下所有书籍；
					sb.delete(0, sb.length());
					//查询书籍方向id
					sb.append("SELECT bdre.id as bdreId FROM book_direction AS bdre " + 
							"LEFT JOIN books_type AS bty  " + 
							"ON bdre.id = bty.book_direction " + 
							"WHERE bty.id = "+suffix+" ");
					query = session.createSQLQuery(sb.toString());
					//方向id;
					Integer bookSize =  (Integer) query.uniqueResult();
					sb.delete(0, sb.length());
					sb.append("SELECT  bty.id AS btyId, bty.name AS btyName " + 
							" FROM books_type AS bty " + 
							" LEFT JOIN book_direction AS bdre " + 
							" ON bdre.id = bty.book_direction WHERE bdre.id = "+bookSize+" ");
					query = session.createSQLQuery(sb.toString());
					//查询书籍类别
					List<Object[]> listType = query.list();
					
					JSONObject boType = ParseUtil.parseBoType(listBooks, listType);
					if(boType.getJSONObject("data").getJSONObject("book") == null) {
						return boType;
					}
					boType.getJSONObject("data").getJSONObject("book").put("bookDire", bookSize);
					boType.put("msg", "KNOEDIRE");
					return boType;
				}
			}
		}
		if(specail != null) {
			sb.append("SELECT DISTINCT bo.id as bookId, bo.name as bookName, " + 
					"		bo.book_desc as bookDesc,type.name as btyName," + 
					"		au.name as AuName,pu.name as PuName,bmg.img_addr as bmgAddr" + 
					"		FROM books AS bo" + 
					"		LEFT JOIN author AS au" + 
					"		ON bo.author_id = au.id" + 
					"		LEFT JOIN publish AS pu" + 
					"		ON bo.publish_id = pu.id" + 
					"		LEFT JOIN books_img AS bmg" + 
					"		ON bo.id = bmg.books_id" + 
					"		LEFT JOIN books_bookstype_map as bmap" + 
					"		ON bo.id = bmap.books_id" + 
					"		LEFT JOIN books_type AS type" + 
					"		ON bmap.bookstype_id = type.id" + 
					"		LEFT JOIN book_direction as bdr" + 
					"		ON bdr.id = type.book_direction  ");
			//方向，分类全部为全部，则查所有书籍标志位degree的
			if(direct == 0 && suffix == 0 && !degree.equals("0")) {
				sb.append(" WHERE bo."+degree+" = 1 and bmg.priority = 1");
				query = session.createSQLQuery( sb.toString() );
				List<Object[]> books = query.list();
				return ParseUtil.parseBo(books);
			}else if(direct != 0 && suffix == 0) {
				if( !degree.equals("0") ) {
					sb.append(" WHERE bo."+degree+" = 1 and bmg.priority = 1 and bdr.id = "+direct+" ");
				}else {
					sb.append(" WHERE  bmg.priority = 1 and bdr.id = "+direct+" ");
				}
				query = session.createSQLQuery(sb.toString());
				List<Object[]> books = query.list();
				return ParseUtil.parseBo(books);
			}else if(  suffix != 0 && direct !=0 ) {
				if( degree.equals("0") ) {
					//当分类不为全部时，方向不为全部时，难度为全部时；
					sb.append(" WHERE  bmg.priority = 1 and type.id = "+suffix+" ");
					query = session.createSQLQuery(sb.toString());
					List<Object[]> books = query.list();
					return ParseUtil.parseBo(books);
				}else {
					//当分类不为全部时，方向不为全部时，难度不为全部，且随意点时。
					sb.append(" WHERE  bo."+degree+" = 1 and bmg.priority = 1 and type.id = "+suffix+" ");
					query = session.createSQLQuery(sb.toString());
					List<Object[]> books = query.list();
					return ParseUtil.parseBo(books);
				}
			}
			
		}
		return null;
	}
}	
