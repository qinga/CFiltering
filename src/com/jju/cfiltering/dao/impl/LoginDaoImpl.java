package com.jju.cfiltering.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.jju.cfiltering.dao.LoginDao;
import com.jju.cfiltering.entity.Manager;
import com.jju.cfiltering.entity.User;

public class LoginDaoImpl extends HibernateDaoSupport implements LoginDao{

	@Override
	public User persistLogin(User user) {
		
		List<User> userInfo  = this.getHibernateTemplate().findByExample(user);
		if(userInfo.size() > 0) {
			return userInfo.get(0);
		}
		return null;
	}

	@Override
	public Manager persistLogin(Manager manager) {
		List<Manager> managerInfo  = this.getHibernateTemplate().findByExample(manager);
		if(managerInfo.size() > 0) {
			return managerInfo.get(0);
		}
		return null;
	}
	
	@Override
	public User getUserByName(String userName) {
		User u = new User();
		u.setName(userName);
		List<User> user = this.getHibernateTemplate().findByExample(u);
		if(user.size() > 0) {
			return user.get(0);
		}else return null;
	}

}
