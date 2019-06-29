package com.jju.cfiltering.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jju.cfiltering.entity.User;

public interface UserOperatorDao extends BaseDao<User> {

	public List<User> getAllUserInfo();

	public List<User> getOnePageUser(DetachedCriteria dt,Integer pageNumber, Integer pageSize);
}
