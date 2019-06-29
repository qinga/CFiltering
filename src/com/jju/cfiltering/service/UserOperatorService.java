package com.jju.cfiltering.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jju.cfiltering.entity.User;

public interface UserOperatorService {

	public List<User> getAllUserInfo();

	public List<User> getOnePageUser(DetachedCriteria dt, Integer pageNumber, Integer pageSize);

}
