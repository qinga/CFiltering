package com.jju.cfiltering.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.jju.cfiltering.dao.LearnPathDao;

public class LearnPathDaoImpl extends HibernateDaoSupport implements LearnPathDao{

	//学习路线
	@Override
	public List<Object[]> getLearnPath(String topDirect) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		StringBuffer sql = new StringBuffer();
		Query query = null;
		if(topDirect == null) {
			 sql.append("SELECT id,name,path_img, mark_direct FROM learnpath WHERE beginner_book_flag = 1");
			 query = session.createSQLQuery(sql.toString());
			 return query.list();
		}else {
			sql.append("SELECT id,name,path_img, mark_direct FROM learnpath WHERE beginner_book_flag = 0 ORDER BY id DESC LIMIT 0, 5 ");
			 query = session.createSQLQuery(sql.toString());
			 return query.list();
		}
		
	}

	//新手入门，哪里找方向路线。。新手入门，有视频id
	@Override
	public List<Object[]> findDirect(Integer directID) {
		SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		String sql = "     SELECT id, name, learn_path_detail, video_path, mark_direct  " + 
				     "     FROM learnpath WHERE id = ? ";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, directID);
		List<Object[]> re = query.list();
		return re.size() > 0 ? re : null;
	}

}
