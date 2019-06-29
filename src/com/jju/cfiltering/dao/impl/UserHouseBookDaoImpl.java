package com.jju.cfiltering.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.dao.UserHouseBookDao;
import com.jju.cfiltering.util.CFileUtil;
import com.jju.cfiltering.util.HouseBookUtil;

public class UserHouseBookDaoImpl extends HibernateDaoSupport implements UserHouseBookDao{


	//用户收藏书籍
	@Override
	public Boolean saveHouBook(Integer bookID, Integer dbID) {
		 SessionFactory sdf =  this.getHibernateTemplate().getSessionFactory();
		 Session session = sdf.getCurrentSession();
		 Query query = null;
		 List<Object[]> result = null;
		 StringBuffer sb = new StringBuffer();
		 //先查询用户是否收藏过。。
		 sb.append("    SELECT ubm.id, ubm.user_id, ubm.books_id "
		 		    +" FROM user_bookhouse_map AS ubm Where ubm.user_id = ? AND ubm.books_id = ? ");
		 
		 query = session.createSQLQuery(sb.toString());
		 query.setParameter(0, dbID);
		 query.setParameter(1, bookID);
		 
		 result = query.list();
		 //表示用户已收藏。。
		 if( result.size() > 0) {
			 //则提示已收藏。
			 return false;
		 }else {
			 //用户没有收藏，并且进行收藏处理
			 sb.delete(0, sb.length());
			 sb.append(" INSERT INTO user_bookhouse_map VALUES(null, ?, ?, ?)");
			 query = session.createSQLQuery(sb.toString());
			 query.setParameter(0, CFileUtil.formateDate(new Date()));
			 query.setParameter(1, dbID);
			 query.setParameter(2, bookID);
			 query.executeUpdate();
			 
			 //同时，这个用户也给书籍评分。（先查询用户是否对这本书有评分记录）
			 sb.delete(0, sb.length());
			 sb.append(" SELECT id, score, user_id, books_id " + 
			 			" FROM user_book_score_map " + 
			 			" WHERE user_id = ? AND books_id = ? ");
			 query = session.createSQLQuery(sb.toString());
			 query.setParameter(0, dbID);
			 query.setParameter(1, bookID);
			 result = query.list();
			 //表明用户对这本书有评分记录
			 if(result.size() > 0 ) {
				 float score = (float) result.get(0)[1];
				 if(score == 10)
					 return true;
				 else if( score + 2 > 10)
					 score = 10;
				 else if( score + 2 <= 10)
					 score = score + 2;
				 
					 //表明用户已评过分，只需要添加评分即可(追加评分)
					 Integer id = (Integer) result.get(0)[0];
					 sb.delete(0, sb.length());
					 sb.append(" UPDATE user_book_score_map" + 
							   " SET score = "+score+" " +
							   " WHERE id ="+id+" ");
					 query = session.createSQLQuery(sb.toString());
					 query.executeUpdate();
					 return true;
			 }else {
				 //用户没有对此书有评分记录，需要插入记录
				 sb.delete(0, sb.length());
				 sb.append("INSERT INTO user_book_score_map VALUES(null, 2, ?, ?)");
				 query = session.createSQLQuery(sb.toString());
				 query.setParameter(0, dbID);
				 query.setParameter(1, bookID);
				 query.executeUpdate();
				 return true;
			 }
		 }
		
	}

