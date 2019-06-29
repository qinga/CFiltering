package com.jju.cfiltering.service.impl;

import com.jju.cfiltering.dao.LoginDao;
import com.jju.cfiltering.entity.Manager;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.LoginService;

public class LoginServiceImpl implements LoginService{

	private LoginDao logDao;
	@Override
	public User persistLogin(User user) {
		return logDao.persistLogin(user);
	}
	@Override
	public User getUserByName(String userName) {
		return logDao.getUserByName(userName);
	}
	@Override
	public Manager persistLogin(Manager manager) {
		return logDao.persistLogin(manager);
	}
	
	public void setLogDao(LoginDao logDao) {
		this.logDao = logDao;
	}
}
