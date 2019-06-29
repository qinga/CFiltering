package com.jju.cfiltering.service;

import com.jju.cfiltering.entity.User;

public interface UserRegisterService {
	
	public void saveUser(User user);

	public User findUser(User user);
	
}