	@Override
	public JSONObject findHouseBook(Integer dbID, Integer start, Integer size, String type) {
		 SessionFactory sdf =  this.getHibernateTemplate().getSessionFactory();
		 Session session = sdf.getCurrentSession();
		 Query query = null;
		 List<Object[]> result = null;
		 StringBuffer sb = new StringBuffer();
		 //初始化事件
		 if(type.equals("init")) {
			 //查询总数
			 sb.append("SELECT COUNT(*) FROM user_bookhouse_map WHERE user_id = ? ");
			 query = session.createSQLQuery(sb.toString());
			 query.setParameter(0, dbID);
			 BigInteger bookCount =  (BigInteger) query.uniqueResult();
			 Integer bookSize= new Integer(String.valueOf(bookCount));
			 //查询到12条记录。。
			 if(bookSize == 0) {
				 return null;
			 }else {
				 sb.delete(0, sb.length());
				 sb.append("SELECT house_time, books_id " + 
				 			" FROM user_bookhouse_map WHERE user_id = ? "
				 			+ " ORDER BY books_id LIMIT "+(start-1)*size+","+size+" ");
				 query = session.createSQLQuery(sb.toString());
				 query.setParameter(0, dbID);
				 result = query.list();
				 
				 ArrayList<String> houseTime = new ArrayList<String>();
				 sb.delete(0, sb.length());
				 sb.append("SELECT b.id,b.name AS boName, pb.name as pbName, au.name as AuName,b.book_version, bmg.img_addr " + 
				 		"	 FROM books AS b " + 
				 		"	 LEFT JOIN books_img AS bmg ON b.id = bmg.books_id " + 
				 		"	 LEFT JOIN books_bookstype_map as btmap ON btmap.books_id = b.id " + 
				 		"	 LEFT JOIN books_type as bty on bty.id = btmap.bookstype_id " + 
				 		"	 LEFT JOIN publish as pb ON pb.id = b.publish_id  " + 
				 		"    LEFT JOIN author as au ON au.id = b.author_id  " + 
				 		"	 WHERE bmg.priority = 1 AND ( ");
				 //将书籍id传入数组houseTime中
				 for(int j = 0;j<result.size();j++) {
						for(int k = 0;k<result.get(j).length;k++) {
							Object[] objs = result.get(j);
							if(k == 0) {
								houseTime.add(objs[k].toString());
							}
							else 
								sb.append("b.id ="+(Integer) objs[k]+" OR ");
						}
					}
				 //删除 "OR"
				 sb.delete(sb.lastIndexOf("OR"), sb.length());
				 sb.append(" ) ");
				 query = session.createSQLQuery(sb.toString());
				 result = query.list();
				 JSONObject data = HouseBookUtil.parseHouseBook(result, houseTime,bookSize);
				 houseTime = null;
				 return data;
			 }
		 }
		 //change事件
		 else {
			 sb.append("SELECT house_time, books_id " + 
			 			" FROM user_bookhouse_map WHERE user_id = ? "
			 			+ " ORDER BY books_id LIMIT "+(start-1)*size+","+size+" ");
			 query = session.createSQLQuery(sb.toString());
			 query.setParameter(0, dbID);
			 result = query.list();
			 
			 ArrayList<String> houseTime = new ArrayList<String>();
			 sb.delete(0, sb.length());
			 sb.append("SELECT b.id,b.name AS boName, pb.name as pbName, au.name AS auName,b.book_version, bmg.img_addr " + 
			 		"	 FROM books AS b " + 
			 		"	 LEFT JOIN books_img AS bmg ON b.id = bmg.books_id " + 
			 		"	 LEFT JOIN books_bookstype_map as btmap ON btmap.books_id = b.id " + 
			 		"	 LEFT JOIN books_type as bty on bty.id = btmap.bookstype_id " + 
			 		"	 LEFT JOIN publish as pb ON pb.id = b.publish_id  " + 
			 		"    LEFT JOIN author as au ON au.id = b.author_id  " + 
			 		"	 WHERE bmg.priority = 1 AND ( ");
			 for(int j = 0;j<result.size();j++) {
					for(int k = 0;k<result.get(j).length;k++) {
						Object[] objs = result.get(j);
						if(k == 0) {
							houseTime.add(objs[k].toString());
						}
						else 
							sb.append("b.id ="+(Integer) objs[k]+" OR ");
					}
				}
			 //删除 "OR"
			 sb.delete(sb.lastIndexOf("OR"), sb.length());
			 sb.append(" ) ");
			 query = session.createSQLQuery(sb.toString());
			 result = query.list();
			 JSONObject data = HouseBookUtil.parseHouseBook(result, houseTime,null);
			 houseTime = null;
			 return data;
		 }
	}

	//用户移除收藏的书籍，减两分。。
	@Override
	public Boolean removeHouseBook(Integer dbID, Integer bookID) {
		 SessionFactory sdf =  this.getHibernateTemplate().getSessionFactory();
		 Session session = sdf.getCurrentSession();
		 Query query = null;
		 StringBuffer sb = new StringBuffer();
		 sb.append(" DELETE FROM user_bookhouse_map WHERE user_id = ? AND books_id = ? ");
		 query = session.createSQLQuery(sb.toString());
		 query.setParameter(0, dbID);
		 query.setParameter(1, bookID);
		 query.executeUpdate();
		 
		 sb.delete(0, sb.length());
		 sb.append(" UPDATE user_book_score_map SET score = score -2 WHERE user_id = ? AND books_id = ? ");
		 query = session.createSQLQuery(sb.toString());
		 query.setParameter(0, dbID);
		 query.setParameter(1, bookID);
		 query.executeUpdate();
		 return true;
	}

	
	//用户分享了书籍，给书籍评分。。
	@Override
	public Boolean updateScore(Integer dbID, Integer bookID, Integer time) {
		 SessionFactory sdf =  this.getHibernateTemplate().getSessionFactory();
		 Session session = sdf.getCurrentSession();
		 Query query = null;
		 StringBuffer sb = new StringBuffer();
		 //先查询用户是否有评分记录，
		 sb.append("SELECT score FROM user_book_score_map WHERE user_id = ? AND books_id = ?");
		query = session.createSQLQuery(sb.toString());
		query.setParameter(0, dbID);
		query.setParameter(1, bookID);
		Float score = (Float) query.uniqueResult();
		//如果有评分记录，则，继续添加分数
		if( score != null) {
			Float s = score;
			if(time == null) {
				//表明用户已评过分，追加分数
				//让评分最高只能是10分。。
				if(s == 10) {
					//刚好是10分，直接返回。。
					return true;
				}else if( s + 2 <= 10) {
					s = s + 2;
				}else if( s + 2 > 10) {
					s = 10f;
				}
			}else {
				//特别标注是停留时间长达30秒时，评分
				if(s == 10) {
					//刚好是10分，直接返回。。
					return true;
				}else if( s + time <= 10) {
					s = s + time;
				}else if( s + time > 10) {
					s = 10f;
				}
			}
			//否则，追加评分。。
			sb.delete(0, sb.length());
			sb.append("UPDATE user_book_score_map SET score = "+ s +" WHERE user_id = ? AND books_id = ?");
			query = session.createSQLQuery(sb.toString());
			query.setParameter(0, dbID);
			query.setParameter(1, bookID);
			query.executeUpdate();
		}else {
			 //用户没有评过分，需要插入记录
			 sb.delete(0, sb.length());
			 if( time == null) {
				 sb.append("INSERT INTO user_book_score_map VALUES(null, 2, ?, ?)");
			 }else {
				 //特别标注是停留时间长达30秒时，评分
				 sb.append("INSERT INTO user_book_score_map VALUES(null, "+time+", ?, ?)");
			 }
			 query = session.createSQLQuery(sb.toString());
			 query.setParameter(0, dbID);
			 query.setParameter(1, bookID);
			 query.executeUpdate();
			 return true;
		}
		return true;
	}

