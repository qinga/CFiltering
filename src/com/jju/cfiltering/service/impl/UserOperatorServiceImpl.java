package com.jju.cfiltering.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jju.cfiltering.dao.UserOperatorDao;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.UserOperatorService;

public class UserOperatorServiceImpl implements UserOperatorService {

	private UserOperatorDao userOperatorDao;
	@Override
	public List<User> getAllUserInfo() {
		return userOperatorDao.getAllUserInfo();
	}

	public void setUserOperatorDao(UserOperatorDao userOperatorDao) {
		this.userOperatorDao = userOperatorDao;
	}

	@Override
	public List<User> getOnePageUser(DetachedCriteria dt,Integer pageNumber, Integer pageSize) {
		System.out.println(pageNumber);
		return userOperatorDao.getOnePageUser(dt, pageNumber, pageSize);
	}

}

