package com.jju.cfiltering.dao.impl;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.jju.cfiltering.dao.SaveDyShareDao;
import com.jju.cfiltering.entity.DynamicShare;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.util.CFileUtil;

public class SaveDyShareDaoImpl extends BaseDaoImpl<DynamicShare> implements SaveDyShareDao{

	public void saveArticle(DynamicShare dynamicShare, User u) {
		SessionFactory factory = this.getHibernateTemplate().getSessionFactory();
		Session session = factory.getCurrentSession();
		Set<DynamicShare> dy= new HashSet<DynamicShare>();
		dy.add(dynamicShare);
		
		u.setDynamicShare(dy);
		dynamicShare.setUser(u);
		
		session.save(dynamicShare);
		session.saveOrUpdate(u);
		
	}

	@Override
	public List<Object[]> findDyShare() {
		SessionFactory factory = this.getHibernateTemplate().getSessionFactory();
		Session session = factory.getCurrentSession();
		String sql = " 	SELECT dynamic_share.id,dynamic_share.contents,dynamic_share.publish_time,dynamic_share.image_addr, " + 
				"			(SELECT COUNT(comments) FROM user_dynamic_share_map WHERE dynamic_share.id = user_dynamic_share_map.dynamicshare_id) " + 
				"						as comments," + 
				"			(SELECT SUM(thumbs_up) FROM user_dynamic_share_map WHERE dynamic_share.id = user_dynamic_share_map.dynamicshare_id) " + 
				"						as thumbUp " + 
				"     FROM dynamic_share " + 
				"     ORDER BY dynamic_share.id";
		Query query = session.createSQLQuery(sql);
		List<Object[]> reData = query.list();
		return reData.size() > 0 ? reData : null;
	}

	@Override
	public List<Object[]> findPersonShare(Integer userId, Integer start, Integer size) {
		SessionFactory factory = this.getHibernateTemplate().getSessionFactory();
		Session session = factory.getCurrentSession();
		StringBuffer sb = new StringBuffer();
		sb.append("             SELECT dynamic_share.id,dynamic_share.title,dynamic_share.article_type,dynamic_share.contents,dynamic_share.publish_time,dynamic_share.image_addr, dynamic_share.skim_count, " + 
				"						(SELECT COUNT(comments) FROM user_dynamic_share_map WHERE dynamic_share.id = user_dynamic_share_map.dynamicshare_id) " + 
				"										as comments, " + 
				"						(SELECT COUNT(thumbs_up) FROM user_dynamic_share_map WHERE dynamic_share.id = user_dynamic_share_map.dynamicshare_id) " + 
				"										as thumbUp " + 
				"				     FROM dynamic_share ");
		if(start != null && size != null) {
			sb.append("				 WHERE user_id = ? " + 
					  "				 ORDER BY dynamic_share.id " +
					  "			     LIMIT "+(start-1)*size+" ,"+size+" ");
		}else {
			sb.append("				     ORDER BY dynamic_share.publish_time DESC" +
					  "					 LIMIT 0 ,"+userId+" ");
		}
		Query query = session.createSQLQuery(sb.toString());
		if(start != null && size != null) {
			query.setParameter(0, userId);
		}
		List<Object[]> reData = query.list();
		return reData.size() > 0 ? reData : null;
	}

	@Override
	public void saveGood(Integer shareID, Integer userID, Integer goodCount) {
		SessionFactory factory = this.getHibernateTemplate().getSessionFactory();
		Session session = factory.getCurrentSession();
		Query query = null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, thumbs_up, user_id, dynamicshare_id FROM  " + 
			     "user_dynamic_share_map WHERE user_id = ? and dynamicshare_id = ? ");
		query = session.createSQLQuery(sb.toString());
		query.setParameter(0, userID);
		query.setParameter(1, shareID);
		List<Object[]> dynamic = query.list();
		if(dynamic.size() > 0) {
			//说明用户点赞过，在点
			sb.delete(0, sb.length());
			sb.append("  UPDATE user_dynamic_share_map SET thumbs_up = ?  " + 
					  "  WHERE id = ? ");
			Integer id = (Integer) dynamic.get(0)[0];
			Integer count = 0;
			if(dynamic.get(0)[1] != null) {
				count = (Integer) dynamic.get(0)[1];
			}
			query = session.createSQLQuery(sb.toString());
			query.setParameter(0, goodCount + count);
			query.setParameter(1, id);
			query.executeUpdate();
		}else {
			sb.delete(0, sb.length());
			sb.append(" INSERT INTO user_dynamic_share_map VALUES(null, null, ?, null, ?, ?) ");
			query = session.createSQLQuery(sb.toString());
			query.setParameter(0, goodCount);
			query.setParameter(1, userID);
			query.setParameter(2, shareID);
			query.executeUpdate();
		}
	}

	@Override
	public void saveOpPin(Integer shareID, Integer userID, String coments) {
		SessionFactory factory = this.getHibernateTemplate().getSessionFactory();
		Session session = factory.getCurrentSession();
		Query query = null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, comments, user_id, dynamicshare_id FROM  " + 
			     "user_dynamic_share_map WHERE user_id = ? and dynamicshare_id = ? ");
		query = session.createSQLQuery(sb.toString());
		query.setParameter(0, userID);
		query.setParameter(1, shareID);
		//查找是否评论或赞过。
		List<Object[]> dynamic = query.list();
		//评论或赞过
		if(dynamic.size() > 0) {
			//说明用户评论或赞过，追加
			sb.delete(0, sb.length());
			sb.append("  UPDATE user_dynamic_share_map SET comments = ?  " + 
					  "  WHERE id = ? ");
			Integer id = (Integer) dynamic.get(0)[0];
			//评论内容
			StringBuffer comment =  new StringBuffer();
			
			query = session.createSQLQuery(sb.toString());
			//评论内容
			if(! new String((String) dynamic.get(0)[1]+"").equals("")) {
				comment.append(dynamic.get(0)[1]);
				comment.append(";");
			}
			query.setParameter(0, comment.append(coments).toString());
			query.setParameter(1, id);
			query.executeUpdate();
		}else {
			sb.delete(0, sb.length());
			sb.append(" INSERT INTO user_dynamic_share_map VALUES(null, ?, null, ?, ?, ?) ");
			query = session.createSQLQuery(sb.toString());
			query.setParameter(0, coments);
			query.setParameter(1, CFileUtil.formateDate(new Date()));
			query.setParameter(2, userID);
			query.setParameter(3, shareID);
			query.executeUpdate();
		}
	}

}