	//bookInfor.html中用户的评论，星级评分。。
	@Override
	public Boolean updateStartPinScore(Integer dbID, Integer bookID, Integer starCount, String userComment) {
		 SessionFactory sdf =  this.getHibernateTemplate().getSessionFactory();
		 Session session = sdf.getCurrentSession();
		 Query query = null;
		 StringBuffer sb = new StringBuffer();
		 
		 //塞入数据
		 sb.append("INSERT INTO user_book_comments_map VALUES(null, ?, ?, ?, ?)");
		 query = session.createSQLQuery(sb.toString());
		 query.setParameter(0, userComment);
		 query.setParameter(1, CFileUtil.formateDate(new Date()));
		 query.setParameter(2, dbID);
		 query.setParameter(3, bookID);
		 query.executeUpdate();
		 //将前面的字符串删除
		 sb.delete(0, sb.length());
		 //查看是否有评分记录
		 sb.append("SELECT score FROM user_book_score_map WHERE user_id = ? AND books_id = ?");
			query = session.createSQLQuery(sb.toString());
			query.setParameter(0, dbID);
			query.setParameter(1, bookID);
			Float score = (Float) query.uniqueResult();
			//如果有评分记录，则，继续添加分数
			if( score != null) {
				//表明用户已评过分，追加分数
				//让评分最高只能是10分。。
				Float s = score;
				if(s == 10) {
					//刚好是10分，直接返回。。
					return true;
				}else if( s + starCount <= 10) {
					s = s + starCount;
				}else if( s + starCount > 10) {
					s = 10f;
				}
				sb.delete(0, sb.length());
				sb.append("UPDATE user_book_score_map SET score = "+ s +" WHERE user_id = ? AND books_id = ?");
				query = session.createSQLQuery(sb.toString());
				query.setParameter(0, dbID);
				query.setParameter(1, bookID);
				query.executeUpdate();
				return true;
			}else {
				//用户没有评过分，需要插入记录
				 sb.delete(0, sb.length());
				 sb.append("INSERT INTO user_book_score_map VALUES(null, ?, ?, ?)");
				 query = session.createSQLQuery(sb.toString());
				 query.setParameter(0, starCount);
				 query.setParameter(1, dbID);
				 query.setParameter(2, bookID);
				 query.executeUpdate();
				 return true;
			}
	}

	//默认评分，新用户
	@Override
	public void insertAutoPin(Integer id) {
		//新用户，表明要给所有书籍默认评分为1分。。
		 SessionFactory sdf =  this.getHibernateTemplate().getSessionFactory();
		 Session session = sdf.getCurrentSession();
		 Query query = null;
		 StringBuffer sb = new StringBuffer();
		 sb.append("SELECT id FROM books ORDER BY id ");
		 
		 query = session.createSQLQuery(sb.toString());
		 List<Integer[]> result= query.list();
		 
		
		 //查找出的书籍大于 0 
		 if( result.size() > 0 ) {
			 sb.delete(0, sb.length());
			 sb.append("INSERT INTO user_book_score_map VALUES ");
			 
			 for(int j = 0;j<result.size();j++) {
				 //id表示用户，后者表示
				 sb.append(" ( null, 1, "+id+", "+result.get(j)+"), ");
			 }
			 //删除最后的","
			 sb.delete(sb.lastIndexOf(","), sb.length());
			 System.out.println( sb.toString() );
			 query = session.createSQLQuery(sb.toString());
			 query.executeUpdate();
		 }
	}


}
