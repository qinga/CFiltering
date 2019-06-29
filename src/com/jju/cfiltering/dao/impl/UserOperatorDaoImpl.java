package com.jju.cfiltering.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jju.cfiltering.dao.UserOperatorDao;
import com.jju.cfiltering.entity.User;

public class UserOperatorDaoImpl extends BaseDaoImpl<User> implements UserOperatorDao {

	public List<User> getAllUserInfo() {
		return (List<User>) this.getHibernateTemplate().find("from User");
	}

	@Override
	public List<User> getOnePageUser(DetachedCriteria dt, Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return super.getPageList(dt, (pageNumber-1)*pageSize, pageSize);
	}

}
