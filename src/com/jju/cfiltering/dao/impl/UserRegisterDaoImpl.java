package com.jju.cfiltering.dao.impl;

import java.util.List;

import com.jju.cfiltering.dao.UserRegisterDao;
import com.jju.cfiltering.entity.User;

public class UserRegisterDaoImpl extends BaseDaoImpl<User> implements UserRegisterDao {

	@Override
	public void save(User user) {
		super.save(user);
	}

	@Override
	public User findUser(User user) {
		List<User> userInfo  = this.getHibernateTemplate().findByExample(user);
		if(userInfo.size() > 0) {
			return userInfo.get(0);
		}
		return null;
	}


}
