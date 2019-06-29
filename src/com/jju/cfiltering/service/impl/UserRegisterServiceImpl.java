package com.jju.cfiltering.service.impl;

import com.jju.cfiltering.dao.UserRegisterDao;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.UserRegisterService;

public class UserRegisterServiceImpl  implements UserRegisterService{

	private UserRegisterDao userDao;
	
	
	public void setUserDao(UserRegisterDao userDao) {
		this.userDao = userDao;
	}


	@Override
	public void saveUser(User user) {
		 userDao.save(user);
	}


	@Override
	public User findUser(User user) {
		return userDao.findUser(user);
	}
	
	
}
