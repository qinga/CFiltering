package com.jju.cfiltering.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.dao.RecommenderDao;

public class RecommenderDaoImpl extends HibernateDaoSupport implements RecommenderDao{

	private DataSource dataSource1;
	public DataModel findDataModel() {
        JDBCDataModel dataModel = null;
        try {
               dataModel = new MySQLJDBCDataModel(dataSource1,"user_book_score_map", "user_id", "books_id","score",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataModel;
	}
	public DataSource getDataSource11() {
		return dataSource1;
	}
	public void setDataSource1(DataSource dataSource) {
		this.dataSource1 = dataSource;
	}


	@Override
	public List<Object[]> findDetailBookByID(List<RecommendedItem> recommendations) {
		SessionFactory sdf = this.getHibernateTemplate().getSessionFactory();
		Session session = sdf.getCurrentSession();
		Query query = null;
		List<Object[]> recommen = null;
		StringBuffer sb = new StringBuffer();
		
		sb.append("  SELECT DISTINCT bo.id, bo.name AS boName, bo.book_desc AS content, bmg.img_addr, bty.name AS btyName, pb.name, au.name as auName " + 
				  "  FROM books AS bo  " + 
				  "  LEFT JOIN books_bookstype_map AS bmap " + 
				  "  ON bo.id = bmap.books_id  " + 
				  "  LEFT JOIN books_type as bty " + 
				  "  ON bmap.bookstype_id = bty.id  " + 
				  "  LEFT JOIN books_img as bmg  " + 
				  "  ON bmg.books_id = bo.id  "  +
				  "  LEFT JOIN publish AS pb " + 
				  "	 ON pb.id = bo.publish_id " + 
				  "	 LEFT JOIN author AS au " + 
				  "	 ON au.id = bo.author_id " + 
				  "  WHERE bmg.priority = 1 AND ( ");
		for( int i = 0; i < recommendations.size(); i++ ) {
			long id = recommendations.get(i).getItemID();
				sb.append(" bo.id = "+ id +" OR ");
		}
		if(recommendations.size() > 0) {
			sb.delete(sb.lastIndexOf("OR"), sb.length());
			sb.append(" ) ");
		}else {
			//否则没有用户历史评分记录，从数据库读取评分较高的那些
			sb.delete(0, sb.length());
			sb.append("SELECT DISTINCT bo.id, bo.name AS boName, bo.book_desc AS content, bmg.img_addr,bty.name AS btyName, pb.name AS pbName, au.name AS auName, boScore.score " + 
					  "FROM books AS bo " + 
					  "LEFT JOIN books_bookstype_map AS bmap  " + 
					  "ON bo.id = bmap.books_id  " + 
					  "LEFT JOIN books_type as bty " + 
					  "ON bmap.bookstype_id = bty.id  " + 
					  "LEFT JOIN books_img as bmg  " + 
					  "ON bmg.books_id = bo.id " + 
					  "  LEFT JOIN publish AS pb " + 
					  "	 ON pb.id = bo.publish_id " + 
					  "	 LEFT JOIN author AS au " + 
					  "	 ON au.id = bo.author_id " +
					  "LEFT JOIN user_book_score_map as boScore  " + 
					  "ON boScore.books_id = bo.id  " + 
					  "WHERE bmg.priority = 1 ORDER BY boScore.score DESC LIMIT 0, 10 ");
			query = session.createSQLQuery(sb.toString());
			recommen = query.list();
			return recommen;
		}
		query = session.createSQLQuery(sb.toString());
		recommen = query.list();
		return recommen;
	}
	@Override
	public List<Object[]> findTopNRecommender() {
		SessionFactory sdf = this.getHibernateTemplate().getSessionFactory();
		Session session = sdf.getCurrentSession();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT DISTINCT bo.id, bo.name AS boName, bo.book_desc AS content, bmg.img_addr,bty.name AS btyName, pb.name AS pbName, au.name AS auName,boScore.score " + 
				  " FROM books AS bo " + 
				  "LEFT JOIN books_bookstype_map AS bmap  " + 
				  "ON bo.id = bmap.books_id  " + 
				  "LEFT JOIN books_type as bty " + 
				  "ON bmap.bookstype_id = bty.id  " + 
				  "LEFT JOIN books_img as bmg  " + 
				  "ON bmg.books_id = bo.id " + 
				  "  LEFT JOIN publish AS pb " + 
				  "	 ON pb.id = bo.publish_id " + 
				  "	 LEFT JOIN author AS au " + 
				  "	 ON au.id = bo.author_id " +
				  "LEFT JOIN user_book_score_map as boScore  " + 
				  "ON boScore.books_id = bo.id  " + 
				  "WHERE bmg.priority = 1 ORDER BY boScore.score DESC LIMIT 0, 10");
		Query query = session.createSQLQuery(sb.toString());
		List<Object[]> recommen  = query.list();
		return recommen;
	}
}
