package com.jju.cfiltering.dao;

import com.jju.cfiltering.entity.User;

public interface UserRegisterDao extends BaseDao<User>{

	public User findUser(User user);

	
}
